/*
 * NewItemDialog.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.Main;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.dao.inventory.InventoryDao;
import de.jardas.drakensang.model.inventory.InventoryItem;
import de.jardas.drakensang.model.inventory.Item;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;


public abstract class NewItemDialog extends JDialog {
    public NewItemDialog() {
        super(Main.getFrame(), "Neuer Gegenstand", true);

        setLayout(new GridBagLayout());

        final TemplateModel model = new TemplateModel();
        add(new JComboBox(model),
            new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));

        add(new JButton(new AbstractAction("OK") {
                public void actionPerformed(ActionEvent e) {
                    itemAdded(model.getSelectedInventoryItem());
                    setVisible(false);
                }
            }),
            new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));

        pack();
        setLocationRelativeTo(Main.getFrame());
        setVisible(true);
    }

    protected abstract void itemAdded(Item item);

    private class TemplateModel extends DefaultComboBoxModel {
        public TemplateModel() {
            List<Item> items = new ArrayList<Item>();
            Set<String> names = new HashSet<String>();

            for (Iterator<InventoryItem> it = InventoryDao.loadInventory(
                        Item.class).iterator(); it.hasNext();) {
                Item item = (Item) it.next();
                String name = getName(item);

                if (names.contains(name)) {
                    continue;
                }

                items.add(item);
                names.add(name);
            }

            Collections.sort(items,
                new Comparator<Item>() {
                    private final Collator collator = Collator.getInstance();

                    public int compare(Item o1, Item o2) {
                        return collator.compare(getName(o1), getName(o2));
                    }
                });

            for (Item item : items) {
                addElement(new ModelItem((Item) item));
            }
        }

        public Item getSelectedInventoryItem() {
            return ((ModelItem) getSelectedItem()).getItem();
        }

        private String getName(Item item) {
            return Messages.get(InventoryItemRenderer.getRenderer(item)
                                                     .getNameKey(item));
        }

        private class ModelItem {
            private final Item item;

            public ModelItem(Item item) {
                super();
                this.item = item;
            }

            @Override
            public String toString() {
                return getName(getItem());
            }

            public Item getItem() {
                return item;
            }
        }
    }
}
