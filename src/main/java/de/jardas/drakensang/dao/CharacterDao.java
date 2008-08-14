package de.jardas.drakensang.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.IntegerMap;
import de.jardas.drakensang.model.InventoryItem;
import de.jardas.drakensang.model.Item;
import de.jardas.drakensang.model.Money;
import de.jardas.drakensang.model.Weapon;
import de.jardas.drakensang.model.Weapon.Type;

public class CharacterDao {
	private Set<Character> characters;
	private final Connection connection;

	public CharacterDao(String filename) {
		try {
			System.out.println("Opening savegame at " + filename);
			connection = DriverManager
					.getConnection("jdbc:sqlite:/" + filename);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Can't open database file '"
					+ filename + "': " + e, e);
		}
	}

	public synchronized Set<Character> getCharacters() {
		if (this.characters == null) {
			try {
				this.characters = loadCharacters();
			} catch (SQLException e) {
				throw new RuntimeException("Error loading characters: " + e, e);
			}
		}

		return this.characters;
	}

	private Set<Character> loadCharacters() throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("select * from _instance_pc order by name");
		ResultSet result = statement.executeQuery();
		Set<Character> items = new HashSet<Character>();

		while (result.next()) {
			Character c = new Character();
			items.add(c);

			c.setGuid(result.getBytes("Guid"));
			c.setName(result.getString("Name"));
			c.setId(result.getString("Id"));
			c.setAbenteuerpunkte(result.getInt("XP"));
			c.setSteigerungspunkte(result.getInt("UpgradeXP"));

			c.getAttribute().load(result);
			c.getTalente().load(result);
			c.getSonderfertigkeiten().load(result);
			c.getZauberfertigkeiten().load(result);

			loadInventory(c);
			System.out.println("Loaded " + c);
		}

		return items;
	}

	private void loadInventory(Character character) throws SQLException {
		loadInventory(character, Item.class, "_instance_item");
		loadInventory(character, Money.class, "_instance_money");
		loadInventory(character, Weapon.class, "_instance_weapon");
	}

	private void loadInventory(Character character,
			Class<? extends InventoryItem> itemClass, String table)
			throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("select * from "
				+ table + " where StorageGuid = ?");
		stmt.setBytes(1, character.getGuid());
		ResultSet results = stmt.executeQuery();

		while (results.next()) {
			try {
				character.getInventory().add(
						createInventoryItem(itemClass, results));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private InventoryItem createInventoryItem(
			Class<? extends InventoryItem> itemClass, ResultSet results)
			throws InstantiationException, IllegalAccessException, SQLException {
		InventoryItem item = itemClass.newInstance();

		item.setGuid(results.getBytes("Guid"));
		item.setId(results.getString("Id"));
		item.setName(results.getString("Name"));
		item.setCount(results.getInt("StackCount"));

		if (item instanceof Weapon) {
			Weapon weapon = (Weapon) item;
			weapon.setEquipmentType(Type.valueOf(results
					.getString("EquipmentType")));
		}

		return item;
	}

	public void save(Character character) throws SQLException {
		UpdateStatementBuilder builder = new UpdateStatementBuilder(
				"_Instance_PC", "Id = '" + character.getId() + "'");

		appendSave(builder, character.getAttribute());
		appendSave(builder, character.getTalente());
		appendSave(builder, character.getSonderfertigkeiten());
		appendSave(builder, character.getZauberfertigkeiten());

		builder.append("'XP' = ?", character.getAbenteuerpunkte());
		builder.append("'UpgradeXP' = ?", character.getSteigerungspunkte());

		builder.createStatement(connection).executeUpdate();

		System.out.println(builder);
		System.out.println("Saved " + character);
	}

	public void appendSave(UpdateStatementBuilder builder, IntegerMap values) {
		for (String key : values.getKeys()) {
			builder.append("'" + key + "' = ?", values.get(key));
		}
	}

	public void saveAll() {
		if (characters == null) {
			return;
		}

		for (Character character : characters) {
			try {
				save(character);
			} catch (SQLException e) {
				throw new RuntimeException("Error saving character "
						+ character + ": " + e, e);
			}
		}
	}
}
