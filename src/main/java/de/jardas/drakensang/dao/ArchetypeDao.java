package de.jardas.drakensang.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.jardas.drakensang.DrakensangException;

public class ArchetypeDao {
	private static boolean initialized;
	private static String[] hairs;
	private static String[] faces;
	private static String[] bodies;

	private static synchronized void initialize() {
		if (!initialized) {
			loadArchetypes();
			initialized = true;
		}
	}

	private static void loadArchetypes() {
		final String sql = "select * from _Template_PC_CharWizard";
		final Set<String> hairsCollected = new HashSet<String>();
		final Set<String> facesCollected = new HashSet<String>();
		final Set<String> bodiesCollected = new HashSet<String>();

		try {
			final PreparedStatement statement = Static.getConnection()
					.prepareStatement(sql);
			final ResultSet result = statement.executeQuery();

			while (result.next()) {
				final String[] hairs = result.getString("SelectableHairs")
						.split("\\s*;\\s*");
				final String[] faces = result.getString("SelectableFaces")
						.split("\\s*;\\s*");
				final String[] bodies = result.getString("SelectableCharSets")
						.split("\\s*;\\s*");

				for (String hair : hairs) {
					hairsCollected.add(hair);
				}

				for (String face : faces) {
					facesCollected.add(face);
				}

				for (String body : bodies) {
					bodiesCollected.add(body);
				}
			}

			hairs = hairsCollected.toArray(new String[hairsCollected.size()]);
			Arrays.sort(hairs);
			faces = facesCollected.toArray(new String[facesCollected.size()]);
			Arrays.sort(faces);
			bodies = bodiesCollected
					.toArray(new String[bodiesCollected.size()]);
			Arrays.sort(bodies);
		} catch (SQLException e) {
			throw new DrakensangException("Error loading archetypes: " + e, e);
		}
	}

	public static String[] getHairs() {
		initialize();
		return hairs;
	}

	public static String[] getFaces() {
		initialize();
		return faces;
	}

	public static String[] getBodies() {
		initialize();
		return bodies;
	}
}
