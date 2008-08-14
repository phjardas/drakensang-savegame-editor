package de.jardas.drakensang.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.jardas.drakensang.gui.inventory.InventoryItemsPanel;
import de.jardas.drakensang.model.Inventory;
import de.jardas.drakensang.model.Item;
import de.jardas.drakensang.model.Weapon;

public class InventoryPanel extends JPanel {
	private final JTabbedPane tabs = new JTabbedPane();
	private final InventoryItemsPanel<Weapon> weapons = new InventoryItemsPanel<Weapon>();
	private final InventoryItemsPanel<Item> items = new InventoryItemsPanel<Item>();
	private Inventory inventory;

	public InventoryPanel() {
		tabs.addTab("Waffen", weapons);
		tabs.addTab("Gegenstände", items);

		setLayout(new BorderLayout());
		add(tabs, BorderLayout.CENTER);
	}

	private void update() {
		weapons.setItems(inventory.getItems(Weapon.class));
		items.setItems(inventory.getItems(Item.class));
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		if (this.inventory == inventory) {
			return;
		}

		this.inventory = inventory;
		update();
	}
}
