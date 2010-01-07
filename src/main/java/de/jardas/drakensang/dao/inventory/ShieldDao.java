package de.jardas.drakensang.dao.inventory;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.jardas.drakensang.model.inventory.Shield;
import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.db.UpdateStatementBuilder;


public class ShieldDao extends InventoryItemDao<Shield> {
    private static final String[] FIELDS = {
            "Guid", "_ID", "_Level", "_Layers", "Transform", "Id", "Graphics",
            "Physics", "Name", "LookAtText", "EquipmentType", "Gew", "WpINI",
            "Value", "WpATmod", "WpPAmod", "QuestId", "PickingRange",
            "WpAmmoCategory", "MaxStackCount", "StackCount", "ScriptPreset",
            "ScriptOverride", "LimitedScript", "PermanentEffect", "CanUse",
            "CanDestroy", "IconBrush", "LookAtUnidentified",
            "IdentificationTalent", "InfoUnIdentified", "InfoIdentified",
            "IdentificationDifficulty", "IsIdentified", "Lootable", "Robable",
            "PickingHeight", "UseAnim", "SoundSet", "ArMaterial", "StorageGUID",
            "VelocityVector", "PhysicCategory", "StorageSlotId", "SoundUI",
            "IsTradeItem", "InventoryType", "IsEquiped", "EquipmentSlotId",
            "SpellTargetOffset", "LocalizeLookAtText", "IsMagical",
            "IsSplitting", "InfiniteStack", "UseTalent", "EntityDiscovered",
        };

    public ShieldDao() {
        super(Shield.class, "_Instance_Shield");
    }

    @Override
    public Shield load(ResultSet results) {
        final Shield shield = super.load(results);

        try {
            shield.setAttackeMod(results.getInt("WpATmod"));
            shield.setParadeMod(results.getInt("WpPAmod"));

            return shield;
        } catch (SQLException e) {
            throw new DrakensangException("Error loading ", e);
        }
    }

    @Override
    protected void appendUpdateStatements(UpdateStatementBuilder builder,
        Shield item) {
        super.appendUpdateStatements(builder, item);

        builder.append("WpATmod = ?", item.getAttackeMod());
        builder.append("WpPAmod = ?", item.getParadeMod());
    }

    @Override
    protected String[] getFields() {
        return FIELDS;
    }
}
