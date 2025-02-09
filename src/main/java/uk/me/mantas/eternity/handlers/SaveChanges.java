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


package uk.me.mantas.eternity.handlers;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import org.json.JSONStringer;
import uk.me.mantas.eternity.Logger;
import uk.me.mantas.eternity.environment.Environment;
import uk.me.mantas.eternity.save.ChangesSaver;

public class SaveChanges extends CefMessageRouterHandlerAdapter {
	private static final Logger logger = Logger.getLogger(SaveChanges.class);

	@Override
	public boolean onQuery (
		CefBrowser browser
		, long id
		, String request
		, boolean persistent
		, CefQueryCallback callback) {

		Environment.getInstance().workers().execute(
			new ChangesSaver(request, callback));

		return true;
	}

	@Override
	public void onQueryCanceled (CefBrowser browser, long id) {
		logger.error("Query #%d was cancelled.%n", id);
	}

	public static String jsonError () {
		return new JSONStringer()
			.object()
				.key("error").value("Error parsing JSON request.")
			.endObject()
			.toString();
	}

	public static String ioError () {
		return new JSONStringer()
			.object()
				.key("error").value("Unable to write new save file.")
			.endObject()
			.toString();
	}

	public static String deserializationError () {
		return new JSONStringer()
			.object()
				.key("error").value("Unable to deserialize new save file.")
			.endObject()
			.toString();
	}
}
