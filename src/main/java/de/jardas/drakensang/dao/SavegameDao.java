package de.jardas.drakensang.dao;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.jardas.drakensang.model.savegame.Savegame;
import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.Settings;
import de.jardas.drakensang.shared.registry.WindowsRegistry;

public class SavegameDao {
	private static final byte[] HEADER_SQLITE = { 83, 81, 76, }; // SQL
	private static final byte[] HEADER_ZIP = { 80, 75, 0, }; // PK*
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
			.getLogger(SavegameDao.class);
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd-HHmmss");
	private static Savegame savegame;
	private static SavegameDao instance;
	private static Connection connection;
	private File savegameLocation;
	private File unpackedSavegame;

	private SavegameDao(Savegame savegame) {
		close();

		try {
			unpackSavegame(savegame);
		} catch (IOException e) {
			throw new DrakensangException("Error unpacking savegame at "
					+ savegame.getFile() + ": " + e, e);
		}

		try {
			LOG.debug("Opening savegame at " + savegameLocation);
			connection = DriverManager.getConnection("jdbc:sqlite:/"
					+ savegameLocation);
			SavegameDao.savegame = savegame;

			CharacterDao.reset();
		} catch (Exception e) {
			throw new DrakensangException("Can't open database file '"
					+ savegame.getFile() + "': " + e, e);
		}
	}

	private void unpackSavegame(Savegame save) throws IOException {
		FileOutputStream out = null;
		FileInputStream in = null;

		try {
			in = new FileInputStream(save.getFile());
			final byte[] header = new byte[3];
			in.read(header);
			IOUtils.closeQuietly(in);
			in = new FileInputStream(save.getFile());

			if (equals(HEADER_SQLITE, header)) {
				unpackedSavegame = null;
				savegameLocation = save.getFile();
			} else if (equals(HEADER_ZIP, header)) {
				ZipInputStream zin = null;

				try {
					zin = new ZipInputStream(in);

					// Es gibt genau eine Datei im ZIP.
					zin.getNextEntry();

					unpackedSavegame = File.createTempFile(
							"drakensang2-editor-unpacked-savegame-", ".db4");
					savegameLocation = unpackedSavegame;
					out = new FileOutputStream(unpackedSavegame);

					LOG.debug("Unpacking savegame from " + save.getFile()
							+ " to " + unpackedSavegame);

					IOUtils.copy(zin, out);
				} finally {
					IOUtils.closeQuietly(zin);
				}
			} else {
				throw new IllegalArgumentException("Unbekannter Header in "
						+ save.getFile() + ": " + Arrays.toString(header));
			}
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}

	private boolean equals(byte[] expected, byte[] actual) {
		if (expected == null || actual == null) {
			return expected == null && actual == null;
		}

		if (expected.length != actual.length) {
			return false;
		}

		for (int i = 0; i < expected.length; i++) {
			if (expected[i] != 0 && expected[i] != actual[i]) {
				return false;
			}
		}

		return true;
	}

	public void publishSavegame() throws IOException {
		if (unpackedSavegame == null) {
			return;
		}

		FileOutputStream out = null;
		ZipOutputStream zout = null;
		FileInputStream in = null;

		try {
			in = new FileInputStream(unpackedSavegame);
			out = new FileOutputStream(savegame.getFile());
			zout = new ZipOutputStream(out);

			final ZipEntry entry = new ZipEntry("game.db4");
			zout.putNextEntry(entry);

			LOG.debug("Packing V3 savegame from " + unpackedSavegame + " to "
					+ savegame.getFile());

			IOUtils.copy(in, zout);
		} finally {
			IOUtils.closeQuietly(zout);
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}

	public static List<Savegame> getSavegames(Progress progress) {
		final List<Savegame> saves = new ArrayList<Savegame>();

		for (File savedir : getSavesDirectories()) {
			if (savedir.isDirectory()) {
				saves.addAll(getSavegames(savedir, progress));
			}
		}

		return saves;
	}

	private static List<Savegame> getSavegames(File savedir, Progress progress) {
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
				progress.onSavegameFailed(file, e);
			}
		}

		Collections.sort(savegames, Collections.reverseOrder());

		return savegames;
	}

	private static File[] getSavesDirectories() {
		final File documentsDir = new File(WindowsRegistry
				.getCurrentUserPersonalFolderPath());
		final File savedir = new File(documentsDir,
				"Drakensang_TRoT/profiles/default/save/");
		final File demodir = new File(documentsDir,
				"Drakensang_TRoT_Demo/profiles/default/save/");

		return new File[] { savedir, demodir, };
	}

	public static SavegameDao open(Savegame savegame) {
		instance = new SavegameDao(savegame);

		return getInstance();
	}

	public static void close() {
		if (instance != null) {
			instance.closeConnection();
		}
	}

	private void closeConnection() {
		if (connection != null) {
			LOG.info("Closing connection to " + savegameLocation + ".");

			try {
				connection.close();
			} catch (SQLException e) {
				LOG.error("Error closing connection: " + e, e);
			}

			if (unpackedSavegame != null) {
				LOG.debug("Deleting unpacked savegame file at "
						+ unpackedSavegame);
				unpackedSavegame.delete();
			}
		}

		connection = null;
		instance = null;
		savegame = null;
		unpackedSavegame = null;
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
		final File backupDir = new File(Settings.getSettingsDirectory(),
				"backups");
		final File dir = new File(new File(backupDir, DATE_FORMAT
				.format(new Date())), getSavegame().getDirectory().getName());

		if (dir.exists()) {
			throw new DrakensangException("Backup directory already exists: "
					+ dir);
		}

		if (!dir.mkdirs()) {
			throw new DrakensangException("Error creating backup directory "
					+ dir);
		}

		LOG.info("Saving backup to " + dir);

		try {
			FileUtils.copyDirectory(getSavegame().getDirectory(), dir, true);
		} catch (IOException e) {
			throw new DrakensangException("Error creating savegame backup to "
					+ dir + ": " + e, e);
		}

		return dir;
	}

	public static interface Progress {
		void setTotalNumberOfSavegames(int total);

		void onSavegameLoaded(Savegame savegame);

		void onSavegameFailed(File file, Throwable cause);
	}
}
