/*
 * ArmorDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.dao.UpdateStatementBuilder;
import de.jardas.drakensang.model.inventory.Armor;
import de.jardas.drakensang.model.inventory.Armor.Type;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ArmorDao extends InventoryItemDao<Armor> {
    public ArmorDao() {
        super(Armor.class, "_Instance_Armor");
    }

    @Override
    public Armor load(ResultSet results) {
        final Armor armor = super.load(results);

        try {
            armor.setType(Type.valueOf(results.getString("EquipmentType")));
            armor.setRuestungKopf(results.getInt("RsKo"));
            armor.setRuestungBrust(results.getInt("RsBr"));
            armor.setRuestungRuecken(results.getInt("RsRu"));
            armor.setRuestungBauch(results.getInt("RsBa"));
            armor.setRuestungArmLinks(results.getInt("RsLA"));
            armor.setRuestungArmRechts(results.getInt("RsRA"));
            armor.setRuestungBeinLinks(results.getInt("RsLB"));
            armor.setRuestungBeinRechts(results.getInt("RsRB"));

            return armor;
        } catch (SQLException e) {
            throw new DrakensangException("Error loading " + armor + ": " + e, e);
        }
    }

    @Override
    protected void appendUpdateStatements(UpdateStatementBuilder builder,
        Armor item) {
        super.appendUpdateStatements(builder, item);

        builder.append("EquipmentType = ?", item.getType().name());
        builder.append("RsKo = ?", item.getRuestungKopf());
        builder.append("RsBr = ?", item.getRuestungBrust());
        builder.append("RsRu = ?", item.getRuestungRuecken());
        builder.append("RsBa = ?", item.getRuestungBauch());
        builder.append("RsLA = ?", item.getRuestungArmLinks());
        builder.append("RsRA = ?", item.getRuestungArmRechts());
        builder.append("RsLB = ?", item.getRuestungBeinLinks());
        builder.append("RsRB = ?", item.getRuestungBeinRechts());
    }
}
