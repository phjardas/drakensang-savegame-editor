package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Torch;


public class TorchDao extends InventoryItemDao<Torch> {
    public TorchDao() {
        super(Torch.class, "_Instance_Torch");
    }
}
