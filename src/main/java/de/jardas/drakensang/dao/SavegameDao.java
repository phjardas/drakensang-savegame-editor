/*
 * SavegameDao.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.model.savegame.Savegame;
import de.jardas.drakensang.util.WindowsRegistry;

import java.io.File;
import java.io.FileFilter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SavegameDao {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SavegameDao.class);
    private static Savegame savegame;
    private static SavegameDao instance;
    private static Connection connection;

    private SavegameDao(Savegame savegame) {
        close();

        try {
            LOG.debug("Opening savegame at " + savegame.getFile());
            connection = DriverManager.getConnection("jdbc:sqlite:/" +
                    savegame.getFile());
            SavegameDao.savegame = savegame;

            LevelDao.reset();
            CharacterDao.reset();
        } catch (Exception e) {
            throw new DrakensangException("Can't open database file '" +
                savegame.getFile() + "': " + e, e);
        }
    }

    public static Savegame getLatestSavegame() {
        List<Savegame> savegames = getSavegames();
        Collections.sort(savegames, Collections.reverseOrder());

        return savegames.isEmpty() ? null : savegames.get(0);
    }

    public static List<Savegame> getSavegames() {
        File savedir = getSavesDirectory();
        File[] files = savedir.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                });

        List<Savegame> savegames = new ArrayList<Savegame>(files.length);

        for (File file : files) {
            try {
                savegames.add(Savegame.load(file));
            } catch (IllegalArgumentException e) {
                LOG.warn("Error loading savegame from " + file + ": " + e, e);

                // ignore
            }
        }

        Collections.sort(savegames, Collections.reverseOrder());

        return savegames;
    }

    public static File getSavesDirectory() {
        File documentsDir = new File(WindowsRegistry.getCurrentUserPersonalFolderPath());
        File savedir = new File(documentsDir,
                "Drakensang/profiles/default/save/");

        return savedir;
    }

    public static SavegameDao open(Savegame savegame) {
        instance = new SavegameDao(savegame);

        return getInstance();
    }

    public static void close() {
        if (connection != null) {
            LOG.info("Closing connection to " + savegame.getFile() + ".");

            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Error closing connection: " + e, e);
            }
        }

        connection = null;
        instance = null;
        savegame = null;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static SavegameDao getInstance() {
        return instance;
    }

    public static Savegame getSavegame() {
        return savegame;
    }
}
