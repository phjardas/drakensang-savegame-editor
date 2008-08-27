/*
 * ShieldDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.model.Shield;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ShieldDao extends InventoryItemDao<Shield> {
    public ShieldDao(Connection connection) {
        super(connection, Shield.class, "_Instance_Shield");
    }

    @Override
    public Shield load(ResultSet results) {
        final Shield shield = super.load(results);

        try {
            shield.setAttackeMod(results.getInt("WpATmod"));
            shield.setParadeMod(results.getInt("WpPAmod"));

            return shield;
        } catch (SQLException e) {
            throw new DrakensangException("Error loading " + shield + ": " + e,
                e);
        }
    }

    @Override
    protected void appendUpdateStatements(UpdateStatementBuilder builder,
        Shield item) {
        super.appendUpdateStatements(builder, item);

        builder.append("WpATmod = ?", item.getAttackeMod());
        builder.append("WpPAmod = ?", item.getParadeMod());
    }
}
