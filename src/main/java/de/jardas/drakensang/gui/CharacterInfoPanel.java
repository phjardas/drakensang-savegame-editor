package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.gui.EnumComboBox;
import de.jardas.drakensang.shared.gui.InfoLabel;
import de.jardas.drakensang.shared.gui.RegeneratingPanel;
import de.jardas.drakensang.shared.gui.character.AttributePanel;
import de.jardas.drakensang.shared.model.CasterRace;
import de.jardas.drakensang.shared.model.CasterType;
import de.jardas.drakensang.shared.model.CharSet;
import de.jardas.drakensang.shared.model.Character;
import de.jardas.drakensang.shared.model.Culture;
import de.jardas.drakensang.shared.model.Face;
import de.jardas.drakensang.shared.model.Hair;
import de.jardas.drakensang.shared.model.Profession;
import de.jardas.drakensang.shared.model.Race;
import de.jardas.drakensang.shared.model.Sex;

public class CharacterInfoPanel extends JPanel {
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

	public CharacterInfoPanel() {
		setLayout(new GridBagLayout());
		attributesPanel.setBorder(BorderFactory.createTitledBorder(Messages
				.get("Attribute")));
	}

	private void update() {
		this.attributesPanel.setValues(getCharacter().getAttribute());
		removeAll();

		int row = 0;
		addArchetypeFields(row++);
		addAppearanceFields(row++);
		addNumberFields(row++);
		addDerivedFields();

		add(attributesPanel, new GridBagConstraints(1, 1, 1, 2, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 6, 3, 6), 0, 0));

		add(new JLabel(), new GridBagConstraints(1, row, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
	}

	private void addDerivedFields() {
		final JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		add(p, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		final JTabbedPane tabs = new JTabbedPane();
		tabs.addTab(Messages.get("LE"), new RegeneratingPanel(null, character
				.getLebensenergie()));
		tabs.addTab(Messages.get("AE"), new RegeneratingPanel(null, character
				.getAstralenergie()));
		tabs.addTab(Messages.get("AU"), new RegeneratingPanel(null, character
				.getAusdauer()));
		// tabs.addTab(Messages.get("KE"), new RegeneratingPanel(null,
		// character.getKarma()));

		p.add(tabs, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(Messages
				.get("BaseValues")));
		p.add(panel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(6, 0, 0, 0), 0, 0));

		int r = 0;
		magieresistenz.setText(String.valueOf(character.getMagieresistenz()));
		addInput(panel, "Magieresistenz", magieresistenz, r++);

		attackeBasis.setText(String.valueOf(character.getAttackeBasis()));
		addInput(panel, "AttackeBasis", attackeBasis, r++);

		paradeBasis.setText(String.valueOf(character.getParadeBasis()));
		addInput(panel, "ParadeBasis", paradeBasis, r++);

		fernkampfBasis.setText(String.valueOf(character.getFernkampfBasis()));
		addInput(panel, "Fernkampf-Basis", fernkampfBasis, r++);
	}

	private void addNumberFields(int panelRow) {
		final JPanel container = new JPanel();
		container.setLayout(new GridBagLayout());
		add(container, new GridBagConstraints(0, panelRow, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 6, 3, 6), 0, 0));

		container.add(createMiscFields(container), new GridBagConstraints(0, 0,
				1, 1, .5, .5, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));

		container.add(createSpeedFields(container), new GridBagConstraints(1,
				0, 1, 1, .5, .5, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));
	}

	private JComponent createMiscFields(JPanel container) {
		int row = 0;
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(Messages
				.get("MiscFigures")));

