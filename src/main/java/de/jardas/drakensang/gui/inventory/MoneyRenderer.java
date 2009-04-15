package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.inventory.InventoryItem;
import de.jardas.drakensang.model.inventory.Money;

import javax.swing.JComponent;


public class MoneyRenderer extends InventoryItemRenderer<Money> {
    @Override
    public String getInfoKey(Money item) {
        return null;
    }

    @Override
    protected JComponent renderActions(Money item) {
        return null;
    }

    @Override
    public boolean isApplicable(InventoryItem item) {
        return item instanceof Money;
    }
}
