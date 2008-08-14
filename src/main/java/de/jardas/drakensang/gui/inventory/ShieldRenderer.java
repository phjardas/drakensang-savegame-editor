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

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;


public class ShieldRenderer extends InventoryItemRenderer {
    @Override
    protected List<JComponent> createComponents(InventoryItem item) {
        final List<JComponent> components = super.createComponents(item);
        Shield shield = (Shield) item;
        components.add(new JLabel(shield.getAttackeMod() + "/"
                + shield.getParadeMod()));

        return components;
    }

    @Override
    public boolean isApplicable(InventoryItem item) {
        return item instanceof Shield;
    }
}
