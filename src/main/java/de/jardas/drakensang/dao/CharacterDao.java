package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.dao.UpdateStatementBuilder.ParameterType;
import de.jardas.drakensang.dao.inventory.InventoryDao;
import de.jardas.drakensang.model.Advantage;
import de.jardas.drakensang.model.CasterRace;
import de.jardas.drakensang.model.CasterType;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.Culture;
import de.jardas.drakensang.model.IntegerMap;
import de.jardas.drakensang.model.Profession;
import de.jardas.drakensang.model.Race;
import de.jardas.drakensang.model.Sex;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.WordUtils;

public class CharacterDao {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(CharacterDao.class);
	private static Set<Character> characters;

	public static synchronized Set<Character> loadCharacters(Progress progress) {
		if (characters == null) {
			try {
				characters = loadCharactersInternal(progress);
			} catch (Exception e) {
				throw new DrakensangException("Error loading characters: " + e,
						e);
			}
		}

		return characters;
	}

	public static synchronized Set<Character> getCharacters() {
		return loadCharacters(new Progress() {
			public void setTotalNumberOfCharacters(int arg0) {
				// ignored
			}

			public void onCharacterLoaded(Character arg0) {
				// ignored
			}
		});
	}

	private static int getNumberOfCharacters() {
		final String sql = "select count(*) from _Instance_PC where name not like '%fake%' and name not like 'loc%' and name not like '%cutscene%'";

		try {
			final PreparedStatement statement = SavegameDao.getConnection()
					.prepareStatement(sql);
			final ResultSet result = statement.executeQuery();
			result.next();

			return result.getInt(1);
		} catch (SQLException e) {
			throw new DrakensangException(
					"Error preparing or executing SQL statement " + sql + ": "
							+ e, e);
		}
	}

	private static Set<Character> loadCharactersInternal(Progress progress)
			throws SQLException {
		progress.setTotalNumberOfCharacters(getNumberOfCharacters());

		final String sql = "select * from _Instance_PC where name not like '%fake%' and name not like 'loc%' and name not like '%cutscene%' order by Name";
		ResultSet result;

		try {
			PreparedStatement statement = SavegameDao.getConnection()
					.prepareStatement(sql);
			result = statement.executeQuery();
		} catch (SQLException e) {
			throw new DrakensangException(
					"Error preparing or executing SQL statement " + sql + ": "
							+ e, e);
		}

		Set<Character> items = new HashSet<Character>();

		while (result.next()) {
			final String name = result.getString("Name");

			LOG.debug("Loading character " + name + ".");

			final Character c = new Character();
			items.add(c);

			c.setGuid(result.getBytes("Guid"));
			c.setName(name);
			c.setId(result.getString("Id"));
			c.setLookAtText(result.getString("LookAtText"));
			c.setLocalizeLookAtText(result.getBoolean("LocalizeLookAtText"));
			c.setLevel(result.getInt("Stufe"));
			c.setAbenteuerpunkte(result.getInt("XP"));
			c.setSteigerungspunkte(result.getInt("UpgradeXP"));

			c.setSex(Sex.valueOf(result.getString("Sex")));
			c.setRace(Race.valueOf(result.getString("Race")));
			c.setCulture(Culture.valueOf(result.getString("Culture")));
			c.setProfession(Profession.valueOf(result.getString("Profession")));
			c.setMagician(result.getBoolean("IsMagicUser"));
			c.setCasterType(CasterType.valueOf(result.getString("CasterType")));
			c.setCasterRace(CasterRace.valueOf(result.getString("CasterRace")));

			c.setLebensenergie(result.getInt("LE"));
			c.setLebensenergieBonus(result.getInt("LEBonus"));
			c.setAstralenergie(result.getInt("AE"));
			c.setAstralenergieBonus(result.getInt("AEBonus"));

			c.setSneakSpeed(round(result.getDouble("SneakSpeed"), 4));
			c.setWalkSpeed(round(result.getDouble("WalkSpeed"), 4));
			c.setRunSpeed(round(result.getDouble("RunSpeed"), 4));
			c.setCurrentSpeed(round(result.getDouble("CurrentSpeed"), 4));
			c.setMaxVelocity(round(result.getDouble("MaxVelocity"), 4));

			final String groups = (result.getString("Groups") != null) ? result
					.getString("Groups") : "";
			c.setCurrentPartyMember(groups.contains("_Group_PlayerParty"));
			c.setPartyMember(c.isCurrentPartyMember()
					|| groups.contains("GainXpGroup"));

			c.getAttribute().load(result);
			c.getTalente().load(result);
			c.getSonderfertigkeiten().load(result);
			c.getZauberfertigkeiten().load(result);

			if (c.isPlayerCharacter()) {
				c.setHair(result.getString("HairSkin"));
				c.setFace(result.getString("HeadSkin"));
				c.setBody(result.getString("CharacterSet"));
			}

			loadAdvantages(c, result);

			InventoryDao.loadInventory(c);

			progress.onCharacterLoaded(c);
		}

		return items;
	}

