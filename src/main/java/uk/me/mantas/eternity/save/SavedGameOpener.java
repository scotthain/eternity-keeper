package uk.me.mantas.eternity.save;

import org.apache.commons.io.FileUtils;
import org.cef.callback.CefQueryCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.me.mantas.eternity.Environment;
import uk.me.mantas.eternity.Settings;
import uk.me.mantas.eternity.game.ObjectPersistencePacket;
import uk.me.mantas.eternity.handlers.OpenSavedGame;
import uk.me.mantas.eternity.serializer.SharpSerializer;
import uk.me.mantas.eternity.serializer.properties.Property;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

public class SavedGameOpener implements Runnable {
	private final String saveGameLocation;
	private final CefQueryCallback callback;

	public SavedGameOpener (String saveGameLocation, CefQueryCallback callback) {
		this.saveGameLocation = saveGameLocation;
		this.callback = callback;
	}

	@Override
	public void run () {
		File savedgame = new File(saveGameLocation);
		File mobileObjectsFile = new File(savedgame, "MobileObjects.save");

		if (!mobileObjectsFile.exists()) {
			OpenSavedGame.notExists(callback);
			return;
		}

		List<Property> gameObjects = deserialize(mobileObjectsFile);
		Map<String, Property> characters = extractCharacters(gameObjects);
		sendJSON(characters);
	}

	private void sendJSON (Map<String, Property> characters) {
		JSONObject[] jsonObjects = characters.entrySet().stream().map(entry -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("GUID", entry.getKey());

			Property property = entry.getValue();
			ObjectPersistencePacket packet = (ObjectPersistencePacket) property.obj;
			boolean isCompanion = packet.ObjectName.startsWith("Companion");
			String name = extractName(packet);

			Optional<Map<String, Object>> stats = extractCharacterStats(packet);
			if (!stats.isPresent()) {
				// This is a stored character that is not presently in the party.
				return Optional.empty();
			}

			if (stats.get().get("OverrideName") != null
				&& !stats.get().get("OverrideName").equals("")) {

				name = (String) stats.get().get("OverrideName");
			} else if (isCompanion) {
				stats.get().put("OverrideName", name);
			}

			jsonObject.put("isCompanion", isCompanion);
			jsonObject.put("name", name);
			jsonObject.put("portrait", extractPortrait(packet, isCompanion));
			jsonObject.put("stats", stats.get());

			return Optional.of(jsonObject);
		}).filter(Optional::isPresent)
		.map(Optional::get)
		.toArray(JSONObject[]::new);

		String json = new JSONArray(jsonObjects).toString();
		callback.success(json);
	}

	private Optional<Map<String, Object>> extractCharacterStats (ObjectPersistencePacket packet) {
		return Arrays.stream(packet.ComponentPackets)
			.filter(c -> c.TypeString.equals("CharacterStats"))
			.findFirst()
			.map(c ->
				c.Variables.entrySet().stream()
					.filter(entry -> isSupportedType(entry.getValue()))
					.collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
	}

	private boolean isSupportedType (Object obj) {
		String cls = obj.getClass().getSimpleName();
		return cls.equals("int") || cls.equals("Integer")
			|| cls.equals("float") || cls.equals("Float")
			|| cls.equals("double") || cls.equals("Double")
			|| cls.equals("boolean") || cls.equals("Boolean")
			|| cls.equals("String");
	}

	private String extractName (ObjectPersistencePacket packet) {
		String name = "";
		if (packet.ObjectName.contains("_")) {
			name = packet.ObjectName.split("_")[1];
			if (name.contains("(")) {
				name = name.split("\\(")[0];
			}
		}

		return name;
	}

	private String extractPortrait (ObjectPersistencePacket packet, boolean isCompanion) {
		JSONObject settings = Settings.getInstance().json;
		Optional<String> portraitSubPath =
			Arrays.stream(packet.ComponentPackets)
				.filter(c -> c.TypeString.equals("Portrait"))
				.findFirst()
				.map(c -> (String) c.Variables.get("m_textureLargePath"));

		if (isCompanion) {
			String name = extractName(packet);
			portraitSubPath = Optional.of(
				String.format(
					"data/art/gui/portraits/companion/portrait_%s_lg.png"
					, name.toLowerCase()));
		}

		if (!portraitSubPath.isPresent()) {
			return "";
		}

		String installationPath;
		try {
			installationPath = settings.getString("gameLocation");
		} catch (JSONException e) {
			return "";
		}

		Path portraitPath =
			Paths.get(installationPath)
				.resolve(Environment.PILLARS_DATA_DIR)
				.resolve(portraitSubPath.get());

		if (!portraitPath.toFile().exists()) {
			System.err.printf(
				"Game files contained reference to portrait at '%s' "
				+ "but it didn't exist.%n", portraitPath.toString());

			return "";
		}

		try {
			byte[] portraitData = FileUtils.readFileToByteArray(portraitPath.toFile());
			return Base64.getEncoder().encodeToString(portraitData);
		} catch (IOException e) {
			System.err.printf(
				"Unable to open portrait file '%s': %s%n"
				, portraitPath.toString()
				, e.getMessage());
		}

		return "";
	}

	private Map<String, Property> extractCharacters (List<Property> gameObjects) {
		Map<String, Property> characters = new HashMap<>();
		for (Property property : gameObjects) {
			if (!(property.obj instanceof ObjectPersistencePacket)) {
				continue;
			}

			ObjectPersistencePacket packet = (ObjectPersistencePacket) property.obj;
			if (packet.ObjectName == null || packet.ObjectID == null) {
				continue;
			}

			if (packet.ObjectName.startsWith("Player_")
				|| packet.ObjectName.startsWith("Companion_")) {

				characters.put(packet.ObjectID, property);
			}
		}

		return characters;
	}

	private List<Property> deserialize (File mobileObjectsFile) {
		List<Property> objects = new ArrayList<>();

		try {
			SharpSerializer deserializer =
				new SharpSerializer(mobileObjectsFile.getAbsolutePath());

			Optional<Property> objectCount = deserializer.deserialize();
			if (!objectCount.isPresent()) {
				OpenSavedGame.deserializationError(callback);
				return objects;
			}

			int count = (int) objectCount.get().obj;
			for (int i = 0; i < count; i++) {
				Optional<Property> gameObject = deserializer.deserialize();
				if (gameObject.isPresent()) {
					objects.add(gameObject.get());
				}
			}
		} catch (FileNotFoundException e) {
			OpenSavedGame.deserializationError(callback);
		}

		return objects;
	}
}
