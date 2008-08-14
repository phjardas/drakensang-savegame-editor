package de.jardas.drakensang.model;

import java.util.HashSet;
import java.util.Set;

public class Inventory {
	private final Set<InventoryItem> items = new HashSet<InventoryItem>();

	public Set<InventoryItem> getItems() {
		return items;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends InventoryItem> Set<T> getItems(Class<T> type) {
		Set<T> filtered = new HashSet<T>();
		
		for (InventoryItem item : getItems()) {
			if (type.isAssignableFrom(item.getClass())) {
				filtered.add((T) item);
			}
		}
		
		return filtered;
	}
	
	public void add(InventoryItem item) {
		items.add(item);
	}
	
	@Override
	public String toString() {
		return items.toString();
	}
}