	private static double round(double input, int precision) {
		return BigDecimal.valueOf(input).round(new MathContext(precision))
				.doubleValue();
	}

	private static void loadAdvantages(Character c, ResultSet result)
			throws SQLException {
		final String[] tokens = result.getString("Advantages").split(
				"\\s*;\\s*");

		for (String token : tokens) {
			c.getAdvantages().add(Advantage.valueOf(token));
		}
	}

	private static void save(Character character) throws SQLException {
		UpdateStatementBuilder builder = new UpdateStatementBuilder(
				"_Instance_PC", "Guid = ?");

		appendSave(builder, character.getAttribute());
		appendSave(builder, character.getTalente());
		appendSave(builder, character.getSonderfertigkeiten());
		appendSave(builder, character.getZauberfertigkeiten());

		if (character.isPlayerCharacter()) {
			builder.append("'LookAtText' = ?", character.getLookAtText());
		}

		builder.append("'Stufe' = ?", character.getLevel());
		builder.append("'XP' = ?", character.getAbenteuerpunkte());
		builder.append("'UpgradeXP' = ?", character.getSteigerungspunkte());
		builder.append("'Sex' = ?", character.getSex().name());
		builder.append("'Race' = ?", character.getRace().name());
		builder.append("'Culture' = ?", character.getCulture().name());
		builder.append("'Profession' = ?", character.getProfession().name());
		builder.append("'IsMagicUser' = ?", character.isMagician() ? 1 : 0);
		builder.append("'CasterType' = ?", character.getCasterType().name());
		builder.append("'CasterRace' = ?", character.getCasterRace().name());
		builder.append("'LE' = ?", character.getLebensenergie());
		builder.append("'LEBonus' = ?", character.getLebensenergieBonus());
		builder.append("'AE' = ?", character.getAstralenergie());
		builder.append("'AEBonus' = ?", character.getAstralenergieBonus());
		builder.append("'Advantages' = ?", Advantage.serialize(character
				.getAdvantages()));
		builder.append("'SneakSpeed' = ?", character.getSneakSpeed());
		builder.append("'WalkSpeed' = ?", character.getWalkSpeed());
		builder.append("'RunSpeed' = ?", character.getRunSpeed());
		builder.append("'CurrentSpeed' = ?", character.getCurrentSpeed());
		builder.append("'MaxVelocity' = ?", character.getMaxVelocity());

		if (character.isPlayerCharacter()) {
			builder.append("'HairSkin' = ?", character.getHair());
			builder.append("'HeadSkin' = ?", character.getFace());
			builder.append("'CharacterSet' = ?", character.getBody());
			builder.append("'Graphics' = ?", "characters/"
					+ character.getSex().name());
			builder.append("'AnimSet' = ?", character.getSex().name());
			builder.append("'PickingPhysics' = ?", "characters/"
					+ character.getSex().name() + "/skeleton");
			builder.append("'SoundSet' = ?", "default"
					+ WordUtils.capitalize(character.getSex().name()));
		}

		builder.addParameter(ParameterType.Bytes, character.getGuid());

		LOG.debug("Character update '" + character.getName() + "': " + builder);

		final int updated = builder
				.createStatement(SavegameDao.getConnection()).executeUpdate();
		LOG.debug("Updated rows: " + updated);

		InventoryDao.save(character.getInventory());
	}

	private static void appendSave(UpdateStatementBuilder builder,
			IntegerMap values) {
		for (String key : values.getKeys()) {
			builder.append("'" + key + "' = ?", values.get(key));
		}
	}

	public static void saveAll() {
		if (characters == null) {
			return;
		}

		try {
			boolean autoCommit = SavegameDao.getConnection().getAutoCommit();
			SavegameDao.getConnection().setAutoCommit(false);

			for (Character character : characters) {
				save(character);
			}

			SavegameDao.getConnection().commit();
			SavegameDao.getConnection().setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new RuntimeException("Error saving characters: " + e, e);
		}

		try {
			SavegameDao.getInstance().publishSavegame();
		} catch (IOException e) {
			throw new RuntimeException("Error saving savegame: " + e, e);
		}
	}

	public static void reset() {
		LOG.debug("Resetting.");
		characters = null;
	}

	public static interface Progress {
		void setTotalNumberOfCharacters(int total);

		void onCharacterLoaded(Character character);
	}
}
