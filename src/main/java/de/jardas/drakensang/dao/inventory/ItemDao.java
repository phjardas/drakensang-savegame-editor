package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Item;


public class ItemDao extends InventoryItemDao<Item> {
    private static final String[] FIELDS = {
            "Guid", "_ID", "_Level", "_Layers", "Transform", "Id", "Graphics",
            "Placeholder", "Physics", "Name", "LookAtText", "QuestId", "Value",
            "Gew", "PickingRange", "ScriptPreset", "ScriptOverride",
            "LimitedScript", "CanUse", "TaBonus", "UseTalent", "CanDestroy",
            "IconBrush", "MaxStackCount", "StackCount", "LookAtUnidentified",
            "IdentificationTalent", "InfoUnIdentified", "InfoIdentified",
            "IdentificationDifficulty", "IsMagical", "IsIdentified", "Robable",
            "PickingHeight", "UseAnim", "TrapId", "StorageGUID", "Lootable",
            "VelocityVector", "PhysicCategory", "StorageSlotId", "SoundUI",
            "IsTradeItem", "LocalizeLookAtText", "IsSplitting", "InfiniteStack",
            "SpellTargetOffset", "EntityDiscovered",
        };

    public ItemDao() {
        super(Item.class, "_Instance_Item");
    }

    @Override
    protected String[] getFields() {
        return FIELDS;
    }
}
