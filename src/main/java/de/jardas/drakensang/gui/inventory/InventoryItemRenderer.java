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

    protected List<JComponent> createComponents(final InventoryItem item) {
        List<JComponent> components = new ArrayList<JComponent>();
        components.add(new JLabel(getBundleValue(item.getId())));

        final JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                    item.getCount(), 1, item.getMaxCount(), 1));
        spinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    item.setCount(((Number) spinner.getValue()).intValue());
                }
            });
        components.add(spinner);

        return components;
    }

    protected String getBundleValue(String key) {
        try {
            return BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public boolean isApplicable(InventoryItem item) {
        return true;
    }
}
