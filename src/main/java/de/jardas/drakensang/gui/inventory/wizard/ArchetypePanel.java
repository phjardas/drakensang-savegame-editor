package de.jardas.drakensang.gui.inventory.wizard;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.dao.inventory.InventoryDao;
import de.jardas.drakensang.gui.inventory.InventoryItemRenderer;
import de.jardas.drakensang.model.inventory.InventoryItem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.text.Collator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;


class ArchetypePanel extends NewItemWizardPanel {
    private final JComboBox templateBox = new JComboBox();

    ArchetypePanel() {
        super("wizard.item.archetype.title");

        getContentPanel().setLayout(new GridBagLayout());

        Insets insets = new Insets(3, 6, 3, 6);
        int row = 0;

        getContentPanel()
            .add(new JLabel(Messages.get("wizard.item.archetype.label")),
            new GridBagConstraints(0, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        getContentPanel()
            .add(templateBox,
            new GridBagConstraints(0, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));
    }

    void setItemGroup(Class<?extends InventoryItem> itemGroup) {
        templateBox.setModel(new TemplateModel(itemGroup));
    }

    public InventoryItem createItem() {
        final InventoryItem item = ((TemplateModel) templateBox.getModel())
            .getSelectedInventoryItem();

        return item;
    }

    public class TemplateModel extends DefaultComboBoxModel {
        private final List<?extends InventoryItem> items;

        public TemplateModel(Class<?extends InventoryItem> itemClass) {
            items = InventoryDao.loadInventory(itemClass);
            Collections.sort(items,
                new Comparator<InventoryItem>() {
                    private final Collator collator = Collator.getInstance();

                    public int compare(InventoryItem o1, InventoryItem o2) {
                        return collator.compare(getName(o1), getName(o2));
                    }
                });

            for (InventoryItem item : items) {
                addElement(new Item(item));
            }
        }

        public InventoryItem getSelectedInventoryItem() {
            return ((Item) getSelectedItem()).getItem();
        }

        private <I extends InventoryItem> String getName(I item) {
            final InventoryItemRenderer<I> renderer = InventoryItemRenderer
                .getRenderer(item);
            final StringBuffer name = new StringBuffer();
            name.append(Messages.get(renderer.getNameKey(item)));

            final String extra = renderer.renderInlineInfo(item);

            if (extra != null) {
                name.append(" (").append(extra).append(")");
            }

            return name.toString();
        }

        private class Item {
            private final InventoryItem item;

            public Item(InventoryItem item) {
                super();
                this.item = item;
            }

            @Override
            public String toString() {
                return getName(getItem());
            }

            public InventoryItem getItem() {
                return item;
            }
        }
    }
}
