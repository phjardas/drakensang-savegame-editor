package de.jardas.drakensang.model;

import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Character {
	private byte[] guid;
	private String id;
	private String name;
	private int abenteuerpunkte;
	private int steigerungspunkte;
	private final Attribute attribute = new Attribute();
	private final Talente talente = new Talente();
	private final Sonderfertigkeiten sonderfertigkeiten = new Sonderfertigkeiten();
	private final Zauberfertigkeiten zauberfertigkeiten = new Zauberfertigkeiten();
	private final Inventory inventory = new Inventory();

	public byte[] getGuid() {
		return guid;
	}

	public void setGuid(byte[] guid) {
		this.guid = guid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPlayerCharacter() {
		return "CharWizardPC".equals(getId());
	}

	public int getMoneyAmount() {
		Set<Money> money = getInventory().getItems(Money.class);
		return money.isEmpty() ? 0 : money.iterator().next().getAmount();
	}

	public void setMoneyAmount(int amount) {
		Set<Money> money = getInventory().getItems(Money.class);

		if (money.size() != 1) {
			throw new IllegalArgumentException("The character " + getName()
					+ " can not carry money.");
		}

		money.iterator().next().setAmount(amount);
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public Talente getTalente() {
		return talente;
	}

	public Sonderfertigkeiten getSonderfertigkeiten() {
		return sonderfertigkeiten;
	}

	public Zauberfertigkeiten getZauberfertigkeiten() {
		return zauberfertigkeiten;
	}

	public int getAbenteuerpunkte() {
		return abenteuerpunkte;
	}

	public void setAbenteuerpunkte(int abenteuerpunkte) {
		this.abenteuerpunkte = abenteuerpunkte;
	}

	public int getSteigerungspunkte() {
		return steigerungspunkte;
	}

	public void setSteigerungspunkte(int steigerungspunkte) {
		this.steigerungspunkte = steigerungspunkte;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
