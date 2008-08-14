/*
 * WeaponsPanel.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.Weapon;


public class WeaponsPanel extends InventoryItemsPanel<Weapon> {
    @Override
    protected int getColumnCount() {
        return 3;
    }

    @Override
    protected Object renderColumn(Weapon item, int col) {
        switch (col) {
        case 3:
            return "Test";

        default:
            return super.renderColumn(item, col);
        }
    }
}
