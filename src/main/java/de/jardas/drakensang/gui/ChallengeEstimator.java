package de.jardas.drakensang.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.game.Challenge;
import de.jardas.drakensang.shared.game.Challenge.ChallengeProbability;
import de.jardas.drakensang.shared.gui.IntegerMapPanel;
import de.jardas.drakensang.shared.model.Character;
import de.jardas.drakensang.shared.model.IntegerMap;

public abstract class ChallengeEstimator<P extends IntegerMapPanel<? extends IntegerMap>>
		implements PropertyChangeListener {
	private static final NumberFormat CHANCE_FORMAT = NumberFormat
			.getPercentInstance();
	private final P panel;
	private Character character;

	public ChallengeEstimator(P panel) {
		super();
		this.panel = panel;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		for (Map.Entry<String, JComponent> entry : panel.getSpecials()
				.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}

			final String key = entry.getKey();
			final JLabel label = (JLabel) entry.getValue();
			label.setText(getChallengeEstimation(key));
		}
	}

	public String getChallengeEstimation(String key) {
		final String[] attrs = getAttributes(key);

		if (attrs == null) {
			return "";
		}

		final int ta = panel.getValues().get(key);

		if (ta >= 0) {
			final StringBuilder attBuilder = new StringBuilder();

			for (String attr : attrs) {
				if (attBuilder.length() > 0) {
					attBuilder.append(", ");
				}

				attBuilder.append(Messages.get(attr + "Short"));
				attBuilder.append("=");
				attBuilder.append(character.getAttribute().get(attr));
			}

			StringBuilder buf = new StringBuilder();
			final int at1 = character.getAttribute().get(attrs[0]);
			final int at2 = character.getAttribute().get(attrs[1]);
			final int at3 = character.getAttribute().get(attrs[2]);
			final ChallengeProbability chances = Challenge.calculateChances(
					at1, at2, at3, ta, 0);
			buf.append(CHANCE_FORMAT.format(chances.getProbabilty()));

			buf.append(" (");
			buf.append(attBuilder);
			buf.append(")");

			return buf.toString();
		} else {
			return "";
		}
	}

	protected abstract String[] getAttributes(String key);

	public void register(Character character) {
		if (this.character != null) {
			unregister();
		}

		this.character = character;
		character.addPropertyChangeListener(this, "attributes.*",
				"advantages.*");
	}

	public void unregister() {
		if (character != null) {
			character.removePropertyChangeListener(this);
		}
	}

}
