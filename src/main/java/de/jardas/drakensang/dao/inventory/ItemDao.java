package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Item;


public class ItemDao extends InventoryItemDao<Item> {
    public ItemDao() {
        super(Item.class, "_Instance_Item");
    }
}
