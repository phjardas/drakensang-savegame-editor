package de.jardas.drakensang.gui.inventory;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.jardas.drakensang.model.inventory.InventoryItem;
import de.jardas.drakensang.model.inventory.Money;
import de.jardas.drakensang.shared.db.Messages;


public class InventoryItemsPanel extends JPanel {
    private List<InventoryItem> items;

    public InventoryItemsPanel() {
        setLayout(new GridBagLayout());
    }

    private void update() {
        removeAll();

        Insets insets = new Insets(3, 6, 3, 6);
        int row = 0;
        int panelCount = 0;
        JPanel panel = null;
        Class<?extends InventoryItem> currentClass = null;

        for (InventoryItem item : items) {
        	if (item instanceof Money) {
        		continue;
        	}
        	
            if ((panel == null) || (currentClass != item.getClass())) {
                panel = new JPanel();
                panel.setLayout(new GridBagLayout());
                panel.setBorder(BorderFactory.createTitledBorder(getGroupTitle(
                            item.getClass())));
                add(panel,
                    new GridBagConstraints(0, panelCount++, 1, 1, 1, 0,
                        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                        insets, 0, 0));
                row = 0;
                currentClass = item.getClass();
            }

            int col = 0;

            for (JComponent comp : getRenderer(item).createComponents(item)) {
                if (comp != null) {
                    panel.add(comp,
                        new GridBagConstraints(col, row, 1, 1, 0, 0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            insets, 0, 0));
                }

                col++;
            }

            row++;

            panel.add(new JLabel(),
                new GridBagConstraints(3, row, 1, 1, 1, 0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
        }

        add(new JLabel(),
            new GridBagConstraints(0, panelCount, 1, 1, 0, 1,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 0), 0, 0));

        repaint();
    }

    private String getGroupTitle(Class<?extends InventoryItem> currentClass) {
        return Messages.get("inventorygroup." + currentClass.getSimpleName());
    }

    private <I extends InventoryItem> InventoryItemRenderer<I> getRenderer(
        I item) {
        return InventoryItemRenderer.getRenderer(item);
    }

    public Set<InventoryItem> getItems() {
        return new HashSet<InventoryItem>(items);
    }

    public void setItems(Set<InventoryItem> items) {
        this.items = new ArrayList<InventoryItem>(items);

        Collections.sort(this.items,
            new Comparator<InventoryItem>() {
                private final Collator collator = Collator.getInstance();

                public int compare(InventoryItem o1, InventoryItem o2) {
                    int classCompare = collator.compare(getGroupTitle(
                                o1.getClass()), getGroupTitle(o2.getClass()));

                    if (classCompare != 0) {
                        return classCompare;
                    }

                    return collator.compare(getRenderer(o1)
                                                .getItemName(o1.getId()),
                        getRenderer(o2).getItemName(o2.getId()));
                }
            });

        update();
    }
}
