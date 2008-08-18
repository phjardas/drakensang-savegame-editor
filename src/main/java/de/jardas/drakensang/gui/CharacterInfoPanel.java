/*
 * CharacterInfoPanel.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.CasterRace;
import de.jardas.drakensang.model.CasterType;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.CharacterSet;
import de.jardas.drakensang.model.Culture;
import de.jardas.drakensang.model.Profession;
import de.jardas.drakensang.model.Race;
import de.jardas.drakensang.model.Sex;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CharacterInfoPanel extends JPanel {
	private Character character;
	private int row;

	public CharacterInfoPanel() {
		setLayout(new GridBagLayout());
	}

	private void update() {
		removeAll();
		row = 0;

		final JLabel pic = character.isPlayerCharacter() ? new JLabel(
				createPicture()) : null;

		final JComboBox race = EnumComboBoxModel.createComboBox(Race.values(),
				character.getRace(), new EnumComboBoxModel.Listener<Race>() {
					public void valueChanged(Race item) {
						character.setRace(item);
					}
				});

		final JComboBox culture = EnumComboBoxModel.createComboBox(Culture
				.values(), character.getCulture(),
				new EnumComboBoxModel.Listener<Culture>() {
					public void valueChanged(Culture item) {
						character.setCulture(item);
					}
				});

		final JComboBox profession = EnumComboBoxModel.createComboBox(
				Profession.values(), character.getProfession(),
				new EnumComboBoxModel.Listener<Profession>() {
					public void valueChanged(Profession item) {
						character.setProfession(item);
					}
				});

		final JComboBox sex = EnumComboBoxModel.createComboBox(Sex.values(),
				character.getSex(), new EnumComboBoxModel.Listener<Sex>() {
					public void valueChanged(Sex item) {
						character.setSex(item);
						updatePicture(pic);
					}
				});

		final JSpinner xp = new JSpinner(new SpinnerNumberModel(character
				.getAbenteuerpunkte(), 0, 100000, 1));
		xp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setAbenteuerpunkte(((Number) xp.getValue())
						.intValue());
			}
		});

		final JSpinner up = new JSpinner(new SpinnerNumberModel(character
				.getSteigerungspunkte(), 0, 100000, 1));
		up.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setSteigerungspunkte(((Number) up.getValue())
						.intValue());
			}
		});

		final JCheckBox magician = new JCheckBox();
		magician.setSelected(character.isMagician());
		magician.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				character.setMagician(magician.isSelected());
			}
		});

		final JComboBox casterType = EnumComboBoxModel.createComboBox(CasterType.values(),
				character.getCasterType(), new EnumComboBoxModel.Listener<CasterType>() {
					public void valueChanged(CasterType item) {
						character.setCasterType(item);
					}
				});
		
		final JComboBox casterRace = EnumComboBoxModel.createComboBox(CasterRace.values(),
				character.getCasterRace(), new EnumComboBoxModel.Listener<CasterRace>() {
			public void valueChanged(CasterRace item) {
				character.setCasterRace(item);
			}
		});

		if (character.isPlayerCharacter()) {
			final JTextField name = new JTextField(character.getLookAtText());
			name.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					character.setLookAtText(name.getText());
				}
			});

			addInput("Name", name);

			add(pic, new GridBagConstraints(2, row - 1, 1, 5, 0, 0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(3, 6, 3, 6), 0, 0));
		}

		addInput("Sex", sex);
		addInput("Race", race);
		addInput("Culture", culture);
		addInput("Profession", profession);
		addInput("Magician", magician);
		addInput("CasterType", casterType);
		addInput("CasterRace", casterRace);
		addInput("XP", xp);
		addInput("UpgradeXP", up);

		if (character.isPlayerCharacter()) {
			final JSpinner money = new JSpinner(new SpinnerNumberModel(
					character.getMoneyAmount(), 0, 9999999, 1));
			money.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					character.setMoneyAmount(((Number) money.getValue())
							.intValue());
				}
			});

			final JComboBox appearance = EnumComboBoxModel.createComboBox(
					CharacterSet.values(), character.getCharacterSet(),
					new EnumComboBoxModel.Listener<CharacterSet>() {
						public void valueChanged(CharacterSet item) {
							character.setCharacterSet(item);
							updatePicture(pic);
						}

						@Override
						public boolean accept(CharacterSet item) {
							return super.accept(item);
						}

						@Override
						public String toString(CharacterSet item) {
							return item.getArchetype(character.getSex());
						}
					});

			addInput("Geld", money);
			addInput("CharacterSet", appearance);
		}

		add(new JLabel(), new GridBagConstraints(2, row, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		repaint();
	}

	private void updatePicture(final JLabel pic) {
		if (pic != null) {
			pic.setIcon(createPicture());
		}
	}

	private ImageIcon createPicture() {
		String url = character.getCharacterSet().getIcon(character.getSex())
				+ ".png";
		return new ImageIcon(getClass().getResource(url));
	}

	private void addInput(final String label, final JComponent input) {
		Insets insets = new Insets(3, 6, 3, 6);
		add(new JLabel(Messages.get(label)), new GridBagConstraints(0, row, 1,
				1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				insets, 0, 0));

		add(input, new GridBagConstraints(1, row, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets,
				0, 0));

		row++;
	}

	public Character getCharacter() {
		return this.character;
	}

	public void setCharacter(Character character) {
		if (character == this.character) {
			return;
		}

		this.character = character;
		update();
	}
}
