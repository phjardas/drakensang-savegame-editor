package de.jardas.drakensang.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Inventory {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(Inventory.class);
	private final Character character;
	private final Set<InventoryItem> items = new HashSet<InventoryItem>();
	private final Set<InventoryItem> added = new HashSet<InventoryItem>();
	private final Set<InventoryItem> deleted = new HashSet<InventoryItem>();
	private final List<InventoryListener> listeners = new ArrayList<InventoryListener>();

	public Inventory(final Character character) {
		super();
		this.character = character;
	}

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
		LOG.debug("Adding " + item);
		item.setInventory(this);
		items.add(item);
		added.add(item);
		deleted.remove(item);
		
		for (InventoryListener listener : this.listeners) {
			listener.itemRemoved(item);
		}
	}

	public void remove(InventoryItem item) {
		LOG.debug("Removing " + item);
		item.setInventory(null);
		items.remove(item);
		added.remove(item);
		deleted.add(item);
		
		for (InventoryListener listener : this.listeners) {
			listener.itemRemoved(item);
		}
	}
	
	public void addInventoryListener(InventoryListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeInventoryListener(InventoryListener listener) {
		this.listeners.remove(listener);
	}

	public Set<InventoryItem> getAddedItems() {
		return this.added;
	}

	public Set<InventoryItem> getDeletedItems() {
		return this.deleted;
	}

	@Override
	public String toString() {
		return items.toString();
	}
	
	public static interface InventoryListener {
		void itemAdded(InventoryItem item);
		void itemRemoved(InventoryItem item);
	}

	public Character getCharacter() {
		return this.character;
	}
}
