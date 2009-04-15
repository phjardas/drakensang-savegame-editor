package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Jewelry;


public class JewelryDao extends InventoryItemDao<Jewelry> {
    public JewelryDao() {
        super(Jewelry.class, "_Instance_Jewelry");
    }
}
