/*
 * ShieldRenderer.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.InventoryItem;
import de.jardas.drakensang.model.Shield;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ShieldRenderer extends InventoryItemRenderer {
    @Override
    protected JComponent renderSpecial(final InventoryItem item) {
        final Shield shield = (Shield) item;

        final JSpinner atSpinner = new JSpinner(new SpinnerNumberModel(
                    shield.getAttackeMod(), -10, 10, 1));
        atSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    shield.setAttackeMod(((Number) atSpinner.getValue())
                        .intValue());
                }
            });

        final JSpinner paSpinner = new JSpinner(new SpinnerNumberModel(
                    shield.getParadeMod(), -10, 10, 1));
        paSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    shield.setParadeMod(((Number) paSpinner.getValue()).intValue());
                }
            });

        final JPanel panel = new JPanel();

        panel.add(atSpinner);
        panel.add(new JLabel("/"));
        panel.add(paSpinner);

        return panel;
    }

    @Override
    public boolean isApplicable(InventoryItem item) {
        return item instanceof Shield;
    }
}
