package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.shared.model.inventory.Money;


public class MoneyDao extends InventoryItemDao<Money> {
    private static final String[] FIELDS = {
            "Guid", "_ID", "_Level", "_Layers", "Transform", "Id", "Graphics",
            "Placeholder", "Physics", "Name", "LookAtText", "PickingRange",
            "MaxStackCount", "Value", "StackCount", "IconBrush", "PickingHeight",
            "SoundUI", "StorageGUID", "Lootable", "Robable", "VelocityVector",
            "PhysicCategory", "StorageSlotId", "IsTradeItem",
            "SpellTargetOffset", "IsSplitting", "InfiniteStack",
            "InfoIdentified", "LocalizeLookAtText",
        };

    public MoneyDao() {
        super(Money.class, "_Instance_Money");
    }

    @Override
    protected String[] getFields() {
        return FIELDS;
    }
}
