package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.MissingResourceException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import de.jardas.drakensang.shared.db.Static;
import de.jardas.drakensang.shared.db.TalentDao;
import de.jardas.drakensang.shared.gui.InfoLabel;
import de.jardas.drakensang.shared.gui.IntegerMapPanel;
import de.jardas.drakensang.shared.gui.TalentSpinnerModel;
import de.jardas.drakensang.shared.model.Character;
import de.jardas.drakensang.shared.model.Talente;

public class TalentePanel extends IntegerMapPanel<Talente> {
	private final ChallengeEstimator<IntegerMapPanel<Talente>> challengeEstimator = new ChallengeEstimator<IntegerMapPanel<Talente>>(
			this) {
		@Override
		protected String[] getAttributes(String key) {
			return TalentDao.valueOf(key).getAttributes();
		}
	};

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
		return TalentDao.valueOf(key).getCategoryKey();
	}

	@Override
	protected String getLocalKey(String key) {
		return TalentDao.valueOf(key).getNameKey();
	}

	@Override
	protected String getInfoKey(String key) {
		return TalentDao.valueOf(key).getDescriptionKey();
	}

	@Override
	protected JComponent createSpecial(String key) {
		return new JLabel(challengeEstimator.getChallengeEstimation(key));
	}

	@Override
	protected void handleChange(String key, int value) {
		super.handleChange(key, value);

		final JComponent label = getSpecials().get(key);
		if (label != null && label instanceof JLabel) {
			((JLabel) label).setText(challengeEstimator
					.getChallengeEstimation(key));
		}
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
			adjust.addChangeListener(new javax.swing.event.ChangeListener() {
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
			return Static.get("TaATAdjustAttr", key, "TaAttr",
					"_Template_Talent");
		} catch (MissingResourceException e) {
			return null;
		}
	}

	@Override
	protected SpinnerModel createSpinnerModel(String key, int value) {
		return TalentSpinnerModel.create(key, value, 1000);
	}

	public void setCharacter(Character character) {
		challengeEstimator.register(character);
		setValues(character.getTalente());
	}
}
