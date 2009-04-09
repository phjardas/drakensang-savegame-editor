package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.dao.UpdateStatementBuilder.ParameterType;
import de.jardas.drakensang.dao.inventory.InventoryDao;
import de.jardas.drakensang.model.Advantage;
import de.jardas.drakensang.model.CasterRace;
import de.jardas.drakensang.model.CasterType;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.CharacterSet;
import de.jardas.drakensang.model.Culture;
import de.jardas.drakensang.model.IntegerMap;
import de.jardas.drakensang.model.Profession;
import de.jardas.drakensang.model.Race;
import de.jardas.drakensang.model.Sex;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Set;


public class CharacterDao {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(CharacterDao.class);
    private static Set<Character> characters;

    public static synchronized Set<Character> getCharacters() {
        if (characters == null) {
            try {
                characters = loadCharacters();
            } catch (Exception e) {
                throw new DrakensangException("Error loading characters: " + e,
                    e);
            }
        }

        return characters;
    }

    private static Set<Character> loadCharacters() throws SQLException {
        final String sql = "select * from _Instance_PC order by Name";
        ResultSet result;

        try {
            PreparedStatement statement = SavegameDao.getConnection()
                                                     .prepareStatement(sql);
            result = statement.executeQuery();
        } catch (SQLException e) {
            throw new DrakensangException(
                "Error preparing or executing SQL statement " + sql + ": " + e,
                e);
        }

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

            c.getAttribute().load(result);
            c.getTalente().load(result);
            c.getSonderfertigkeiten().load(result);
            c.getZauberfertigkeiten().load(result);

            if (c.isPlayerCharacter()) {
                c.setCharacterSet(CharacterSet.valueOf(result.getString(
                            "CharacterSet")));
            }

            loadAdvantages(c, result);

            InventoryDao.loadInventory(c);
        }

        return items;
    }

    private static void loadAdvantages(Character c, ResultSet result)
        throws SQLException {
        final String[] tokens = result.getString("Advantages").split("\\s*;\\s*");

        for (String token : tokens) {
            c.getAdvantages().add(Advantage.valueOf(token));
        }
    }

    private static void save(Character character) throws SQLException {
        UpdateStatementBuilder builder = new UpdateStatementBuilder("_Instance_PC",
                "Guid = ?");

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
        builder.append("'Advantages' = ?",
            Advantage.serialize(character.getAdvantages()));

        if (character.isPlayerCharacter()) {
            builder.append("'CharacterSet' = ?",
                character.getCharacterSet().name());
        }

        builder.addParameter(ParameterType.Bytes, character.getGuid());

        LOG.debug("Character update '" + character.getName() + "': " + builder);

        builder.createStatement(SavegameDao.getConnection()).executeUpdate();

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
    }
    
    public static void reset() {
    	LOG.debug("Resetting.");
    	characters = null;
    }
}
