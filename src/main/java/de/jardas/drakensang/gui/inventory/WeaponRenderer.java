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

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;


public class WeaponRenderer extends InventoryItemRenderer {
    @Override
    protected List<JComponent> createComponents(InventoryItem item) {
        final List<JComponent> components = super.createComponents(item);
        Weapon weapon = (Weapon) item;
        components.add(new JLabel(weapon.getSchaden().toString()));

        return components;
    }
    
    @Override
    public boolean isApplicable(InventoryItem item) {
    	return item instanceof Weapon;
    }
}
