package de.jardas.drakensang.gui.inventory;

import javax.swing.JComponent;

import de.jardas.drakensang.model.InventoryItem;
import de.jardas.drakensang.model.Money;

public class MoneyRenderer extends InventoryItemRenderer {
	@Override
	public String getInfoKey(InventoryItem item) {
		return null;
	}
	
	@Override
	protected JComponent renderActions(InventoryItem item) {
		return null;
	}
	
	@Override
	public boolean isApplicable(InventoryItem item) {
		return item instanceof Money;
	}
}
