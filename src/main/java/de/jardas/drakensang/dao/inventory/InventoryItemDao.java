/*
 * InventoryItemDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.dao.UpdateStatementBuilder;
import de.jardas.drakensang.dao.UpdateStatementBuilder.ParameterType;
import de.jardas.drakensang.model.inventory.Armor;
import de.jardas.drakensang.model.inventory.InventoryItem;
import de.jardas.drakensang.model.inventory.Money;
import de.jardas.drakensang.model.inventory.Recipe;
import de.jardas.drakensang.model.inventory.Weapon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class InventoryItemDao<I extends InventoryItem> {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(InventoryItemDao.class);
    private final Class<I> itemClass;
    private final String table;

    public InventoryItemDao(Class<I> itemClass,
        String table) {
        super();
        this.itemClass = itemClass;
        this.table = table;
    }

    public Class<I> getItemClass() {
        return this.itemClass;
    }

    public boolean isApplicable(Class<?extends InventoryItem> clazz) {
        return getItemClass().isAssignableFrom(clazz);
    }

    public I load(ResultSet results) {
        I item;

        try {
            item = getItemClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error creating inventory item of type "
                + getItemClass().getName() + ": " + e, e);
        }

        try {
            item.setGuid(results.getBytes("Guid"));
            item.setId(results.getString("Id"));
            item.setName(results.getString("Name"));
            item.setIcon(results.getString("IconBrush"));

            if (!Money.class.isAssignableFrom(getItemClass())) {
                item.setWeight(results.getInt("Gew"));
                item.setValue(results.getInt("Value"));
                item.setQuestId(results.getString("QuestId"));
                item.setScriptPreset(results.getString("ScriptPreset"));
                item.setScriptOverride(results.getString("ScriptOverride"));
                item.setLimitedScript(results.getString("LimitedScript"));
                item.setCanUse(results.getBoolean("CanUse"));
                item.setUseTalent(results.getString("UseTalent"));
                item.setCanDestroy(results.getBoolean("CanDestroy"));
            }

            if (hasQuestId()) {
                item.setQuestItem((results.getString("QuestId") != null)
                    && !"NONE".equalsIgnoreCase(results.getString("QuestId")));
            }

            if (hasTaBonus()) {
                item.setTaBonus(results.getInt("TaBonus"));
            }

            if (item.isCountable()) {
                item.setCount(results.getInt("StackCount"));
                item.setMaxCount(results.getInt("MaxStackCount"));
            }

            item.setPickingRange(results.getInt("PickingRange"));
            item.setGraphics(results.getString("Graphics"));
            item.setPhysics(results.getString("Physics"));
            item.setLookAtText(results.getString("LookAtText"));
            
            item.setLevel(results.getString("_Level"));
            item.setStorageGuid(results.getBytes("StorageGuid"));

            return item;
        } catch (SQLException e) {
            throw new DrakensangException("Error loading inventory item "
                + getItemClass().getName() + ": " + e);
        }
    }

    protected boolean hasQuestId() {
        return !Recipe.class.isAssignableFrom(getItemClass())
        && !Money.class.isAssignableFrom(getItemClass());
    }

    protected boolean hasTaBonus() {
        return !Armor.class.isAssignableFrom(getItemClass())
        && !Weapon.class.isAssignableFrom(getItemClass())
        && !Money.class.isAssignableFrom(getItemClass());
    }

    public void save(InventoryItem item) throws SQLException {
        UpdateStatementBuilder builder = new UpdateStatementBuilder(getTable(),
                "guid = ?");

        @SuppressWarnings("unchecked")
        final I theItem = (I) item;
        appendUpdateStatements(builder, theItem);

        builder.addParameter(ParameterType.Bytes, item.getGuid());
        LOG.debug("Inventory update: " + builder);

        builder.createStatement(SavegameDao.getConnection()).executeUpdate();
    }

    protected void appendUpdateStatements(UpdateStatementBuilder builder, I item) {
        builder.append("Name = ?", item.getName());
        builder.append("Id = ?", item.getId());

        if (item.isCountable()) {
            builder.append("StackCount = ?", ParameterType.Int, item.getCount());
        }
    }

    public String getTable() {
        return this.table;
    }

    public void create(InventoryItem item) {
        LOG.warn("Creating inventory items is not supported yet: " + item);
    }

    public void delete(InventoryItem item) throws SQLException {
        LOG.debug("Deleting " + item);

        PreparedStatement stmt = SavegameDao.getConnection().prepareStatement("delete from "
                + getTable() + " where Guid = ?");
        stmt.setBytes(1, item.getGuid());
        stmt.executeUpdate();
    }
}
