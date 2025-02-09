/**
 * Eternity Keeper, a Pillars of Eternity save game editor.
 * Copyright (C) 2015 the authors.
 * <p>
 * Eternity Keeper is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * Eternity Keeper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.me.mantas.eternity.environment;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
	Configuration () {}

	public static final int LEGACY_EXE_SIZE = 0x17400;

	private List<String> installationLocations = new ArrayList<String>() {{
		add("Program Files\\GOG Games\\Pillars of Eternity");
		add("Program Files (x86)\\GOG Games\\Pillars of Eternity");
		add("Program Files\\Steam\\SteamApps\\common\\Pillars of Eternity");
		add("Program Files (x86)\\Steam\\SteamApps\\common\\Pillars of Eternity");
	}};

	private List<String> usefulGlobals =
		ImmutableList.of("GameState", "WorldTime", "GlobalVariables");
	public List<String> usefulGlobals () { return usefulGlobals; }

	public long maxLogSize () { return 0x20000; }
	public String pillarsDataDirectory () {	return "PillarsOfEternity_Data"; }
	public String gameStructuresPkg () { return "uk.me.mantas.eternity.game"; }

	public String companionPortraitPath () {
		return "data/art/gui/portraits/companion/portrait_%s_lg.png";
	}

	public List<String> possibleInstallationLocations () { return installationLocations; }
	public void possibleInstallationLocations (final List<String> list) {
		installationLocations = list;
	}

	public Map<String, String> companionNameMap () {
		final Map<String, String> map = new HashMap<>();
		map.put("GM", "Grieving Mother");
		map.put("GGP", "Durance");

		return map;
	}
}
