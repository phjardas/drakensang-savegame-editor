/*
 * InventoryDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao;

import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.Inventory;
import de.jardas.drakensang.model.InventoryItem;
import de.jardas.drakensang.model.Item;
import de.jardas.drakensang.model.Jewelry;
import de.jardas.drakensang.model.Key;
import de.jardas.drakensang.model.Money;
import de.jardas.drakensang.model.Recipe;
import de.jardas.drakensang.model.Torch;

import java.sql.Connection;
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
	private final Set<InventoryItemDao<? extends InventoryItem>> itemDaos = new HashSet<InventoryItemDao<? extends InventoryItem>>();
	private final Connection connection;

	public InventoryDao(final Connection connection) {
		super();
		this.connection = connection;

		itemDaos.add(new WeaponDao(connection));
		itemDaos.add(new ShieldDao(connection));
		itemDaos.add(new ArmorDao(connection));
		itemDaos.add(new InventoryItemDao<Money>(connection, Money.class,
				"_Instance_Money"));
		itemDaos.add(new InventoryItemDao<Item>(connection, Item.class,
				"_Instance_Item"));
		itemDaos.add(new InventoryItemDao<Jewelry>(connection, Jewelry.class,
				"_Instance_Jewelry"));
		itemDaos.add(new InventoryItemDao<Key>(connection, Key.class,
				"_Instance_Key"));
		itemDaos.add(new InventoryItemDao<Recipe>(connection, Recipe.class,
				"_Instance_Recipe"));
		itemDaos.add(new InventoryItemDao<Torch>(connection, Torch.class,
				"_Instance_Torch"));
	}

	public void loadInventory(Character character) throws SQLException {
		for (InventoryItemDao<? extends InventoryItem> dao : itemDaos) {
			loadInventory(character, dao.getItemClass());
		}
	}

	private void loadInventory(Character character,
			Class<? extends InventoryItem> itemClass) throws SQLException {
		InventoryItemDao<? extends InventoryItem> dao = getInventoryItemDao(itemClass);
		final String sql = "select * from " + dao.getTable()
				+ " where StorageGuid = ?";
		LOG.debug("Loading inventory for character " + character.getId() + ": "
				+ sql);

		PreparedStatement stmt = connection.prepareStatement(sql);
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

	public List<InventoryItem> loadInventory(
			Class<? extends InventoryItem> itemClass) {
		List<InventoryItem> items = new ArrayList<InventoryItem>();
		InventoryItemDao<? extends InventoryItem> dao = getInventoryItemDao(itemClass);
		final String sql = "select * from " + dao.getTable();

		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

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
		
		LOG.debug("Loaded " + items.size() + " inventory items of type " + itemClass.getSimpleName() + ".");

		return items;
	}

	private <I extends InventoryItem> InventoryItemDao<I> getInventoryItemDao(
			Class<I> itemClass) {
		for (InventoryItemDao<? extends InventoryItem> dao : itemDaos) {
			if (dao.isApplicable(itemClass)) {
				@SuppressWarnings("unchecked")
				final InventoryItemDao<I> theDao = (InventoryItemDao<I>) dao;

				return theDao;
			}
		}

		throw new IllegalArgumentException("No DAO known for "
				+ itemClass.getName() + ".");
	}

	public void save(Inventory inventory) throws SQLException {
		for (InventoryItem item : inventory.getItems()) {
			getInventoryItemDao(item.getClass()).save(item);
		}
	}
}
