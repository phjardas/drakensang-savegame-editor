package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.InventoryItem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class InventoryItemsPanel extends JPanel {
    private final List<InventoryItemRenderer> renderers = new ArrayList<InventoryItemRenderer>();
    private List<InventoryItem> items;

    public InventoryItemsPanel() {
        renderers.add(new WeaponRenderer());
        renderers.add(new InventoryItemRenderer());

        setLayout(new GridBagLayout());
    }

    private void update() {
        removeAll();

        int row = 0;

        for (InventoryItem item : items) {
            Insets insets = new Insets(3, 6, 3, 6);
            int col = 0;

            for (JComponent comp : getRenderer(item).createComponents(item)) {
                add(comp,
                    new GridBagConstraints(col++, row, 1, 1, 0, 0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        insets, 0, 0));
            }

            row++;
        }

        repaint();
    }

    private InventoryItemRenderer getRenderer(InventoryItem item) {
        for (InventoryItemRenderer renderer : renderers) {
            if (renderer.isApplicable(item)) {
                return renderer;
            }
        }

        throw new IllegalArgumentException("Can't render " + item);
    }

    public Set<InventoryItem> getItems() {
        return new HashSet<InventoryItem>(items);
    }

    public void setItems(Set<InventoryItem> items) {
        this.items = new ArrayList<InventoryItem>(items);
        Collections.sort(this.items,
            new Comparator<InventoryItem>() {
                public int compare(InventoryItem o1, InventoryItem o2) {
                    return getRenderer(o1).getBundleValue(o1.getId())
                               .compareToIgnoreCase(getRenderer(o2)
                                                        .getBundleValue(o2.getId()));
                }
            });
        update();
    }
}
