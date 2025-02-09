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


package uk.me.mantas.eternity.game;

import static uk.me.mantas.eternity.game.UnityEngine.Texture2D;

public class GenericTalent {
	public DatabaseString DisplayName;
	public DatabaseString Description;
	public GenericAbility[] Abilities;
	public Texture2D Icon;
	public GenericTalent.TalentCategory Category;
	public GenericTalent.TalentType Type;
	public AbilityMod[] AbilityMods;
	public GenericTalent.SkillBonus[] SkillBonuses;

	public enum TalentType {
		GrantNewAbility
		, ModExistingAbility
	}

	public enum TalentCategory {
		Undefined
		, Class
		, Offense
		, Defense
		, MixedOrUtility
	}

	public static class SkillBonus {
		public CharacterStats.SkillType Skill;
		public int Bonus;
	}
}
