package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Key;


public class KeyDao extends InventoryItemDao<Key> {
    public KeyDao() {
        super(Key.class, "_Instance_Key");
    }
}
