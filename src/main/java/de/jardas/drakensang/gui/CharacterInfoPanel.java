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

import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.Culture;
import de.jardas.drakensang.model.Profession;
import de.jardas.drakensang.model.Race;
import de.jardas.drakensang.model.Sex;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

		final JComboBox race = new JComboBox(Race.values());
		race.setSelectedIndex(character.getRace().ordinal());
		race.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					character.setRace((Race) race.getSelectedItem());
				}
			}
		});

		final JComboBox culture = new JComboBox(Culture.values());
		culture.setSelectedIndex(character.getCulture().ordinal());
		culture.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					character.setCulture((Culture) culture.getSelectedItem());
				}
			}
		});

		final JComboBox profession = new JComboBox(Profession.values());
		profession.setSelectedIndex(character.getProfession().ordinal());
		profession.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					character.setProfession((Profession) profession
							.getSelectedItem());
				}
			}
		});

		final JComboBox sex = new JComboBox(Sex.values());
		sex.setSelectedIndex(character.getSex().ordinal());
		sex.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					character.setSex((Sex) sex.getSelectedItem());
				}
			}
		});

		final JSpinner xp = new JSpinner(new SpinnerNumberModel(character
				.getAbenteuerpunkte(), 0, 100000, 1));
		xp.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				character.setAbenteuerpunkte(((Number) xp.getValue())
						.intValue());
			}
		});

		final JSpinner up = new JSpinner(new SpinnerNumberModel(character
				.getSteigerungspunkte(), 0, 100000, 1));
		up.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				character.setSteigerungspunkte(((Number) up.getValue())
						.intValue());
			}
		});

		final JTextField name = new JTextField(character.getLookAtText());
		name.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				character.setLookAtText(name.getText());
			}
		});

		final JCheckBox magician = new JCheckBox();
		magician.setSelected(character.isMagician());
		magician.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				character.setMagician(magician.isSelected());
			}
		});

		final JSpinner money = new JSpinner(new SpinnerNumberModel(character
				.getMoneyAmount(), 0, 99000000, 1));
		money.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				character
						.setMoneyAmount(((Number) money.getValue()).intValue());
			}
		});

		if (character.isPlayerCharacter()) {
			addInput("Name", name);
		}

		addInput("Geschlecht", sex);
		addInput("Rasse", race);
		addInput("Kultur", culture);
		addInput("Beruf", profession);
		addInput("Magier", magician);
		addInput("Abenteuerpunkte", xp);
		addInput("Steigerungspunkte", up);

		if (character.isPlayerCharacter()) {
			addInput("Geld", money);
		}

		add(new JLabel(), new GridBagConstraints(2, row, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		repaint();
	}

	private void addInput(final String label, final JComponent input) {
		Insets insets = new Insets(3, 6, 3, 6);
		add(new JLabel(label), new GridBagConstraints(0, row, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));

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
