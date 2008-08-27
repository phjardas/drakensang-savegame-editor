/*
 * WeaponDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.model.Schaden;
import de.jardas.drakensang.model.Weapon;
import de.jardas.drakensang.model.Weapon.Type;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class WeaponDao extends InventoryItemDao<Weapon> {
    public WeaponDao(Connection connection) {
        super(connection, Weapon.class, "_Instance_Weapon");
    }

    @Override
    public Weapon load(ResultSet results) {
        final Weapon weapon = super.load(results);

        try {
            weapon.setEquipmentType(Type.valueOf(results.getString(
                        "EquipmentType")));
            weapon.setSchaden(new Schaden(results.getInt("WpW6"),
                    results.getInt("WpW6plus")));

            return weapon;
        } catch (SQLException e) {
            throw new DrakensangException("Error loading " + weapon + ": " + e,
                e);
        }
    }

    @Override
    protected void appendUpdateStatements(UpdateStatementBuilder builder,
        Weapon item) {
        super.appendUpdateStatements(builder, item);

        builder.append("WpW6 = ?", item.getSchaden().getDiceMultiplier());
        builder.append("WpW6plus = ?", item.getSchaden().getAddition());
    }
}
