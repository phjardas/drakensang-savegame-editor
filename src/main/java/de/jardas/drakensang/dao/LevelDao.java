package de.jardas.drakensang.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.model.Level;

public class LevelDao {
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
					level.setWorldMapLocation(result
							.getString("WorldMapLocation"));

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

	public static void reset() {
		levels = null;
	}
}
