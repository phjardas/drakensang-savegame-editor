package de.jardas.drakensang.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.jardas.drakensang.DrakensangException;
import de.jardas.drakensang.model.Archetype;
import de.jardas.drakensang.model.CasterRace;
import de.jardas.drakensang.model.CasterType;
import de.jardas.drakensang.model.Culture;
import de.jardas.drakensang.model.Profession;
import de.jardas.drakensang.model.Race;
import de.jardas.drakensang.model.Sex;

public class ArchetypeDao {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(ArchetypeDao.class);
	private static Map<String, Archetype> archetypes = new HashMap<String, Archetype>();
	private static String[] hairs;
	private static String[] faces;
	private static String[] bodies;

	public static Archetype get(String id) {
		final Archetype archetype = getArchetypes().get(id);

		if (archetype == null) {
			throw new IllegalArgumentException("Unknown archetype: '" + id
					+ "'");
		}

		return archetype;
	}

	private static synchronized void initialize() {
		if (archetypes.isEmpty()) {
			loadArchetypes();
		}
	}

	private static void loadArchetypes() {
		final String sql = "select * from _Template_PC_CharWizard";
		final  Set<String> hairsCollected = new HashSet<String>();
		final  Set<String> facesCollected = new HashSet<String>();
		final Set<String> bodiesCollected = new HashSet<String>();
		
		try {
			final PreparedStatement statement = Static.getConnection()
					.prepareStatement(sql);
			final ResultSet result = statement.executeQuery();

			while (result.next()) {
				final String id = result.getString("Id");
				final String archetypeClass = result.getString("ArchetypeID");
				final String icon = result.getString("IconBrush");
				final String[] hairs = result.getString("SelectableHairs")
						.split("\\s*;\\s*");
				final String[] faces = result.getString("SelectableFaces")
						.split("\\s*;\\s*");
				final String[] bodies = result.getString("SelectableCharSets")
						.split("\\s*;\\s*");
				final Race race = Race.valueOf(result.getString("Race"));
				final Culture culture = Culture.valueOf(result
						.getString("Culture"));
				final Profession profession = Profession.valueOf(result
						.getString("Profession"));
				final Sex sex = Sex.valueOf(result.getString("Sex"));
				final CasterType casterType = CasterType.valueOf(result
						.getString("CasterType"));
				final CasterRace casterRace = CasterRace.valueOf(result
						.getString("CasterRace"));

				for (String hair : hairs) {
					hairsCollected.add(hair);
				}

				for (String face : faces) {
					facesCollected.add(face);
				}

				for (String body : bodies) {
					bodiesCollected.add(body);
				}

				final Archetype archetype = new Archetype(id, archetypeClass,
						icon, race, culture, profession, casterType,
						casterRace, sex, hairs, faces, bodies);
				LOG.debug("Loaded archetype: " + archetype);
				archetypes.put(id, archetype);
			}
			
			hairs = hairsCollected.toArray(new String[hairsCollected.size()]);
			Arrays.sort(hairs);
			faces = facesCollected.toArray(new String[facesCollected.size()]);
			Arrays.sort(faces);
			bodies = bodiesCollected.toArray(new String[bodiesCollected.size()]);
			Arrays.sort(bodies);
		} catch (SQLException e) {
			throw new DrakensangException("Error loading archetypes: " + e, e);
		}
	}

	public static Map<String, Archetype> getArchetypes() {
		initialize();
		return archetypes;
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
