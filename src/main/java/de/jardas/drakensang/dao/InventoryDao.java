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

import java.util.HashSet;
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
				"_instance_money"));
		itemDaos.add(new InventoryItemDao<Item>(connection, Item.class,
				"_instance_item"));
		itemDaos.add(new InventoryItemDao<Jewelry>(connection, Jewelry.class,
				"_instance_jewelry"));
		itemDaos.add(new InventoryItemDao<Key>(connection, Key.class,
				"_instance_key"));
		itemDaos.add(new InventoryItemDao<Recipe>(connection, Recipe.class,
				"_instance_recipe"));
		itemDaos.add(new InventoryItemDao<Torch>(connection, Torch.class,
				"_instance_torch"));
	}

	public void loadInventory(Character character) throws SQLException {
		for (InventoryItemDao<? extends InventoryItem> dao : itemDaos) {
			loadInventory(character, dao.getItemClass(), dao.getTable());
		}
	}

	private void loadInventory(Character character,
			Class<? extends InventoryItem> itemClass, String table)
			throws SQLException {
		final String sql = "select * from " + table + " where StorageGuid = ?";
		LOG.debug("Loading inventory for character " + character.getId() + ": "
				+ sql);

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setBytes(1, character.getGuid());

		ResultSet results = stmt.executeQuery();

		while (results.next()) {
			try {
				final InventoryItem item = getInventoryItemDao(itemClass).load(
						results);
				character.getInventory().add(item);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
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
