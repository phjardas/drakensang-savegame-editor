package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.shared.model.inventory.Jewelry;


public class JewelryDao extends InventoryItemDao<Jewelry> {
    private static final String[] FIELDS = {
            "Guid", "_ID", "_Level", "_Layers", "Transform", "Id", "Graphics",
            "Physics", "Name", "LimitedScript", "PermanentEffect", "State",
            "LookAtText", "EquipmentType", "Gew", "Value", "QuestId",
            "PickingRange", "ScriptPreset", "ScriptOverride", "CanUse",
            "CanDestroy", "IconBrush", "LookAtUnidentified",
            "IdentificationTalent", "InfoUnIdentified", "InfoIdentified",
            "IdentificationDifficulty", "IsIdentified", "Robable",
            "PickingHeight", "UseAnim", "SoundUI", "StorageGUID", "Lootable",
            "VelocityVector", "PhysicCategory", "StorageSlotId", "IsTradeItem",
            "InventoryType", "IsEquiped", "EquipmentSlotId",
            "LocalizeLookAtText", "IsMagical", "UseTalent", "EntityDiscovered",
        };

    public JewelryDao() {
        super(Jewelry.class, "_Instance_Jewelry");
    }

    @Override
    protected String[] getFields() {
        return FIELDS;
    }
}
