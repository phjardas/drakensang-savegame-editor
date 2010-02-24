package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.db.Static;
import de.jardas.drakensang.shared.game.Challenge;
import de.jardas.drakensang.shared.game.Challenge.ChallengeProbability;
import de.jardas.drakensang.shared.gui.InfoLabel;
import de.jardas.drakensang.shared.gui.IntegerMapPanel;
import de.jardas.drakensang.shared.gui.TalentSpinnerModel;
import de.jardas.drakensang.shared.model.Character;
import de.jardas.drakensang.shared.model.Talente;

public class TalentePanel extends IntegerMapPanel<Talente> {
	private static final NumberFormat CHANCE_FORMAT = NumberFormat
			.getPercentInstance();
	private Character character;
	private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			for (Map.Entry<String, JComponent> entry : getSpecials().entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}

				final String key = entry.getKey();
				final JLabel label = (JLabel) entry.getValue();
				label.setText(createAttributesLabel(key));
			}
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
		return Static.get("TaCategory", key, "TaAttr", "_Template_Talent");
	}

	@Override
	protected String getLocalKey(String key) {
		return Talente.getNameKey(key);
	}

	@Override
	protected JComponent createSpecial(String key) {
		return new JLabel(createAttributesLabel(key));
	}

	@Override
	protected void handleChange(String key, int value) {
		super.handleChange(key, value);

		final JComponent label = getSpecials().get(key);
		if (label != null && label instanceof JLabel) {
			((JLabel) label).setText(createAttributesLabel(key));
		}
	}

	private String createAttributesLabel(String key) {
		final String[] attrs = Talente.getAttributes(key);

		if (attrs == null) {
			return "";
		}

		final int ta = getValues().get(key);

		if (ta >= 0) {
			final StringBuilder attBuilder = new StringBuilder();

			for (String attr : attrs) {
				if (attBuilder.length() > 0) {
					attBuilder.append(", ");
				}

				attBuilder.append(Messages.get(attr + "Short"));
				attBuilder.append("=");
				attBuilder.append(getCharacter().getAttribute().get(attr));
			}

			StringBuilder buf = new StringBuilder();
			final int at1 = getCharacter().getAttribute().get(attrs[0]);
			final int at2 = getCharacter().getAttribute().get(attrs[1]);
			final int at3 = getCharacter().getAttribute().get(attrs[2]);
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
	protected String getInfoKey(String key) {
		return Static.get("Description", key, "TaAttr", "_Template_Talent");
	}

	@Override
	protected SpinnerModel createSpinnerModel(String key, int value) {
		return TalentSpinnerModel.create(key, value, 1000);
	}

	public Character getCharacter() {
		return this.character;
	}

	public void setCharacter(Character character) {
		if (character == this.character) {
			return;
		}

		if (this.character != null) {
			this.character.removePropertyChangeListener(propertyChangeListener);
		}

		this.character = character;

		if (this.character != null) {
			character.addPropertyChangeListener(propertyChangeListener,
					"attributes.*", "advantages.*");
		}

		setValues(character.getTalente());
	}
}