		final JSpinner level = new JSpinner(new SpinnerNumberModel(character
				.getLevel(), 1, 40, 1));
		level.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setLevel(((Number) level.getValue()).intValue());
			}
		});
		addInput(panel, "Stufe", level, row++);

		final JSpinner xp = new JSpinner(new SpinnerNumberModel(character
				.getAbenteuerpunkte(), 0, 100000, 1));
		xp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setAbenteuerpunkte(((Number) xp.getValue())
						.intValue());
			}
		});
		addInput(panel, "XP", xp, row++);

		final JSpinner up = new JSpinner(new SpinnerNumberModel(character
				.getSteigerungspunkte(), 0, 1, 1));
		up.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setSteigerungspunkte(((Number) up.getValue())
						.intValue());
			}
		});
		addInput(panel, "UpgradeXP", up, row++);

		if (character.isPlayerCharacter()) {
			final JSpinner money = new JSpinner(new SpinnerNumberModel(
					character.getMoneyAmount(), 0, 9999999, 1));
			money.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					character.setMoneyAmount(((Number) money.getValue())
							.intValue());
				}
			});

			addInput(panel, "Money", money, row++);
		}

		return panel;
	}

	private JComponent createSpeedFields(JPanel container) {
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

	private void addAppearanceFields(int panelRow) {
		if (character.isPlayerCharacter()) {
			final JPanel appearancePanel = new JPanel();
			appearancePanel.setLayout(new GridBagLayout());
			appearancePanel.setBorder(BorderFactory.createTitledBorder(Messages
					.get("Appearance")));
			add(appearancePanel, new GridBagConstraints(0, panelRow, 1, 1, 0,
					0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
					new Insets(3, 6, 3, 6), 0, 0));

			int row = 0;

			final EnumComboBox<Sex> sex = new EnumComboBox<Sex>(Sex.values(),
					character.getSex()) {
				protected void valueChanged(Sex item) {
					character.setSex(item);
				}
			};

			addInput(appearancePanel, "Sex", sex, row++);

			final EnumComboBox<Hair> hairCombo = new EnumComboBox<Hair>(Hair
					.values(character.getSex(), character.getRace()), character
					.getHair()) {
				protected void valueChanged(Hair item) {
					character.setHair(item);
				}

				@Override
				protected String getLabel(String key) {
					return key;
				}
			};

			addInput(appearancePanel, "SelectHair", hairCombo, row++);

			final EnumComboBox<Face> faceCombo = new EnumComboBox<Face>(Face
					.values(character.getSex(), character.getRace()), character
					.getFace()) {
				protected void valueChanged(Face item) {
					character.setFace(item);
				}

				@Override
				protected String getLabel(String key) {
					return key;
				}
			};

			addInput(appearancePanel, "SelectFace", faceCombo, row++);

			final EnumComboBox<CharSet> bodyCombo = new EnumComboBox<CharSet>(
					CharSet.values(character.getSex(), character.getRace()),
					character.getCharSet()) {
				protected void valueChanged(CharSet item) {
					character.setCharSet(item);
				}

				@Override
				protected String getLabel(String key) {
					return key;
				}
			};

			addInput(appearancePanel, "CharacterSet", bodyCombo, row++);
		}
	}

	private void addArchetypeFields(int panelRow) {
		int row = 0;

		final JPanel archetypePanel = new JPanel();
		archetypePanel.setLayout(new GridBagLayout());
		archetypePanel.setBorder(BorderFactory.createTitledBorder(Messages
				.get("Archetype")));
		add(archetypePanel, new GridBagConstraints(0, panelRow, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(3, 6, 3, 6), 0, 0));

		if (character.isPlayerCharacter()) {
			final JTextField name = new JTextField(character.getLookAtText());
			name.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					character.setLookAtText(name.getText());
				}
			});

			addInput(archetypePanel, "Name", name, row++);
		}

		final EnumComboBox<Race> race = new EnumComboBox<Race>(Race.values(),
				character.getRace()) {
			protected void valueChanged(Race item) {
				character.setRace(item);
			}
		};

		addInput(archetypePanel, "Race", race, row++);

		final EnumComboBox<Culture> culture = new EnumComboBox<Culture>(Culture
				.values(), character.getCulture()) {
			protected void valueChanged(Culture item) {
				character.setCulture(item);
			}
		};

		addInput(archetypePanel, "Culture", culture, row++);

		final EnumComboBox<Profession> profession = new EnumComboBox<Profession>(
				Profession.values(), character.getProfession()) {
			protected void valueChanged(Profession item) {
				character.setProfession(item);
			}
		};

		addInput(archetypePanel, "Profession", profession, row++);

		final JCheckBox magician = new JCheckBox();
		magician.setSelected(character.isMagician());
		magician.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setMagician(magician.isSelected());
			}
		});
		addInput(archetypePanel, "Magician", magician, row++);

		final EnumComboBox<CasterType> casterType = new EnumComboBox<CasterType>(
				CasterType.values(), character.getCasterType()) {
			protected void valueChanged(CasterType item) {
				character.setCasterType(item);
			}

			protected String toString(CasterType item) {
				if (item == CasterType.none) {
					return "CasterType.none";
				}

				return super.toString(item);
			}
		};

		addInput(archetypePanel, "CasterType", casterType, row++);

		final EnumComboBox<CasterRace> casterRace = new EnumComboBox<CasterRace>(
				CasterRace.values(), character.getCasterRace()) {
			protected void valueChanged(CasterRace item) {
				character.setCasterRace(item);
			}

			protected boolean accept(CasterRace item) {
				return item != CasterRace.dwarf;
			}
		};

		addInput(archetypePanel, "CasterRace", casterRace, row++);
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
