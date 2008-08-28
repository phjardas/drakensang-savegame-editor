package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.dao.UpdateStatementBuilder.ParameterType;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class CharacterDao {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(CharacterDao.class);
    private static final Random RANDOM = new Random();
    private final Connection connection;
    private final InventoryDao inventoryDao;
    private Set<Character> characters;
    private List<byte[]> guids;

    public CharacterDao(String filename) {
        try {
            LOG.debug("Opening savegame at " + filename);
            connection = DriverManager.getConnection("jdbc:sqlite:/" + filename);
        } catch (Exception e) {
            throw new DrakensangException("Can't open database file '"
                + filename + "': " + e, e);
        }

        inventoryDao = new InventoryDao(connection);
    }

    private List<byte[]> getGuids() {
        if (this.guids == null) {
            this.guids = collectIds();
        }

        return this.guids;
    }

    public byte[] generateGuid() {
        while (true) {
            byte[] id = new byte[16];
            RANDOM.nextBytes(id);

            if (!getGuids().contains(id)) {
                LOG.debug("Generated Guid: " + Arrays.toString(id));
                getGuids().add(id);

                return id;
            }

            LOG.debug("Guid already exists: " + Arrays.toString(id));
        }
    }

    private List<byte[]> collectIds() {
        List<byte[]> ids = new ArrayList<byte[]>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(
                    "select name from sqlite_master where type = 'table'");

            while (result.next()) {
                String table = result.getString("name");

                try {
                    ids.addAll(collectIds(table));
                } catch (SQLException e) {
                    LOG.debug("Error loading IDs from table '" + table + "': "
                        + e);
                }
            }

            LOG.debug("Got a total of " + ids.size() + " IDs.");
        } catch (SQLException e) {
            throw new DrakensangException("Error collecting IDs: " + e, e);
        }

        Collections.sort(ids,
            new Comparator<byte[]>() {
                public int compare(byte[] o1, byte[] o2) {
                    if (o1.length > o2.length) {
                        return 1;
                    }

                    if (o1.length < o2.length) {
                        return -1;
                    }

                    for (int i = 0; i < o1.length; i++) {
                        if (o1[i] > o2[i]) {
                            return 1;
                        }

                        if (o1[i] < o2[i]) {
                            return -1;
                        }
                    }

                    return 0;
                }
            });

        byte[] previous = null;

        for (byte[] id : ids) {
            if ((previous != null) && previous.equals(id)) {
                throw new DrakensangException("Duplicate Guid found: "
                    + Arrays.toString(id));
            }

            previous = id;
        }

        return ids;
    }

    public List<byte[]> collectIds(String table) throws SQLException {
        LOG.debug("Loading IDs from table '" + table + "'.");

        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("select Guid from " + table);
        List<byte[]> ids = new ArrayList<byte[]>();

        while (result.next()) {
            ids.add(result.getBytes("Guid"));
        }

        LOG.debug("Loaded " + ids.size() + " IDs from table '" + table + "'.");

        return ids;
    }

    public InventoryDao getInventoryDao() {
        return inventoryDao;
    }

    public synchronized Set<Character> getCharacters() {
        if (this.characters == null) {
            try {
                this.characters = loadCharacters();
            } catch (Exception e) {
                throw new DrakensangException("Error loading characters: " + e,
                    e);
            }
        }

        return this.characters;
    }

    private Set<Character> loadCharacters() throws SQLException {
        final String sql = "select * from _Instance_PC order by Name";
        ResultSet result;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
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

            c.setLebensenergieBonus(result.getInt("LEBonus"));
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

            inventoryDao.loadInventory(c);
        }

        return items;
    }

    private void loadAdvantages(Character c, ResultSet result)
        throws SQLException {
        final String[] tokens = result.getString("Advantages").split("\\s*;\\s*");

        for (String token : tokens) {
            c.getAdvantages().add(Advantage.valueOf(token));
        }
    }

    private void save(Character character) throws SQLException {
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
        builder.append("'LEBonus' = ?", character.getLebensenergieBonus());
        builder.append("'AEBonus' = ?", character.getAstralenergieBonus());
        builder.append("'Advantages' = ?",
            Advantage.serialize(character.getAdvantages()));

        if (character.isPlayerCharacter()) {
            builder.append("'CharacterSet' = ?",
                character.getCharacterSet().name());
        }

        builder.addParameter(ParameterType.Bytes, character.getGuid());

        LOG.debug("Character update '" + character.getName() + "': " + builder);

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

    public void close() {
        LOG.info("Closing connection.");

        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                LOG.error("Error closing connection: " + e, e);
            }
        }
    }
}
