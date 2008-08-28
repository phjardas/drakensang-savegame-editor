/*
 * InventoryDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.inventory.Inventory;
import de.jardas.drakensang.model.inventory.InventoryItem;
import de.jardas.drakensang.model.inventory.Item;
import de.jardas.drakensang.model.inventory.Jewelry;
import de.jardas.drakensang.model.inventory.Key;
import de.jardas.drakensang.model.inventory.Recipe;
import de.jardas.drakensang.model.inventory.Torch;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class InventoryDao {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(InventoryDao.class);
    private static final Set<InventoryItemDao<?extends InventoryItem>> ITEM_DAOS =
        new HashSet<InventoryItemDao<?extends InventoryItem>>();

    static {
        ITEM_DAOS.add(new WeaponDao());
        ITEM_DAOS.add(new ShieldDao());
        ITEM_DAOS.add(new ArmorDao());
        ITEM_DAOS.add(new MoneyDao());
        ITEM_DAOS.add(new InventoryItemDao<Item>(Item.class, "_Instance_Item"));
        ITEM_DAOS.add(new InventoryItemDao<Jewelry>(Jewelry.class,
                "_Instance_Jewelry"));
        ITEM_DAOS.add(new InventoryItemDao<Key>(Key.class, "_Instance_Key"));
        ITEM_DAOS.add(new InventoryItemDao<Recipe>(Recipe.class,
                "_Instance_Recipe"));
        ITEM_DAOS.add(new InventoryItemDao<Torch>(Torch.class, "_Instance_Torch"));
    }

    public static void loadInventory(Character character)
        throws SQLException {
        for (InventoryItemDao<?extends InventoryItem> dao : ITEM_DAOS) {
            loadInventory(character, dao.getItemClass());
        }
    }

    private static void loadInventory(Character character,
        Class<?extends InventoryItem> itemClass) throws SQLException {
        InventoryItemDao<?extends InventoryItem> dao = getInventoryItemDao(itemClass);
        final String sql = "select * from " + dao.getTable()
            + " where StorageGuid = ?";
        LOG.debug("Loading inventory for character " + character.getId() + ": "
            + sql);

        PreparedStatement stmt = SavegameDao.getConnection()
                                            .prepareStatement(sql);
        stmt.setBytes(1, character.getGuid());

        ResultSet results = stmt.executeQuery();
        List<InventoryItem> items = new ArrayList<InventoryItem>();

        while (results.next()) {
            try {
                final InventoryItem item = dao.load(results);
                items.add(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for (InventoryItem item : items) {
            character.getInventory().getItems().add(item);
            item.setInventory(character.getInventory());
        }
    }

    public static List<InventoryItem> loadInventory(
        Class<?extends InventoryItem> itemClass) {
        List<InventoryItem> items = new ArrayList<InventoryItem>();
        InventoryItemDao<?extends InventoryItem> dao = getInventoryItemDao(itemClass);
        final String sql = "select * from " + dao.getTable();

        try {
            PreparedStatement stmt = SavegameDao.getConnection()
                                                .prepareStatement(sql);

            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                try {
                    final InventoryItem item = dao.load(results);
                    items.add(item);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading inventory items: " + e, e);
        }

        LOG.debug("Loaded " + items.size() + " inventory items of type "
            + itemClass.getSimpleName() + ".");

        return items;
    }

    private static <I extends InventoryItem> InventoryItemDao<I> getInventoryItemDao(
        Class<I> itemClass) {
        for (InventoryItemDao<?extends InventoryItem> dao : ITEM_DAOS) {
            if (dao.isApplicable(itemClass)) {
                @SuppressWarnings("unchecked")
                final InventoryItemDao<I> theDao = (InventoryItemDao<I>) dao;

                return theDao;
            }
        }

        throw new IllegalArgumentException("No DAO known for "
            + itemClass.getName() + ".");
    }

    public static void save(Inventory inventory) throws SQLException {
        for (InventoryItem item : inventory.getItems()) {
            getInventoryItemDao(item.getClass()).save(item);
        }

        for (InventoryItem item : inventory.getAddedItems()) {
            getInventoryItemDao(item.getClass()).create(item);
        }

        for (InventoryItem item : inventory.getDeletedItems()) {
            getInventoryItemDao(item.getClass()).delete(item);
        }
    }
}
