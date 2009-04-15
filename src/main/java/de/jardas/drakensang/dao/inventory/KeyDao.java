package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Key;


public class KeyDao extends InventoryItemDao<Key> {
    private static final String[] FIELDS = {
            "Guid", "_ID", "_Level", "_Layers", "Transform", "Id", "Graphics",
            "Placeholder", "Physics", "Name", "LookAtText", "QuestId", "Gew",
            "PickingRange", "ScriptPreset", "ScriptOverride", "CanUse",
            "CanDestroy", "LockId", "IconBrush", "LookAtUnidentified",
            "IdentificationTalent", "InfoUnIdentified", "InfoIdentified",
            "IdentificationDifficulty", "IsIdentified", "Robable",
            "PickingHeight", "UseAnim", "Value", "StorageGUID", "Lootable",
            "VelocityVector", "PhysicCategory", "StorageSlotId", "SoundUI",
            "IsTradeItem", "LocalizeLookAtText", "IsMagical", "LimitedScript",
            "UseTalent", "EntityDiscovered",
        };

    public KeyDao() {
        super(Key.class, "_Instance_Key");
    }

    @Override
    protected String[] getFields() {
        return FIELDS;
    }
}
