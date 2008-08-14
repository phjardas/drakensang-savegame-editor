package de.jardas.drakensang.model;


public class Weapon extends InventoryItem {
	private Type equipmentType; 
	
	public Type getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(Type equipmentType) {
		this.equipmentType = equipmentType;
	}
	
	public static enum Type {
		OneHandWeapon,
		TwoHandWeapon,
	}
}
