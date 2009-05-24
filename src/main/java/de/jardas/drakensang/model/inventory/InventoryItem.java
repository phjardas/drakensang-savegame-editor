package de.jardas.drakensang.model.inventory;

import de.jardas.drakensang.model.Persistable;

public abstract class InventoryItem extends Persistable {
    private final boolean countable;
    private Inventory inventory;
    private int count;
    private int maxCount;
    private boolean questItem;
    private int value;
    private String icon;
    private int weight;
    private int pickingRange;
    private String graphics;
    private String physics;
    private String lookAtText;
    private String questId;
    private String scriptPreset;
    private String scriptOverride;
    private String limitedScript;
    private boolean canUse;
    private int taBonus;
    private String useTalent;
    private boolean canDestroy;
    private EquipmentSlot slot;
    private String level;
    private byte[] storageGuid;

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

    public EquipmentSlot getSlot() {
		return slot;
	}

	public void setSlot(EquipmentSlot slot) {
		this.slot = slot;
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

    public boolean isCanDestroy() {
        return this.canDestroy;
    }

    public void setCanDestroy(boolean canDestroy) {
        this.canDestroy = canDestroy;
    }

    public boolean isCanUse() {
        return this.canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public String getGraphics() {
        return this.graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public String getLimitedScript() {
        return this.limitedScript;
    }

    public void setLimitedScript(String limitedScript) {
        this.limitedScript = limitedScript;
    }

    public String getLookAtText() {
        return this.lookAtText;
    }

    public void setLookAtText(String lookAtText) {
        this.lookAtText = lookAtText;
    }

    public String getPhysics() {
        return this.physics;
    }

    public void setPhysics(String physics) {
        this.physics = physics;
    }

    public int getPickingRange() {
        return this.pickingRange;
    }

    public void setPickingRange(int pickingRange) {
        this.pickingRange = pickingRange;
    }

    public String getQuestId() {
        return this.questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getScriptOverride() {
        return this.scriptOverride;
    }

    public void setScriptOverride(String scriptOverride) {
        this.scriptOverride = scriptOverride;
    }

    public String getScriptPreset() {
        return this.scriptPreset;
    }

    public void setScriptPreset(String scriptPreset) {
        this.scriptPreset = scriptPreset;
    }

    public int getTaBonus() {
        return this.taBonus;
    }

    public void setTaBonus(int taBonus) {
        this.taBonus = taBonus;
    }

    public String getUseTalent() {
        return this.useTalent;
    }

    public void setUseTalent(String useTalent) {
        this.useTalent = useTalent;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public byte[] getStorageGuid() {
		return this.storageGuid;
	}

	public void setStorageGuid(byte[] storageGuid) {
		this.storageGuid = storageGuid;
	}
}
