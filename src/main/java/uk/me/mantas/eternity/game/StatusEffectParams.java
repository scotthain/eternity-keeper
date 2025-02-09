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

import uk.me.mantas.eternity.game.UnityEngine.GameObject;

import java.util.UUID;

public class StatusEffectParams {
	public boolean Persistent;
	public boolean IsHostile;
	public int MaxRestCycles;
	public AttackBase.EffectAttachType VisualEffectAttach;
	public float DestroyVFXDelay = 5f;
	public StatusEffectTrigger TriggerAdjustment;
	public StatusEffectLevelScaling LevelScaling;
	public String Tag;
	public PrerequisiteData[] ApplicationPrerequisites;
	public boolean KeepOnDeath;
	public StatusEffect.ApplyType Apply;
	public StatusEffect.ModifiedStat AffectsStat;
	public CharacterStats.AttributeScoreType AttributeType;
	public CharacterStats.DefenseType DefenseType;
	public DamagePacket.DamageType DmgType;
	public float Value;
	public float ExtraValue;
	public float Duration;
	public boolean LastsUntilCombatEnds;
	public boolean LastsUntilRest;
	public StatusEffectParams.IntervalRateType IntervalRate;
	public boolean HideFromUi;
	public boolean OneHitUse;
	public boolean ChecksReligion;
	public GameObject OnStartVisualEffect;
	public GameObject OnAppliedVisualEffect;
	public GameObject OnStopVisualEffect;
	public GameObject OnAuraVisualEffect;
	public GameObject OnTriggerVisualEffect;
	public Trap TrapPrefab;
	public Equippable EquippablePrefab;
	public AttackBase AttackPrefab;
	public CharacterStats.Race RaceType;
	public CharacterStats.Class ClassType;
	public String Keyword;
	public boolean DontHideFromLog;
	public boolean IgnoreAbilityDeactivation;
	public boolean DoNotScalePlayer;
	public boolean m_deserializeInitialized;
	public GenericAbility OnDamagedCallbackAbility;
	public boolean IsOverTime;
	public boolean IsInstantApplication;
	public int LevelScalingBaseLevel;
	public int LevelScalingLevelIncrement;
	public int LevelScalingMaxLevel;
	public float LevelScalingValueAdjustment;
	public float LevelScalingExtraValueAdjustment;
	public float LevelScalingDurationAdjustment;
	public UUID TrapPrefabSerialized;
	public Trap TrapSerialized2;
	public UUID EquippablePrefabSerialized;
	//public Equippable EquippableSerialized2;
	public String EquippableSerialized2;
	public UUID AttackPrefabSerialized;
	public AttackBase AttackPrefabSerialized2;
	// TODO: Check this.
	//public Affliction AfflictionPrefabSerialized;
	public String AfflictionPrefabSerialized;
	public float UnadjustedDurationAdd;
	public float TemporaryDurationAdjustment;
	public UUID OnDamageCallbackAbilitySerialized;
	public String Description;
	public boolean IsCleanedUp;
	public float MergedValue;

	public enum IntervalRateType {
		None
		, Damage
		, Hazard
		, Footstep
	}
}
