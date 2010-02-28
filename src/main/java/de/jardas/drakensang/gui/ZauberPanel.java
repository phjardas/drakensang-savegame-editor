package de.jardas.drakensang.gui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SpinnerModel;

import de.jardas.drakensang.shared.db.Static;
import de.jardas.drakensang.shared.db.ZauberfertigkeitDao;
import de.jardas.drakensang.shared.gui.IntegerMapPanel;
import de.jardas.drakensang.shared.gui.TalentSpinnerModel;
import de.jardas.drakensang.shared.model.Character;
import de.jardas.drakensang.shared.model.Zauberfertigkeiten;

public class ZauberPanel extends IntegerMapPanel<Zauberfertigkeiten> {
	private final ChallengeEstimator<IntegerMapPanel<Zauberfertigkeiten>> challengeEstimator = new ChallengeEstimator<IntegerMapPanel<Zauberfertigkeiten>>(
			this) {
		@Override
		protected String[] getAttributes(String key) {
			return ZauberfertigkeitDao.valueOf(key).getAttributes();
		}
	};

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

	public void setCharacter(Character character) {
		challengeEstimator.register(character);
		setValues(character.getZauberfertigkeiten());
	}
}
