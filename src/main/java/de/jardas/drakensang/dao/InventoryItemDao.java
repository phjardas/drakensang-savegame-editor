/*
 * InventoryItemDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao;

import de.jardas.drakensang.dao.UpdateStatementBuilder.ParameterType;
import de.jardas.drakensang.model.InventoryItem;
import de.jardas.drakensang.model.Money;
import de.jardas.drakensang.model.Recipe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryItemDao<I extends InventoryItem> {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(InventoryItemDao.class);
	private final Class<I> itemClass;
	private final String table;
	private final Connection connection;

	public InventoryItemDao(Connection connection, Class<I> itemClass,
			String table) {
		super();
		this.connection = connection;
		this.itemClass = itemClass;
		this.table = table;
	}

	public Class<I> getItemClass() {
		return this.itemClass;
	}

	public boolean isApplicable(Class<? extends InventoryItem> clazz) {
		return getItemClass().isAssignableFrom(clazz);
	}

	public I load(ResultSet results) throws SQLException {
		I item;

		try {
			item = getItemClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error creating inventory item of type "
					+ getItemClass().getName() + ": " + e, e);
		}

		item.setGuid(results.getBytes("Guid"));
		item.setId(results.getString("Id"));
		item.setName(results.getString("Name"));
		item.setIcon(results.getString("IconBrush"));

		if (!Money.class.isAssignableFrom(getItemClass())) {
			if (!Recipe.class.isAssignableFrom(getItemClass())) {
				item.setQuestItem(results.getString("QuestId") != null
						&& !"NONE".equalsIgnoreCase(results
								.getString("QuestId")));
			}
			
			item.setValue(results.getInt("Value"));
		}

		if (item.isCountable()) {
			item.setCount(results.getInt("StackCount"));
			item.setMaxCount(results.getInt("MaxStackCount"));
		}

		return item;
	}

	public void save(InventoryItem item) throws SQLException {
		UpdateStatementBuilder builder = new UpdateStatementBuilder(getTable(),
				"guid = ?");

		@SuppressWarnings("unchecked")
		final I theItem = (I) item;
		appendUpdateStatements(builder, theItem);

		builder.addParameter(ParameterType.Bytes, item.getGuid());
		LOG.debug("Inventory update: " + builder);

		builder.createStatement(connection).executeUpdate();
	}

	protected void appendUpdateStatements(UpdateStatementBuilder builder, I item) {
		builder.append("Name = ?", item.getName());
		builder.append("Id = ?", item.getId());

		if (item.isCountable()) {
			builder
					.append("StackCount = ?", ParameterType.Int, item
							.getCount());
		}
	}

	public String getTable() {
		return this.table;
	}
}
