package de.jardas.drakensang.gui;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;

import de.jardas.drakensang.shared.db.Static;
import de.jardas.drakensang.shared.gui.IntegerMapPanel;
import de.jardas.drakensang.shared.gui.TalentSpinnerModel;
import de.jardas.drakensang.shared.model.Sonderfertigkeiten;

public class SonderfertigkeitenPanel extends
		IntegerMapPanel<SonderfertigkeitenIntegerMap> {
	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected String getGroupKey(String key) {
		return Static.get("AttrCategory", key, "AtAttr", "_Template_Attacks");
	}

	@Override
	protected String getLocalKey(String key) {
		return Static.get("Name", key, "AtAttr", "_Template_Attacks");
	}

	@Override
	protected String getInfoKey(String key) {
		return Static.get("Description", key, "AtAttr", "_Template_Attacks");
	}

	@Override
	protected JComponent createField(final String key, int value) {
		final JCheckBox box = new JCheckBox();
		box.setSelected(value >= 0);
		box.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				getValues().set(key, box.isSelected() ? 1 : -500);
			}
		});

		return box;
	}

	@Override
	protected SpinnerModel createSpinnerModel(String key, int value) {
		return TalentSpinnerModel.create(key, value, 1000);
	}

	public void setValues(Sonderfertigkeiten values) {
		super.setValues(new SonderfertigkeitenIntegerMap(values));
	}
}
