package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Static;
import de.jardas.drakensang.model.Zauberfertigkeiten;

public class ZauberPanel extends IntegerMapPanel<Zauberfertigkeiten> {
	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected String getGroupKey(String key) {
		return Static.get("CategoryName", key, "ZaAttr", "_Template_Zauber");
	}

	@Override
	protected String getLocalKey(String key) {
		return Static.get("Name", key, "ZaAttr", "_Template_Zauber");
	}

	@Override
	protected String getInfoKey(String key) {
		return Static.get("Description", key, "ZaAttr", "_Template_Zauber");
	}
}
