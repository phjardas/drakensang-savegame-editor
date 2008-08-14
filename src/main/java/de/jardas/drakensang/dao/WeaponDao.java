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

import de.jardas.drakensang.model.Schaden;
import de.jardas.drakensang.model.Weapon;
import de.jardas.drakensang.model.Weapon.Type;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class WeaponDao extends InventoryItemDao<Weapon> {
    public WeaponDao(Connection connection) {
        super(connection, Weapon.class, "_instance_weapon");
    }

    @Override
    public Weapon load(ResultSet results) throws SQLException {
        final Weapon weapon = super.load(results);
        weapon.setEquipmentType(Type.valueOf(results.getString("EquipmentType")));
        weapon.setSchaden(new Schaden(results.getInt("WpW6"),
                results.getInt("WpW6plus")));

        return weapon;
    }

    @Override
    protected void appendUpdateStatements(UpdateStatementBuilder builder,
        Weapon item) {
        super.appendUpdateStatements(builder, item);

        builder.append("WpW6 = ?", item.getSchaden().getDiceMultiplier());
        builder.append("WpW6plus = ?", item.getSchaden().getAddition());
    }
}
