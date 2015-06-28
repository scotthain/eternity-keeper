/**
 *  Eternity Keeper, a Pillars of Eternity save game editor.
 *  Copyright (C) 2015 Kim Mantas
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


package uk.me.mantas.eternity.serializer;

import com.google.common.primitives.UnsignedInteger;
import uk.me.mantas.eternity.game.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class TypeMap {
	public static final Map<String, Class> map =
		new HashMap<String, Class>() {{
			put("System.Int32", int.class);
			put("System.String", String.class);
			put("System.Object", Object.class);
			put("System.Boolean", boolean.class);
			put("System.Single", float.class);
			put("System.Guid", UUID.class);
			put("System.UInt32", UnsignedInteger.class);

			put("System.Single[]", Float[].class);
			put("System.Int32[]", Integer[].class);
			put("System.Int32[][]", Integer[][].class);
			put("System.Boolean[]", Boolean[].class);
			put("System.Byte[]", Byte[].class);
			put("System.Collections.Generic.List", CSharpCollection.class);
			put("System.Collections.Generic.Dictionary", HashMap.class);
			put("System.Collections.Hashtable", Hashtable.class);
			put("System.Collections.Generic.SortedList", SortedList.class);

			put("UnityEngine.Color", UnityEngine.Colour.class);
			put("UnityEngine.Vector2", UnityEngine.Vector2.class);
			put("UnityEngine.Vector3", UnityEngine.Vector3.class);
			put("UnityEngine.Vector3[]", UnityEngine.Vector3[].class);
			put("UnityEngine.Vector3[][]", UnityEngine.Vector3[][].class);

			put("ObjectPersistencePacket", ObjectPersistencePacket.class);
			put("ComponentPersistencePacket", ComponentPersistencePacket.class);
			put("ItemMod", ItemMod.class);
			put("AppearancePiece", AppearancePiece.class);
			put("Team", Team.class);
			put("Gender", Gender.class);
			put("Affliction", Affliction.class);
			put("GenericTalent", GenericTalent.class);
			put("StatusEffect", StatusEffect.class);
			put("InventoryItem", InventoryItem.class);
			put("EternityTimeInterval", EternityTimeInterval.class);
			put("CurrencyValue", CurrencyValue.class);
			put("KeyControl", KeyControl.class);
			put("EternityDateTime", EternityDateTime.class);
			put("ExperienceSerializerPacket", ExperienceSerializerPacket.class);
			put("Reputation", Reputation.class);
			put("Reputation[]", Reputation[].class);
			put("Disposition", Disposition.class);
			put("NotesPage", NotesPage.class);
			put("Item", Item.class);
			put("GameDifficulty", GameDifficulty.class);
			put("PrerequisiteData", PrerequisiteData.class);
			put("CharacterDatabaseString", CharacterDatabaseString.class);
			put("GenericSpell", GenericSpell.class);
			put("Phrase", Phrase.class);

			put("NotesPage", NotesPage.class);
			put("NotesPage+NoteEntry", NotesPage.NoteEntry.class);

			// TODO: This is a little weird.
			//put("Phrase[]", Phrase[].class);
			put("Phrase[]", String[].class);

			put("OCL+State", OCL.State.class);

			put("StrongholdEvent", StrongholdEvent.class);
			put("StrongholdAdventure", StrongholdAdventure.class);
			put("StrongholdAdventure+Reward", StrongholdAdventure.Reward.class);
			put("StrongholdHireling", StrongholdHireling.class);
			put("StrongholdGuestHireling", StrongholdGuestHireling.class);
			put("StrongholdPrisonerData", StrongholdPrisonerData.class);
			put("StrongholdUpgrade+Type", StrongholdUpgrade.Type.class);
			put("StrongholdVisitorSerializeData", StrongholdVisitorSerializeData.class);

			put("GenericAbility+AbilityType", GenericAbility.AbilityType.class);

			put("Grimoire+SpellChapter", Grimoire.SpellChapter.class);
			put("Grimoire+SpellChapter[]", Grimoire.SpellChapter[].class);

			put("NPCAppearance+Sex", NPCAppearance.Sex.class);
			put("NPCAppearance+Race", NPCAppearance.Race.class);
			put("NPCAppearance+Subrace", NPCAppearance.Subrace.class);

			put("Religion+Deity", Religion.Deity.class);
			put("Religion+PaladinOrder", Religion.PaladinOrder.class);

			put("PartyMemberAI+DetectionObject", PartyMemberAI.DetectionObject.class);

			put("AIController+AISummonType", AIController.AISummonType.class);
			put("AIController+AggressionType", AIController.AggressionType.class);

			put("CharacterStats+Race", CharacterStats.Race.class);
			put("CharacterStats+Subrace", CharacterStats.Subrace.class);
			put("CharacterStats+Culture", CharacterStats.Culture.class);
			put("CharacterStats+Class", CharacterStats.Class.class);
			put("CharacterStats+Background", CharacterStats.Background.class);
			put("CharacterStats+NoiseLevelType", CharacterStats.NoiseLevelType.class);

			put("CompanionNames+Companions", CompanionNames.Companions.class);

			put("MapData+VisibilityType", MapData.VisibilityType.class);
			put("MapData+VisibilityType[]", MapData.VisibilityType[].class);

			put("QuestManager+QuestTimestamps", QuestManager.QuestTimestamps.class);

			put(
				"AchievementTracker+TrackedAchievementStat"
				, AchievementTracker.TrackedAchievementStat.class);
		}};

	public static final Map<Class, String> reverseMap = new HashMap<>();

	static {
		for (Entry<String, Class> item : map.entrySet()) {
			reverseMap.put(item.getValue(), item.getKey());
		}

		reverseMap.put(Integer.class, "System.Int32");
		reverseMap.put(Boolean.class, "System.Boolean");
		reverseMap.put(Float.class, "System.Single");
		reverseMap.put(Double.class, "System.Double");
		reverseMap.put(Short.class, "System.Int16");
		reverseMap.put(Byte.class, "System.Byte");
	}
}
