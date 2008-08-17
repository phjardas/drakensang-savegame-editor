package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Static;
import de.jardas.drakensang.model.Attribute;

public class AttributePanel extends IntegerMapPanel<Attribute> {
	@Override
	protected String getInfoKey(String key) {
		return Static.get("Description", key, "Id", "_Template_CharAttr");
	}
}
