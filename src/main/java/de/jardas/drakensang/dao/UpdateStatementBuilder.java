package de.jardas.drakensang.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UpdateStatementBuilder {
	private final String table;
	private final String where;
	private final StringBuffer buffer = new StringBuffer();
	private final List<Parameter> parameters = new ArrayList<Parameter>();

	public UpdateStatementBuilder(String table, String where) {
		this.table = table;
		this.where = where;
	}

	public void append(String sql) {
		if (buffer.length() > 0) {
			buffer.append(",");
		}

		buffer.append(" ").append(sql);
	}

	public void append(String sql, ParameterType type, Object value) {
		append(sql);
		parameters.add(new Parameter(type, value));
	}

	public void append(String sql, int value) {
		append(sql, ParameterType.Int, value);
	}

	public void append(String sql, String value) {
		append(sql, ParameterType.String, value);
	}

	public PreparedStatement createStatement(Connection connection)
			throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(table).append(" set");
		sql.append(buffer.toString());

		if (where != null) {
			sql.append(" where ").append(where);
		}

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		int i = 1;
		for (Parameter parameter : parameters) {
			parameter.getType().set(stmt, i++, parameter.getValue());
		}

		return stmt;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE).toString();
	}

	private static class Parameter {
		private final ParameterType type;
		private final Object value;

		public Parameter(ParameterType type, Object value) {
			super();
			this.type = type;
			this.value = value;
		}

		public ParameterType getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue() + " (" + getType() + ")";
		}
	}

	public static enum ParameterType {
		Int {
			@Override
			public void set(PreparedStatement stmt, int index, Object value)
					throws SQLException {
				stmt.setInt(index, (Integer) value);
			}
		},
		String {
			@Override
			public void set(PreparedStatement stmt, int index, Object value)
					throws SQLException {
				stmt.setString(index, (String) value);
			}
		};

		public abstract void set(PreparedStatement stmt, int index, Object value)
				throws SQLException;
	}
}
