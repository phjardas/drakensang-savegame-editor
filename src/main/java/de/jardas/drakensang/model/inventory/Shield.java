package de.jardas.drakensang.model.inventory;

public class Shield extends InventoryItem {
    private int attackeMod;
    private int paradeMod;

    public int getAttackeMod() {
        return this.attackeMod;
    }

    public void setAttackeMod(int attackeMod) {
        this.attackeMod = attackeMod;
    }

    public int getParadeMod() {
        return this.paradeMod;
    }

    public void setParadeMod(int paradeMod) {
        this.paradeMod = paradeMod;
    }
}
