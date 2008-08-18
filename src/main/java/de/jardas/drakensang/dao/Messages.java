package de.jardas.drakensang.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.jardas.drakensang.Main;
import de.jardas.drakensang.Settings;

public class Messages {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(Messages.class);
	private static final Map<String, String> CACHE = new HashMap<String, String>();
	private static Connection connection;

	static {
		ResourceBundle bundle = ResourceBundle.getBundle(Main.class
				.getPackage().getName()
				+ ".messages");
		Enumeration<String> keys = bundle.getKeys();

		while (keys.hasMoreElements()) {
			final String key = keys.nextElement();
			cache(key, bundle.getString(key));
		}
	}

	private static void cache(String key, String value) {
		CACHE.put(key.toLowerCase(), value);
	}

	private static Connection getConnection() {
		if (connection == null) {
			connection = loadConnection();
		}

		return connection;
	}

	private static Connection loadConnection() {
		File home = Settings.getInstance().getDrakensangHome();
		File localeFile = new File(home, "export/db/locale.db4");
		String url = "jdbc:sqlite:/" + localeFile.getAbsolutePath();
		LOG.debug("Opening localization connection to " + url);

		try {
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new RuntimeException(
					"Error opening localization connection to " + localeFile
							+ ": " + e, e);
		}
	}

	public static String get(String key) {
		if (key == null) {
			return "";
		}

		try {
			return getRequired(key);
		} catch (MissingResourceException e) {
			LOG.warn("No localization found for '" + key + "': " + e);
			return "!!!" + key + "!!!";
		}
	}

	public static String getRequired(String key) {
		String value = CACHE.get(key.toLowerCase());

		if (value != null) {
			return value;
		}

		value = get("LocaText", key, "LocaId", "_Locale");
		cache(key, value);

		return value;
	}

	public static String get(String col, String key, String idCol, String table) {
		try {
			LOG.debug("Loading " + col + " from " + table + " where " + idCol
					+ " = '" + key + "'.");
			PreparedStatement stmt = getConnection().prepareStatement(
					"select " + col + " from " + table + " where " + idCol
							+ " = ?");
			stmt.setString(1, key);
			ResultSet result = stmt.executeQuery();

			if (!result.next()) {
				throw new MissingResourceException(
						"No localization found for '" + key + "'.",
						Messages.class.getName(), key);
			}

			return result.getString(col);
		} catch (SQLException e) {
			throw new MissingResourceException(
					"Error looking up localized value for '" + key + "': " + e,
					Messages.class.getName(), key);
		}
	}

	public static boolean testConnection() {
		try {
			getConnection();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void resetConnection() {
		LOG.debug("Resetting connection.");
		connection = null;
	}
}
