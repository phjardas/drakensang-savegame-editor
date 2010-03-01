package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.gui.InfoLabel;
import de.jardas.drakensang.shared.gui.RegeneratingPanel;
import de.jardas.drakensang.shared.gui.character.AttributePanel;
import de.jardas.drakensang.shared.model.Character;

public class FiguresPanel extends JPanel {
	private final JLabel fernkampfBasis = new JLabel();
	private final JLabel paradeBasis = new JLabel();
	private final JLabel attackeBasis = new JLabel();
	private final JLabel magieresistenz = new JLabel();
	private final PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			final String property = evt.getPropertyName();
			if ("attackeBasis.value".equals(property)) {
				attackeBasis.setText(String.valueOf(character.getAttackeBasis()
						.getValue()));
			} else if ("paradeBasis.value".equals(property)) {
				paradeBasis.setText(String.valueOf(character.getParadeBasis()
						.getValue()));
			} else if ("fernkampfBasis.value".equals(property)) {
				fernkampfBasis.setText(String.valueOf(character
						.getFernkampfBasis().getValue()));
			} else if ("magieresistenz.value".equals(property)) {
				magieresistenz.setText(String.valueOf(character
						.getMagieresistenz().getValue()));
			}
		}
	};
	private final AttributePanel attributesPanel = new AttributePanel();
	private Character character;

	public FiguresPanel() {
		setLayout(new GridBagLayout());
		attributesPanel.setBorder(BorderFactory.createTitledBorder(Messages
				.get("Attribute")));
	}

	private void update() {
		this.attributesPanel.setValues(getCharacter().getAttribute());
		removeAll();

		add(attributesPanel, new GridBagConstraints(0, 0, 1, 2, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 6, 3, 6), 0, 0));

		add(createDerivedFieldsPanel(), new GridBagConstraints(1, 0, 1, 1, 0,
				0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(3, 6, 3, 6), 0, 0));

		add(createSpeedFields(), new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(3, 6, 3, 6), 0, 0));

		add(new RegeneratingPanel("LE", character.getLebensenergie()),
				new GridBagConstraints(0, 2, 1, 1, 0, 0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6),
						0, 0));

		add(new RegeneratingPanel("AU", character.getAusdauer()),
				new GridBagConstraints(1, 2, 1, 1, 0, 0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6),
						0, 0));

		add(new RegeneratingPanel("AE", character.getAstralenergie()),
				new GridBagConstraints(2, 2, 1, 1, 0, 0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6),
						0, 0));

		add(new JLabel(), new GridBagConstraints(3, 3, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
	}

	private JPanel createDerivedFieldsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(Messages
				.get("BaseValues")));

		int r = 0;
		magieresistenz.setText(String.valueOf(character.getMagieresistenz()));
		addInput(panel, "Magieresistenz", magieresistenz, r++);

		attackeBasis.setText(String.valueOf(character.getAttackeBasis()));
		addInput(panel, "AttackeBasis", attackeBasis, r++);

		paradeBasis.setText(String.valueOf(character.getParadeBasis()));
		addInput(panel, "ParadeBasis", paradeBasis, r++);

		fernkampfBasis.setText(String.valueOf(character.getFernkampfBasis()));
		addInput(panel, "Fernkampf-Basis", fernkampfBasis, r++);

		return panel;
	}

	private JComponent createSpeedFields() {
		int row = 0;
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(Messages
				.get("SpeedFields")));

		final JSpinner sneak = new JSpinner(new SpinnerNumberModel(character
				.getSneakSpeed(), 0, 100, .1));
		sneak.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setSneakSpeed(((Number) sneak.getValue())
						.doubleValue());
			}
		});
		addInput(panel, "SneakSpeed", sneak, row++);

		final JSpinner walk = new JSpinner(new SpinnerNumberModel(character
				.getWalkSpeed(), 0, 100, .1));
		walk.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character
						.setWalkSpeed(((Number) walk.getValue()).doubleValue());
			}
		});
		addInput(panel, "WalkSpeed", walk, row++);

		final JSpinner run = new JSpinner(new SpinnerNumberModel(character
				.getRunSpeed(), 0, 100, .1));
		run.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setRunSpeed(((Number) run.getValue()).doubleValue());
				character.setCurrentSpeed(character.getRunSpeed());
				character.setMaxVelocity(character.getRunSpeed());
			}
		});
		addInput(panel, "RunSpeed", run, row++);

		return panel;
	}

	private void addInput(JComponent parent, String label, JComponent input,
			int row) {
		addInput(parent, label, null, input, row);
	}

	private void addInput(JComponent parent, String label, String infoLabel,
			JComponent input, int row) {
		parent.add(new InfoLabel(label, infoLabel), new GridBagConstraints(0,
				row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));

		if (input != null) {
			parent.add(input,
					new GridBagConstraints(1, row, 1, 1, 0, 0,
							GridBagConstraints.NORTHWEST,
							GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3,
									6), 0, 0));
		}
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
					"attackeBasis.value", "paradeBasis.value",
					"fernkampfBasis.value", "magieresistenz.value");
		}

		update();
	}
}
