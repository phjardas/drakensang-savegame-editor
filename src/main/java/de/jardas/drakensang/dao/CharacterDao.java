package de.jardas.drakensang.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.db.DaoHelper;
import de.jardas.drakensang.shared.db.UpdateStatementBuilder;
import de.jardas.drakensang.shared.db.UpdateStatementBuilder.ParameterType;
import de.jardas.drakensang.shared.db.inventory.InventoryDao;
import de.jardas.drakensang.shared.model.CharSet;
import de.jardas.drakensang.shared.model.Character;
import de.jardas.drakensang.shared.model.Face;
import de.jardas.drakensang.shared.model.Hair;

public class CharacterDao {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
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

			DaoHelper.load(c, result);
			c.setGuid(result.getBytes("Guid"));
			c.setLocalizeLookAtText(result.getBoolean("LocalizeLookAtText"));

			c.setLevel(result.getInt("Stufe"));
			c.setAbenteuerpunkte(result.getInt("XP"));
			c.setSteigerungspunkte(result.getInt("UpgradeXP"));
			c.setMaxVelocity(DaoHelper
					.round(result.getDouble("MaxVelocity"), 4));

			final String groups = (result.getString("Groups") != null) ? result
					.getString("Groups") : "";
			c.setCurrentPartyMember(groups.contains("_Group_PlayerParty"));
			c.setPartyMember(c.isCurrentPartyMember()
					|| groups.contains("GainXpGroup"));

			if (c.isPlayerCharacter()) {
				c.setHair(Hair.valueOf(result.getString("HairSkin")));
				c.setFace(Face.valueOf(result.getString("HeadSkin")));
				c.setCharSet(CharSet.valueOf(result.getString("CharacterSet")));
			}

			InventoryDao.loadInventory(c, "_Instance_", SavegameDao
					.getConnection());

			c.initialized();

			DaoHelper.load(c.getLebensenergie(), result);
			DaoHelper.load(c.getAstralenergie(), result);
			DaoHelper.load(c.getAusdauer(), result);
			// DaoHelper.load(c.getKarma(), result);

			items.add(c);
			progress.onCharacterLoaded(c);
		}

		return items;
	}

	private static void save(Character character) throws SQLException {
		UpdateStatementBuilder builder = new UpdateStatementBuilder(
				"_Instance_PC", "Guid = ?");

		DaoHelper.store(character, builder);

		builder.append("'Stufe' = ?", character.getLevel());
		builder.append("'XP' = ?", character.getAbenteuerpunkte());
		builder.append("'UpgradeXP' = ?", character.getSteigerungspunkte());
		builder.append("'MaxVelocity' = ?", character.getMaxVelocity());

		if (character.isPlayerCharacter()) {
			builder.append("'LookAtText' = ?", character.getLookAtText());
			builder.append("'HairSkin' = ?", character.getHair().name());
			builder.append("'HeadSkin' = ?", character.getFace().name());
			builder.append("'CharacterSet' = ?", character.getCharSet().name());
		}

		builder.addParameter(ParameterType.Bytes, character.getGuid());

		if (LOG.isDebugEnabled()) {
			LOG.debug("Character update '" + character.getName() + "': "
					+ builder);
		}

		builder.createStatement(SavegameDao.getConnection()).executeUpdate();

		InventoryDao.save(character.getInventory(), "_Instance_", SavegameDao
				.getConnection());
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
