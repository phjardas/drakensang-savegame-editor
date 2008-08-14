package de.jardas.drakensang.model;

public class Weapon extends InventoryItem {
    private Type equipmentType;
    private Schaden schaden;

    public Schaden getSchaden() {
        return this.schaden;
    }

    public void setSchaden(Schaden schaden) {
        this.schaden = schaden;
    }

    public Type getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(Type equipmentType) {
        this.equipmentType = equipmentType;
    }
    public static enum Type {OneHandWeapon, TwoHandWeapon;
    }
}
