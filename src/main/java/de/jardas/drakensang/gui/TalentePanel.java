package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.MissingResourceException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.dao.Static;
import de.jardas.drakensang.model.Talente;

public class TalentePanel extends IntegerMapPanel<Talente> {
	@Override
	protected boolean isVisible(String key) {
		return !key.startsWith("TaATAdjust");
	}

	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected String getGroupKey(String key) {
		return Static.get("TaCategory", key, "TaAttr", "_Template_Talent");
	}

	@Override
	protected String getLocalKey(String key) {
		return Static.get("Name", key, "TaAttr", "_Template_Talent");
	}

	@Override
	protected JComponent createField(final String key, int value) {
		JComponent taField = super.createField(key, value);
		String adjustKey = getAdjustAttributeKey(key);

		if (getValues().contains(adjustKey)) {
			JPanel wrapper = new JPanel();
			wrapper.setLayout(new GridBagLayout());
			wrapper.add(taField, new GridBagConstraints(0, 0, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));

			int adjustValue = getValues().get(adjustKey);

			if (adjustValue < -100) {
				adjustValue = 0;
			}

			final JSpinner adjust = new JSpinner(new SpinnerNumberModel(
					adjustValue, -100, 100, 1));
			adjust.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					getValues().set(key,
							((Number) adjust.getValue()).intValue());
				}
			});
			wrapper.add(adjust, new GridBagConstraints(1, 0, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(0, 6, 0, 0), 0, 0));

			wrapper.add(new InfoLabel(null, "talent.adjustinfo"),
					new GridBagConstraints(2, 0, 1, 1, 0, 0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE,
							new Insets(0, 6, 0, 0), 0, 0));

			return wrapper;
		}

		return taField;
	}

	private String getAdjustAttributeKey(final String key) {
		try {
			return Static.get("TaATAdjustAttr", key, "TaAttr", "_Template_Talent");
		} catch (MissingResourceException e) {
			return null;
		}
	}

	@Override
	protected String getInfoKey(String key) {
		return Static.get("Description", key, "TaAttr", "_Template_Talent");
	}
}
