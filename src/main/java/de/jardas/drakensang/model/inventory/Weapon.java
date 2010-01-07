package de.jardas.drakensang.model.inventory;

import de.jardas.drakensang.shared.model.Schaden;

public class Weapon extends InventoryItem {
	private Type equipmentType;
	private Schaden schaden;
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

	public static enum Type {
		OneHandWeapon, TwoHandWeapon, TwoHandWeaponLeft;
	}
}
