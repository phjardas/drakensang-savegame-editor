package de.jardas.drakensang.model;

public abstract class InventoryItem extends Persistable {
	public static final Class<?>[] TYPES = { Armor.class, Jewelry.class,
			Key.class, Money.class, Recipe.class, Shield.class, Torch.class,
			Weapon.class, };
	private final boolean countable;
	private Inventory inventory;
	private int count;
	private int maxCount;
	private boolean questItem;
	private int value;
	private String icon;

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

	public boolean isQuestItem() {
		return questItem;
	}

	public void setQuestItem(boolean questItem) {
		this.questItem = questItem;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}
