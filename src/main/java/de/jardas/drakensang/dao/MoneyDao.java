/*
 * MoneyDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao;

import de.jardas.drakensang.model.Money;

import java.sql.Connection;


public class MoneyDao extends InventoryItemDao<Money> {
    public MoneyDao(Connection connection) {
        super(connection, Money.class, "_Instance_Money");
    }
}