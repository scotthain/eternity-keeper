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


package uk.me.mantas.eternity;

import org.json.JSONObject;
import org.json.JSONTokener;
import uk.me.mantas.eternity.environment.Environment;

import java.io.*;

public class Settings {
	private static final Logger logger = Logger.getLogger(Settings.class);
	private static Settings instance = null;
	private final File settingsFile;
	public JSONObject json = new JSONObject();

	private Settings () {
		settingsFile = Environment.getInstance().directory().settingsFile();

		try {
			final boolean fileCreated = settingsFile.createNewFile();
			if (fileCreated) {
				writeBlankJSON();
			} else {
				readSettingsJSON();
			}
		} catch (final IOException e) {
			logger.error("Error processing settings file: %s%n", e.getMessage());
		}
	}

	private void readSettingsJSON () {
		try (final Reader reader =
			new BufferedReader(new InputStreamReader(new FileInputStream(settingsFile), "UTF-8"))) {

			json = new JSONObject(new JSONTokener(reader));
		} catch (final IOException e) {
			logger.error("Error reading file '%s': %s%n"
				, settingsFile.getAbsolutePath()
				, e.getMessage());
		}
	}

	private void writeBlankJSON () {
		try (final Writer writer =
			new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(settingsFile), "UTF-8"))) {

			writer.write("{}");
		} catch (final IOException e) {
			logger.error(
				"Unable to write to '%s': %s%n"
				, settingsFile.getAbsolutePath()
				, e.getMessage());
		}
	}

	public void save () {
		try (final Writer writer =
			new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(settingsFile), "UTF-8"))) {

			writer.write(json.toString());
		} catch (final IOException e) {
			logger.error("Unable to save settings file: %s%n", e.getMessage());
		}
	}

	public static Settings getInstance () {
		if (instance == null) {
			initialise();
		}

		return instance;
	}

	public static void initialise () {
		instance = new Settings();
	}

	public static void clear () {
		instance = null;
	}
}
