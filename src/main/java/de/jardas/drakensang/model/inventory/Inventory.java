package de.jardas.drakensang.model.inventory;

import java.util.HashSet;
import java.util.Set;

import de.jardas.drakensang.model.Character;

public class Inventory {
	private final Character character;
	private final Set<InventoryItem> items = new HashSet<InventoryItem>();

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

	@Override
	public String toString() {
		return items.toString();
	}

	public Character getCharacter() {
		return this.character;
	}
}
