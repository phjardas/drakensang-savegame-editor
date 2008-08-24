package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.Inventory;
import de.jardas.drakensang.model.InventoryItem;
import de.jardas.drakensang.model.Inventory.InventoryListener;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class InventoryPanel extends JPanel implements InventoryListener {
    private InventoryItemsPanel panel = new InventoryItemsPanel();
    private Inventory inventory;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        add(new JScrollPane(panel), BorderLayout.CENTER);
    }

    private void update() {
        panel.setItems(getInventory().getItems());
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        if (this.inventory == inventory) {
            return;
        }
        
        if (this.inventory != null) {
        	this.inventory.removeInventoryListener(this);
        }

        this.inventory = inventory;
        this.inventory.addInventoryListener(this);
        
        update();
    }

	public void itemAdded(InventoryItem item) {
		System.out.println("Item added: " + item);
		update();
	}

	public void itemRemoved(InventoryItem item) {
		System.out.println("Item removed: " + item);
		repaint();
		update();
	}
}
