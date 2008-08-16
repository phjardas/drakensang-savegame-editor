package de.jardas.drakensang.gui;

import de.jardas.drakensang.model.Attribute;

public class AttributePanel extends IntegerMapPanel<Attribute> {
	@Override
	protected String getInfoKey(String key) {
		return getName(key);
	}
}
