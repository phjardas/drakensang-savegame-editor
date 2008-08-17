package de.jardas.drakensang.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.MissingResourceException;

import de.jardas.drakensang.Settings;

public class Static {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(Static.class);
	private static Connection connection;

	private static Connection getConnection() {
		if (connection == null) {
			connection = loadConnection();
		}

		return connection;
	}

	private static Connection loadConnection() {
		File home = Settings.getInstance().getDrakensangHome();
		File localeFile = new File(home, "export/db/static.db4");
		String url = "jdbc:sqlite:/" + localeFile.getAbsolutePath();
		LOG.debug("Opening static values connection to " + url);

		try {
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new RuntimeException(
					"Error opening static values connection to " + localeFile
							+ ": " + e, e);
		}
	}

	public static String get(String col, String key, String idCol, String table) {
		// LOG.debug("Loading '" + col + "' from table '" + table + "' where '" + idCol + "' = '" + key + "'.");
		String sql = "select " + col + " from " + table + " where " + idCol
				+ " = ?";
		
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, key);
			ResultSet result = stmt.executeQuery();

			if (!result.next()) {
				throw new MissingResourceException(
						"No static value found in table '" + table + "' for '"
								+ key + "'.", Messages.class.getName(), key);
			}

			return result.getString(col);
		} catch (SQLException e) {
			throw new MissingResourceException(
					"Error looking up static value in table '" + table
							+ "' for '" + key + "': " + e, Messages.class
							.getName(), key);
		}
	}
}
