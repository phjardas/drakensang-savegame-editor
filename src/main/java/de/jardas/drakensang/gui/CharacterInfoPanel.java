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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class CharacterInfoPanel extends JPanel {
    private final JLabel ae = new JLabel();
    private final JLabel le = new JLabel();
    private final JLabel fernkampfBasis = new JLabel();
    private final JLabel paradeBasis = new JLabel();
    private final JLabel attackeBasis = new JLabel();
    private final AttributePanel attributesPanel;
    private Character character;
    private EnumComboBox<CharacterSet> appearanceCombo;
    private JLabel pic;

    public CharacterInfoPanel(AttributePanel attributesPanel) {
        this.attributesPanel = attributesPanel;

        setLayout(new GridBagLayout());

        attributesPanel.setBorder(BorderFactory.createTitledBorder(Messages.get(
                    "Attribute")));
        attributesPanel.addChangeListener(new de.jardas.drakensang.gui.IntegerMapPanel.ChangeListener() {
                public void valueChanged(String key, int value) {
                    updateDerivedFields();
                }
            });
    }

    private void update() {
        this.attributesPanel.setValues(getCharacter().getAttribute());
        removeAll();

        int row = 0;
        addArchetypeFields(row++);
        addMagicFields(row++);
        addNumberFields(row++);
        addDerivedFields();

        add(attributesPanel,
            new GridBagConstraints(1, 1, 1, 2, 1, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        add(new JLabel(),
            new GridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        updateDerivedFields();
    }

    private void addDerivedFields() {
        int row = 0;
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Werte"));
        add(panel,
            new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(3, 6, 3, 6), 0, 0));

        addInput(panel, "LE", le, row);

        final JSpinner leBonus = new JSpinner(new SpinnerNumberModel(
                    character.getLebensenergieBonus(), -100, 100, 1));
        leBonus.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setLebensenergieBonus(((Number) leBonus.getValue())
                        .intValue());
                    updateDerivedFields();
                }
            });
        panel.add(leBonus,
            new GridBagConstraints(2, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        addInput(panel, "AE", ae, row);

        final JSpinner aeBonus = new JSpinner(new SpinnerNumberModel(
                    character.getAstralenergieBonus(), -100, 100, 1));
        aeBonus.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setAstralenergieBonus(((Number) aeBonus.getValue())
                        .intValue());
                    updateDerivedFields();
                }
            });
        panel.add(aeBonus,
            new GridBagConstraints(2, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        addInput(panel, "AttackeBasis", attackeBasis, row++);
        addInput(panel, "ParadeBasis", paradeBasis, row++);
        addInput(panel, "Fernkampf-Basis", fernkampfBasis, row++);
    }

    public void updateDerivedFields() {
        ae.setText(String.valueOf(character.getAstralenergie()));
        le.setText(String.valueOf(character.getLebensenergie()));
        attackeBasis.setText(String.valueOf(character.getAttackeBasis()));
        paradeBasis.setText(String.valueOf(character.getParadeBasis()));
        fernkampfBasis.setText(String.valueOf(character.getFernkampfBasis()));
    }

    private void addMagicFields(int panelRow) {
        int row = 0;
        final JPanel magicPanel = new JPanel();
        magicPanel.setLayout(new GridBagLayout());
        magicPanel.setBorder(BorderFactory.createTitledBorder("Magie"));
        add(magicPanel,
            new GridBagConstraints(0, panelRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        final JCheckBox magician = new JCheckBox();
        magician.setSelected(character.isMagician());
        magician.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setMagician(magician.isSelected());
                }
            });
        addInput(magicPanel, "Magician", magician, row++);

        final EnumComboBox<CasterType> casterType = new EnumComboBox<CasterType>(CasterType
                .values(), character.getCasterType()) {
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

        addInput(magicPanel, "CasterType", casterType, row++);

        final EnumComboBox<CasterRace> casterRace = new EnumComboBox<CasterRace>(CasterRace
                .values(), character.getCasterRace()) {
                protected void valueChanged(CasterRace item) {
                    character.setCasterRace(item);
                }

                protected boolean accept(CasterRace item) {
                    return item != CasterRace.dwarf;
                }
            };

        addInput(magicPanel, "CasterRace", casterRace, row++);
    }

    private void addNumberFields(int panelRow) {
        int row = 0;
        final JPanel numbersPanel = new JPanel();
        numbersPanel.setLayout(new GridBagLayout());
        numbersPanel.setBorder(BorderFactory.createTitledBorder("Zahlen"));
        add(numbersPanel,
            new GridBagConstraints(0, panelRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        final JSpinner xp = new JSpinner(new SpinnerNumberModel(
                    character.getAbenteuerpunkte(), 0, 100000, 1));
        xp.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setAbenteuerpunkte(((Number) xp.getValue())
                        .intValue());
                }
            });
        addInput(numbersPanel, "XP", xp, row++);

        final JSpinner up = new JSpinner(new SpinnerNumberModel(
                    character.getSteigerungspunkte(), 0, 100000, 1));
        up.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setSteigerungspunkte(((Number) up.getValue())
                        .intValue());
                }
            });
        addInput(numbersPanel, "UpgradeXP", up, row++);

        if (character.isPlayerCharacter()) {
            final JSpinner money = new JSpinner(new SpinnerNumberModel(
                        character.getMoneyAmount(), 0, 9999999, 1));
            money.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        character.setMoneyAmount(((Number) money.getValue())
                            .intValue());
                    }
                });

            addInput(numbersPanel, "Money", money, row++);
        }
    }

    private void addArchetypeFields(int panelRow) {
        int row = 0;

        final JPanel archetypePanel = new JPanel();
        archetypePanel.setLayout(new GridBagLayout());
        archetypePanel.setBorder(BorderFactory.createTitledBorder("Archetyp"));
        add(archetypePanel,
            new GridBagConstraints(0, panelRow, 1, 1, 0, 0,
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

        if (character.isPlayerCharacter()) {
            pic = new JLabel(createPicture());
            archetypePanel.add(pic,
                new GridBagConstraints(2, 0, 1, 5, 0, 0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(3, 6, 3, 6), 0, 0));
        }

        final EnumComboBox<Culture> culture = new EnumComboBox<Culture>(Culture
                .values(), character.getCulture()) {
                protected void valueChanged(Culture item) {
                    character.setCulture(item);
                }
            };

        addInput(archetypePanel, "Culture", culture, row++);

        final EnumComboBox<Profession> profession = new EnumComboBox<Profession>(Profession
                .values(), character.getProfession()) {
                protected void valueChanged(Profession item) {
                    character.setProfession(item);
                }
            };

        addInput(archetypePanel, "Profession", profession, row++);

        final EnumComboBox<Sex> sex = new EnumComboBox<Sex>(Sex.values(),
                character.getSex()) {
                protected void valueChanged(Sex item) {
                    character.setSex(item);
                    updatePicture();
                    updateAppearances();
                }
            };

        addInput(archetypePanel, "Sex", sex, row++);

        if (character.isPlayerCharacter()) {
            appearanceCombo = new EnumComboBox<CharacterSet>(CharacterSet.values(),
                    character.getCharacterSet()) {
                        protected void valueChanged(CharacterSet item) {
                            character.setCharacterSet(item);
                            updatePicture();
                        }

                        @Override
                        protected boolean accept(CharacterSet item) {
                            return super.accept(item);
                        }

                        @Override
                        protected String toString(CharacterSet item) {
                            return item.getArchetype(character.getSex());
                        }
                    };

            addInput(archetypePanel, "CharacterSet", appearanceCombo, row++);
        }
    }

    private void updateAppearances() {
        if (appearanceCombo != null) {
            appearanceCombo.replaceValues(CharacterSet.values(),
                character.getCharacterSet());
        }
    }

    private void updatePicture() {
        if (pic != null) {
            pic.setIcon(createPicture());
        }
    }

    private ImageIcon createPicture() {
        String url = character.getCharacterSet().getIcon(character.getSex())
            + ".png";

        return new ImageIcon(getClass().getResource(url));
    }

    private void addInput(JComponent parent, String label, JComponent input,
        int row) {
        parent.add(new JLabel(Messages.get(label)),
            new GridBagConstraints(0, row, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(3, 6, 3, 6), 0, 0));

        parent.add(input,
            new GridBagConstraints(1, row, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));
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
