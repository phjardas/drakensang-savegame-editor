package de.jardas.drakensang.dao.inventory;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.db.UpdateStatementBuilder;
import de.jardas.drakensang.shared.model.Schaden;
import de.jardas.drakensang.shared.model.inventory.Weapon;

public class WeaponDao extends InventoryItemDao<Weapon> {
	private static final String[] FIELDS = { "Guid", "_ID", "_Level",
			"_Layers", "Transform", "Id", "Graphics", "IsCharGraphics",
			"AnimSet", "Physics", "Name", "LookAtText", "EquipmentType",
			"WeaponAnimGroup", "AlternativeWeaponAnimGroup", "TaAttr",
			"SoundSet", "WpW6", "WpW6plus", "WpTPKK1", "WpTPKK2", "Gew",
			"WpLg", "WpBF", "WpINI", "Value", "WpATmod", "WpPAmod", "WpDK",
			"WpATRange", "WpReload", "WpD1", "WpTP1", "WpTP2", "WpTP3",
			"WpTP4", "WpTP5", "QuestId", "PickingRange", "WpAmmoCategory",
			"WpAmmoGraphicsAttr", "MaxStackCount", "StackCount",
			"ScriptPreset", "ScriptOverride", "LimitedScript",
			"PermanentEffect", "CanUse", "CanDestroy", "IconBrush",
			"LookAtUnidentified", "IdentificationTalent", "InfoUnIdentified",
			"InfoIdentified", "IdentificationDifficulty", "IsIdentified",
			"IsMagical", "Robable", "PickingHeight", "UseAnim",
			"Characteristica", "SoundUI", "StorageGUID", "Lootable",
			"VelocityVector", "PhysicCategory", "StorageSlotId", "IsTradeItem",
			"InventoryType", "IsEquiped", "EquipmentSlotId",
			"SpellTargetOffset", "LocalizeLookAtText", "IsSplitting",
			"InfiniteStack", "UseTalent", "EntityDiscovered", "WpATmod",
			"WpPAMod", };

	public WeaponDao() {
		super(Weapon.class, "_Instance_Weapon");
	}

	@Override
	public Weapon load(ResultSet results) {
		final Weapon weapon = super.load(results);

		try {
			weapon.setSchaden(new Schaden(results.getInt("WpW6"), results
					.getInt("WpW6plus")));
			weapon.setAttackeMod(results.getInt("WpATmod"));
			weapon.setParadeMod(results.getInt("WpPAmod"));

			return weapon;
		} catch (SQLException e) {
			throw new DrakensangException("Error loading ", e);
		}
	}

	@Override
	protected void appendUpdateStatements(UpdateStatementBuilder builder,
			Weapon item) {
		super.appendUpdateStatements(builder, item);

		builder.append("WpW6 = ?", item.getSchaden().getDiceMultiplier());
		builder.append("WpW6plus = ?", item.getSchaden().getAddition());
		builder.append("WpATmod = ?", item.getAttackeMod());
		builder.append("WpPAmod = ?", item.getParadeMod());
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}
}
