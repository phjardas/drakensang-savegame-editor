/*
 * Chest.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.inventory.InventoryItem;

import java.util.ArrayList;
import java.util.List;


public class Chest extends Persistable {
	private String lookAtText;
    private List<InventoryItem> items = new ArrayList<InventoryItem>();

    public String getLookAtText() {
		return this.lookAtText;
	}

	public void setLookAtText(String lookAtText) {
		this.lookAtText = lookAtText;
	}
	
	public String getLocalizedName() {
		return Messages.get(getLookAtText());
	}

	public List<InventoryItem> getItems() {
        return this.items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }
}
