package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.Settings;
import de.jardas.drakensang.model.savegame.Savegame;
import de.jardas.drakensang.util.WindowsRegistry;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SavegameDao {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SavegameDao.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyyMMdd-HHmmss");
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

    public static List<Savegame> getSavegames(Progress progress) {
        File savedir = getSavesDirectory();
        File[] files = savedir.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isDirectory();
                    }
                });

        progress.setTotalNumberOfSavegames(files.length);

        List<Savegame> savegames = new ArrayList<Savegame>(files.length);

        for (File file : files) {
        	try {
                final Savegame save = Savegame.load(file);
                savegames.add(save);
                progress.onSavegameLoaded(save);
            } catch (IllegalArgumentException e) {
                LOG.warn("Error loading savegame from " + file + ": " + e, e);
                progress.onSavegameFailed(file);
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

    public static File createBackup() {
        if (!Settings.getInstance().isCreateBackupOnSave()) {
            return null;
        }

        final File backupDir = Settings.getInstance().getBackupDirectory();
        final File dir = new File(new File(backupDir,
                    DATE_FORMAT.format(new Date())),
                getSavegame().getDirectory().getName());

        if (dir.exists()) {
            throw new DrakensangException("Backup directory already exists: " +
                dir);
        }

        if (!dir.mkdirs()) {
            throw new DrakensangException("Error creating backup directory " +
                dir);
        }

        LOG.info("Saving backup to " + dir);

        try {
            FileUtils.copyDirectory(getSavegame().getDirectory(), dir, true);
        } catch (IOException e) {
            throw new DrakensangException("Error creating savegame backup to " +
                dir + ": " + e, e);
        }

        return dir;
    }

    public static interface Progress {
        void setTotalNumberOfSavegames(int total);

        void onSavegameLoaded(Savegame savegame);

        void onSavegameFailed(File file);
    }
}
