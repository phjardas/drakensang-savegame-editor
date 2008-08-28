/*
 * LevelDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.dao.inventory.InventoryDao;
import de.jardas.drakensang.model.Chest;
import de.jardas.drakensang.model.Level;

import org.apache.commons.lang.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


public class LevelDao {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(LevelDao.class);
    private static List<Level> levels;

    public static List<Level> getLevels() {
        if (levels == null) {
            try {
                PreparedStatement stmt = SavegameDao.getConnection()
                                                    .prepareStatement("select * from _Instance_Levels");
                ResultSet result = stmt.executeQuery();
                levels = new ArrayList<Level>();

                while (result.next()) {
                    Level level = new Level();
                    level.setId(result.getString("Id"));
                    level.setName(result.getString("Name"));
                    level.setWorldMapLocation(result.getString(
                            "WorldMapLocation"));

                    if (!StringUtils.isBlank(level.getWorldMapLocation())) {
                        levels.add(level);
                    }
                }
            } catch (SQLException e) {
                throw new DrakensangException("Error loading levels: " + e, e);
            }
        }

        return levels;
    }

    public static Level getLevel(String id) {
        if (id == null) {
            return null;
        }

        for (Level level : getLevels()) {
            if (id.equals(level.getId())) {
                return level;
            }
        }

        return null;
    }

    public static List<Chest> getChests(Level level) {
        LOG.debug("Loading chests in " + level);

        try {
            PreparedStatement stmt = SavegameDao.getConnection()
                                                .prepareStatement("select * from _Instance_Chest where _Level = ?");
            stmt.setString(1, level.getId());

            ResultSet result = stmt.executeQuery();
            List<Chest> chests = new ArrayList<Chest>();

            while (result.next()) {
                Chest chest = new Chest();
                chest.setGuid(result.getBytes("Guid"));
                chest.setId(result.getString("Id"));
                chest.setName(result.getString("Name"));
                chest.setLookAtText(result.getString("LookAtText"));

                chest.setItems(InventoryDao.loadItems(chest.getGuid()));

                chests.add(chest);
            }

            return chests;
        } catch (SQLException e) {
            throw new DrakensangException("Error loading chests from " + level
                + ": " + e, e);
        }
    }

    public static void reset() {
        levels = null;
    }
}
