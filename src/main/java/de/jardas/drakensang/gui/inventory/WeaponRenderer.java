/*
 * WeaponRenderer.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.InventoryItem;
import de.jardas.drakensang.model.Weapon;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class WeaponRenderer extends InventoryItemRenderer {
    @Override
    protected JComponent renderSpecial(InventoryItem item) {
        final Weapon weapon = (Weapon) item;
        final JLabel schadenLabel = new JLabel(getSchadenText(weapon));

        final JSpinner diceSpinner = new JSpinner(new SpinnerNumberModel(
                    weapon.getSchaden().getDiceMultiplier(), 0, 10, 1));
        diceSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    weapon.getSchaden()
                          .setDiceMultiplier(((Number) diceSpinner.getValue())
                        .intValue());
                    schadenLabel.setText(getSchadenText(weapon));
                }
            });

        final JSpinner addSpinner = new JSpinner(new SpinnerNumberModel(
                    weapon.getSchaden().getAddition(), 0, 100, 1));
        addSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    weapon.getSchaden()
                          .setAddition(((Number) addSpinner.getValue()).intValue());
                    schadenLabel.setText(getSchadenText(weapon));
                }
            });

        final JPanel panel = new JPanel();

        panel.add(diceSpinner);
        panel.add(new JLabel("W6+"));
        panel.add(addSpinner);
        panel.add(schadenLabel);

        return panel;
    }

    private String getSchadenText(Weapon weapon) {
        return " (" + weapon.getSchaden().getMinimum() + "-"
        + weapon.getSchaden().getMaximum() + " TP)";
    }

    @Override
    public boolean isApplicable(InventoryItem item) {
        return item instanceof Weapon;
    }
}
