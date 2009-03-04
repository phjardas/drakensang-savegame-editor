/*
 * MoneyDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Recipe;


public class RecipeDao extends InventoryItemDao<Recipe> {
    public RecipeDao() {
        super(Recipe.class, "_Instance_Recipe");
    }
}
