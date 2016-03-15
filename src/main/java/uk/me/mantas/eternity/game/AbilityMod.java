/**
 * Eternity Keeper, a Pillars of Eternity save game editor.
 * Copyright (C) 2016 the authors.
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

package uk.me.mantas.eternity.game;

import uk.me.mantas.eternity.game.UnityEngine.GameObject;

public class AbilityMod {
	public AbilityMod.AbilityModType Type;
	public float Value;
	public StatusEffectParams[] StatusEffects;
	public AbilityMod.ReplaceObjectParams[] ReplaceObjects;
	public GenericAbility.AbilityType SourceType;
	public Equippable EquipmentOrigin;

	public enum AbilityModType {
		AdditionalUse
		, AddAbilityStatusEffects
		, AddAttackStatusEffects
		, AttackAccuracyBonus
		, WoundThresholdAdjustment
		, NegativeReligiousTraitMultiplier
		, FinishingBlowDamagePercentAdjustment
		, AttackDTBypass
		, AttackSpeedMultiplier
		, ReplaceParticleFX
		, AddAttackStatusEffectOnCasterOnly
	}

	public static class ReplaceObjectParams {
		public GameObject Existing;
		public GameObject ReplaceWith;
	}
}
