package de.jardas.drakensang.gui;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;

import de.jardas.drakensang.shared.db.SonderfertigkeitDao;
import de.jardas.drakensang.shared.gui.InfoLabel;
import de.jardas.drakensang.shared.gui.IntegerMapPanel;
import de.jardas.drakensang.shared.gui.TalentSpinnerModel;
import de.jardas.drakensang.shared.model.Sonderfertigkeit;
import de.jardas.drakensang.shared.model.Sonderfertigkeiten;

public class SonderfertigkeitenPanel extends
		IntegerMapPanel<SonderfertigkeitenIntegerMap> {
	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected String getGroupKey(String key) {
		return SonderfertigkeitDao.valueOf(key).getCategoryKey();
	}

	@Override
	protected InfoLabel createLabel(String key) {
		final Sonderfertigkeit sf = SonderfertigkeitDao.valueOf(key);
		return new InfoLabel(sf.getName(), sf.getInfo(), false);
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
