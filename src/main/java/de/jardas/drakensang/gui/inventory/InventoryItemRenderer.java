/*
 * InventoryItemRenderer.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.InventoryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class InventoryItemRenderer {
    private static final ResourceBundle BUNDLE = ResourceBundle
        .getBundle(InventoryItemsPanel.class.getPackage().getName()
            + ".inventory");
    private static final String[] PREFIXES = {
            "crafted_a_", "crafted_s_", "special_", "loc01_", "loc02_",
            "zutat_a_", "robable_", "weapon_unique_",
        };

    protected List<JComponent> createComponents(final InventoryItem item) {
        List<JComponent> components = new ArrayList<JComponent>();
        components.add(renderLabel(item));
        components.add(renderCounter(item));
        components.add(renderSpecial(item));

        return components;
    }

    protected JComponent renderLabel(final InventoryItem item) {
        return new JLabel(getBundleValue(item.getId()));
    }

    protected JComponent renderCounter(final InventoryItem item) {
        if (item.getMaxCount() <= 1) {
            return null;
        }

        final JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                    item.getCount(), 1, item.getMaxCount(), 1));
        spinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    item.setCount(((Number) spinner.getValue()).intValue());
                }
            });

        return spinner;
    }

    protected JComponent renderSpecial(final InventoryItem item) {
        return null;
    }

    protected String getBundleValue(String key) {
        try {
            return BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            for (String prefix : PREFIXES) {
                if (key.startsWith(prefix)) {
                    final String name = key.substring(prefix.length());

                    return name.substring(0, 1).toUpperCase()
                    + name.substring(1);
                }
            }

            return key;
        }
    }

    public boolean isApplicable(InventoryItem item) {
        return true;
    }
}
