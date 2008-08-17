package de.jardas.drakensang.dao;

import de.jardas.drakensang.dao.UpdateStatementBuilder.ParameterType;
import de.jardas.drakensang.model.CasterRace;
import de.jardas.drakensang.model.CasterType;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.CharacterSet;
import de.jardas.drakensang.model.Culture;
import de.jardas.drakensang.model.IntegerMap;
import de.jardas.drakensang.model.Profession;
import de.jardas.drakensang.model.Race;
import de.jardas.drakensang.model.Sex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Set;

public class CharacterDao {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(CharacterDao.class);
	private final Connection connection;
	private final InventoryDao inventoryDao;
	private Set<Character> characters;

	public CharacterDao(String filename) {
		try {
			LOG.debug("Opening savegame at " + filename);
			connection = DriverManager
					.getConnection("jdbc:sqlite:/" + filename);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Can't open database file '"
					+ filename + "': " + e, e);
		}

		inventoryDao = new InventoryDao(connection);
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
			String name = result.getString("Name");

			if (name.contains("fake") || name.startsWith("loc")
					|| name.contains("cutscene")) {
				LOG.debug("Skipping character " + name + ".");
				continue;
			}

			LOG.debug("Loading character " + name + ".");

			Character c = new Character();
			items.add(c);

			c.setGuid(result.getBytes("Guid"));
			c.setName(name);
			c.setId(result.getString("Id"));
			c.setLookAtText(result.getString("LookAtText"));
			c.setLocalizeLookAtText(result.getBoolean("LocalizeLookAtText"));
			c.setAbenteuerpunkte(result.getInt("XP"));
			c.setSteigerungspunkte(result.getInt("UpgradeXP"));

			c.setSex(Sex.valueOf(result.getString("Sex")));
			c.setRace(Race.valueOf(result.getString("Race")));
			c.setCulture(Culture.valueOf(result.getString("Culture")));
			c.setProfession(Profession.valueOf(result.getString("Profession")));
			c.setMagician(result.getBoolean("IsMagicUser"));
			c.setCasterType(CasterType.valueOf(result.getString("CasterType")));
			c.setCasterRace(CasterRace.valueOf(result.getString("CasterRace")));

			c.getAttribute().load(result);
			c.getTalente().load(result);
			c.getSonderfertigkeiten().load(result);
			c.getZauberfertigkeiten().load(result);
			
			if (c.isPlayerCharacter()) {
				c.setCharacterSet(CharacterSet.valueOf(result.getString("CharacterSet")));
			}

			inventoryDao.loadInventory(c);
		}

		return items;
	}

	private void save(Character character) throws SQLException {
		UpdateStatementBuilder builder = new UpdateStatementBuilder(
				"_Instance_PC", "Guid = ?");

		appendSave(builder, character.getAttribute());
		appendSave(builder, character.getTalente());
		appendSave(builder, character.getSonderfertigkeiten());
		appendSave(builder, character.getZauberfertigkeiten());

		if (character.isPlayerCharacter()) {
			builder.append("'LookAtText' = ?", character.getLookAtText());
		}

		builder.append("'XP' = ?", character.getAbenteuerpunkte());
		builder.append("'UpgradeXP' = ?", character.getSteigerungspunkte());
		builder.append("'Sex' = ?", character.getSex().name());
		builder.append("'Race' = ?", character.getRace().name());
		builder.append("'Culture' = ?", character.getCulture().name());
		builder.append("'Profession' = ?", character.getProfession().name());
		builder.append("'IsMagicUser' = ?", character.isMagician() ? 1 : 0);
		builder.append("'CasterType' = ?", character.getCasterType().name());
		builder.append("'CasterRace' = ?", character.getCasterRace().name());
		
		if (character.isPlayerCharacter()) {
			builder.append("'CharacterSet' = ?", character.getCharacterSet().name());
		}

		builder.addParameter(ParameterType.Bytes, character.getGuid());

		LOG.debug("Character update: " + builder);

		builder.createStatement(connection).executeUpdate();

		inventoryDao.save(character.getInventory());
	}

	private void appendSave(UpdateStatementBuilder builder, IntegerMap values) {
		for (String key : values.getKeys()) {
			builder.append("'" + key + "' = ?", values.get(key));
		}
	}

	public void saveAll() {
		if (characters == null) {
			return;
		}

		try {
			boolean autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);

			for (Character character : characters) {
				save(character);
			}

			connection.commit();
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new RuntimeException("Error saving characters: " + e, e);
		}
	}
}
