package de.jardas.drakensang.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class IntegerMap {
	private final Map<String, Integer> values = new HashMap<String, Integer>();

	public int get(String name) {
		return values.get(name);
	}

	public void set(String name, int value) {
		values.put(name, value);
	}

	public abstract String[] getKeys();

	public void load(ResultSet result) throws SQLException {
		for (String key : getKeys()) {
			set(key, result.getInt(key));
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE).toString();
	}
}
