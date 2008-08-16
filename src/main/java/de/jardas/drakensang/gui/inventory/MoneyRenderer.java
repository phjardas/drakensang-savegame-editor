package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.InventoryItem;
import de.jardas.drakensang.model.Money;

public class MoneyRenderer extends InventoryItemRenderer {
	@Override
	protected String getInfoKey(InventoryItem item) {
		return null;
	}
	
	@Override
	public boolean isApplicable(InventoryItem item) {
		return item instanceof Money;
	}
}
