package de.jardas.drakensang.gui;

import javax.swing.SpinnerModel;

import de.jardas.drakensang.shared.db.Static;
import de.jardas.drakensang.shared.gui.IntegerMapPanel;
import de.jardas.drakensang.shared.gui.TalentSpinnerModel;
import de.jardas.drakensang.shared.model.Zauberfertigkeiten;

public class ZauberPanel extends IntegerMapPanel<Zauberfertigkeiten> {
	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected SpinnerModel createSpinnerModel(String key, int value) {
		return TalentSpinnerModel.create(key, value, 1000);
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
