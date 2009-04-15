package de.jardas.drakensang.gui.inventory.wizard;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.dao.inventory.InventoryDao;
import de.jardas.drakensang.model.inventory.InventoryItem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.text.Collator;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;


class GroupPanel extends NewItemWizardPanel {
    private final JComboBox inventoryType;
    private final Vector<Class<?extends InventoryItem>> types;

    GroupPanel() {
        super("wizard.item.group.title");

        types = new Vector<Class<?extends InventoryItem>>(InventoryDao
                .getInventoryItemTypes());
        Collections.sort(types,
            new Comparator<Class<?extends InventoryItem>>() {
                private final Collator collator = Collator
                    .getInstance();

                public int compare(Class<?extends InventoryItem> o1,
                    Class<?extends InventoryItem> o2) {
                    return collator.compare(getArchetypeName(o1),
                        getArchetypeName(o2));
                }
            });

        inventoryType = new JComboBox(new DefaultComboBoxModel(types) {
                    @Override
                    public Object getElementAt(int index) {
                        return getArchetypeName(types.get(index));
                    }
                });

        getContentPanel().setLayout(new GridBagLayout());

        Insets insets = new Insets(3, 6, 3, 6);
        int row = 0;

        getContentPanel()
            .add(new JLabel(Messages.get("wizard.item.group.label")),
            new GridBagConstraints(0, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        getContentPanel()
            .add(inventoryType,
            new GridBagConstraints(0, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));
    }

    private String getArchetypeName(final Class<?extends InventoryItem> type) {
        return Messages.get("inventorygroup." + type.getSimpleName());
    }

    public Class<?extends InventoryItem> getItemGroup() {
        return types.get(inventoryType.getSelectedIndex());
    }
}
