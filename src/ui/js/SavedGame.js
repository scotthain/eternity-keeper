/**
 *  Eternity Keeper, a Pillars of Eternity save game editor.
 *  Copyright (C) 2015 the authors.
 *
 *  Eternity Keeper is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  Eternity Keeper is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

var SavedGame = function () {
	var self = this;
	var disabledForCompanions = [
		'BaseMight', 'BaseConstitution', 'BaseDexterity', 'BaseIntellect', 'BasePerception'
		, 'BaseResolve'];

	self.views = Object.freeze({ATTR: 0, RAW: 1, GLOBALS: 2});

	var defaultState = {
		saveData: {}
		, info: {}
		, activeCharacter: false
		, view: self.views.ATTR
	};

	var populateCharacterList = (container, characters) => {
		container.append(
			characters.map(
				character =>
					$('<li>')
					.data('guid', character.GUID)
					.html('<i class="fa fa-heartbeat"></i> ' + character.name)
					.addClass(character.isDead ? 'dead' : '')
					.click(self.switchCharacter.bind(self, character.GUID))));
	};

	var sortData = unsorted => {
		var sortable = [];
		for (var k in unsorted) {
			if (!unsorted.hasOwnProperty(k)) {
				continue;
			}

			sortable.push([k, unsorted[k]]);
		}

		sortable.sort((a, b) => a[0].toLowerCase() > b[0].toLowerCase() ? 1 : -1);
		return sortable;
	};

	var createBooleanEditor = initialValue => {
		return createEnumEditor(['true', 'false'], initialValue);
	};

	var createEnumEditor = (enumValues, initialValue) => {
		return $('<select></select>')
			.addClass('form-control')
			.append(enumValues.map(v =>
				$('<option></option>').prop('selected', v == initialValue).text(v).val(v)));
	};

	var createRawEditor = (stat, value) => {
		var row = $('<tr></tr>');
		var keyCol = $('<td></td>');
		var valCol = $('<td></td>');
		var editor;

		keyCol.text(stat);
		valCol.data('key', stat);

		if (value.type === 'java.lang.Boolean') {
			editor = createBooleanEditor(value.value);
			valCol.append(editor);
			editor.change(self.update.bind(self));
		} else if (Eternity.structures[value.type] !== undefined) {
			editor = createEnumEditor(Eternity.structures[value.type], value.value);
			valCol.append(editor);
			editor.change(self.update.bind(self));
		} else {
			valCol.prop('contenteditable', true);
			valCol.text(value.value);
			valCol.keyup(self.update.bind(self));
		}

		row.append(keyCol, valCol);
		return row;
	};

	var populateGlobals = globals => {
		var globalsTable = self.html.globalsTable.find('tbody');
		globalsTable.empty();

		var tuples = [];
		for (var p1 in globals) {
			if (!globals.hasOwnProperty(p1)) {
				continue;
			}

			for (var p2 in globals[p1]) {
				if (!globals[p1].hasOwnProperty(p2)) {
					continue;
				}

				for (var k in globals[p1][p2]) {
					if (!globals[p1][p2].hasOwnProperty(k)) {
						continue;
					}

					tuples.push([p1 + '.' + p2, k, globals[p1][p2][k]]);
				}
			}
		}

		tuples.sort((a, b) => a[1].toLowerCase() > b[1].toLowerCase() ? 1 : -1);
		tuples.forEach(tuple => {
			var row = createRawEditor(tuple[1], tuple[2]);
			row.data('key', tuple[0]);
			globalsTable.append(row);
		});
	};

	var populateCharacter = (container, data) => {
		container.find('.portrait')
			.empty()
			.css('background-image', 'url(data:image/png;base64,' + data.portrait + ')')
			.css('background-repeat', 'no-repeat')
			.html((data.isDead) ? '<div>DEAD</div>' : '');

		var sortedData = sortData(data.stats);
		var rawTable = self.html.rawTable.find('tbody');
		rawTable.empty();

		sortedData.forEach(tuple => {
			var stat = tuple[0];
			var value = tuple[1];

			container
				.find('.stats')
				.find('input[data-key="' + stat + '"]')
				.val(value.value.toString())
				.prop('disabled', data.isCompanion && disabledForCompanions.indexOf(stat) > -1);

			rawTable.append(createRawEditor(stat, value));
		});

		container
			.find('.stats')
			.find('input')
			.change(self.update.bind(self))
			.keyup(self.update.bind(self));
	};

	var lastSearch = 0;
	var filterRaw = () => {
		var thisSearch = new Date().valueOf();
		lastSearch = thisSearch;

		var searchString = self.html.searchRaw.val().toLowerCase();
		if (searchString.length < 1) {
			self.html.rawTable.find('tbody tr').show();
			return;
		}

		var matches =
			self.html.rawTable.find('td:last-child')
				.filter((i, el) => $(el).data('key').toLowerCase().includes(searchString));

		if (thisSearch < lastSearch) {
			// There has been another keypress since we calculated the matches, the new keypress
			// takes priority.
			return;
		}

		self.html.rawTable.find('tbody tr').hide();
		matches.each((i, el) => $(el).parent().show());
	};

	self.init = () => {
		self.html.searchRaw.keyup(filterRaw);
	};

	self.state = $.extend({}, defaultState);
	self.html = {};

	self.render = newState => {
		self.state = $.extend({}, defaultState, newState);
		self.html.characterList.empty();
		Eternity.render({saveView: true});

		self.html.menuCharacterAttributes.off();
		self.html.menuCharacterAttributes.click(self.switchView.bind(self, self.views.ATTR));
		self.html.menuCharacterRaw.off();
		self.html.menuCharacterRaw.click(self.switchView.bind(self, self.views.RAW));
		self.html.menuEditGlobals.off();
		self.html.menuEditGlobals.click(self.switchView.bind(self, self.views.GLOBALS));

		Eternity.CurrencyEditor.render({enabled: true, amount: self.state.saveData.currency});
		Eternity.Modifications.html.newSaveName.val(
			Eternity.Modifications.suggestSaveName(self.state.info));
		populateCharacterList(self.html.characterList, self.state.saveData.characters);
		populateGlobals(self.state.saveData.globals);

		if (self.state.activeCharacter) {
			self.html.characterList.find('li')
				.removeClass('active')
				.filter((i, li) => $(li).data('guid') === self.state.activeCharacter)
				.addClass('active');
			var character =
				self.state.saveData.characters.filter(c => c.GUID == self.state.activeCharacter);
			if (character.length > 0) {
				populateCharacter(self.html.character, character[0]);
			}
		} else {
			self.switchCharacter(self.state.saveData.characters[0].GUID);
		}

		$('.view').hide();
		switch(self.state.view) {
			case self.views.RAW:
				self.html.rawTable.show();
				filterRaw();
				break;

			default:
				self.html.character.show();
		}
	};
};

SavedGame.prototype.switchCharacter = function (guid) {
	var self = this;
	self.transition({activeCharacter: guid});
};

SavedGame.prototype.switchView = function (view) {
	var self = this;
	self.transition({view: view});
};

SavedGame.prototype.update = function (e) {
	var self = this;
	var element = $(e.currentTarget);
	var isCol = element.prop('nodeName') === 'TD';
	var isDropdown = element.prop('nodeName') === 'SELECT';
	var value = isCol ? element.text() : element.val();
	var key = isDropdown ? element.parent().data('key') : element.data('key');
	var superKey =
		isDropdown
			? element.parent().parent().data('key')
			: element.parent().data('key');

	if (key === null || key.length < 1) {
		return;
	}

	if (superKey == null || superKey.length < 1) {
		var character =
			self.state.saveData.characters.filter(c => c.GUID === self.state.activeCharacter)[0];
		character.stats[key].value = value;
	} else {
		var explode = superKey.split('.');
		var p1 = explode[0];
		var p2 = explode[1];
		self.state.saveData.globals[p1][p2][key].value = value;
	}

	Eternity.Modifications.transition({modifications: true});
};

$.extend(SavedGame.prototype, Renderer.prototype);
