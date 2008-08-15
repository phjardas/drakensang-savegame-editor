package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.Inventory;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class InventoryPanel extends JPanel {
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

        this.inventory = inventory;
        update();
    }
}
