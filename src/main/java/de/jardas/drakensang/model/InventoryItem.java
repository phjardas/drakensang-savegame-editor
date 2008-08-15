package de.jardas.drakensang.model;

public abstract class InventoryItem extends Persistable {
	private int count;
	private int maxCount;
	private final boolean countable;

	public InventoryItem() {
		this(true);
	}

	public InventoryItem(boolean countable) {
		super();
		this.countable = countable;

		if (!countable) {
			setCount(1);
			setMaxCount(1);
		}
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMaxCount() {
		return this.maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public boolean isCountable() {
		return countable;
	}
}
