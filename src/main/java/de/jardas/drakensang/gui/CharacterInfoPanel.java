package de.jardas.drakensang.gui;

import de.jardas.drakensang.shared.db.CharSetDao;
import de.jardas.drakensang.shared.db.CultureDao;
import de.jardas.drakensang.shared.db.FaceDao;
import de.jardas.drakensang.shared.db.HairDao;
import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.db.ProfessionDao;
import de.jardas.drakensang.shared.db.RaceDao;
import de.jardas.drakensang.shared.gui.EnumComboBox;
import de.jardas.drakensang.shared.gui.IdentifiedComboBox;
import de.jardas.drakensang.shared.gui.InfoLabel;
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
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
    private Character character;

    public CharacterInfoPanel() {
        setLayout(new GridBagLayout());
    }

    private void update() {
        removeAll();

        int row = 0;
        addArchetypeFields(row++);
        addAppearanceFields(row++);

        add(createMiscFields(),
            new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));

        add(new JLabel(),
            new GridBagConstraints(1, row, 2, 1, 1, 1, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    private void addAppearanceFields(int panelRow) {
        if (character.isPlayerCharacter()) {
            final JPanel appearancePanel = new JPanel();
            appearancePanel.setLayout(new GridBagLayout());
            appearancePanel.setBorder(BorderFactory.createTitledBorder(Messages.get("Appearance")));
            add(appearancePanel,
                new GridBagConstraints(0, panelRow, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.BOTH, new Insets(3, 6, 3, 6), 0, 0));

            int row = 0;

            final EnumComboBox<Sex> sex =
                new EnumComboBox<Sex>(Sex.values(), character.getSex()) {
                    protected void valueChanged(Sex item) {
                        character.setSex(item);
                    }
                };

            addInput(appearancePanel, "Sex", sex, row++);

            final IdentifiedComboBox<Hair> hairCombo =
                new IdentifiedComboBox<Hair>(HairDao.values(character.getSex(), character.getRace()),
                    character.getHair()) {
                    protected void valueChanged(Hair item) {
                        character.setHair(item);
                    }

                    @Override
                    protected String getLabel(String key) {
                        return key;
                    }
                };

            addInput(appearancePanel, "SelectHair", hairCombo, row++);

            final IdentifiedComboBox<Face> faceCombo =
                new IdentifiedComboBox<Face>(FaceDao.values(character.getSex(), character.getRace()),
                    character.getFace()) {
                    protected void valueChanged(Face item) {
                        character.setFace(item);
                    }

                    @Override
                    protected String getLabel(String key) {
                        return key;
                    }
                };

            addInput(appearancePanel, "SelectFace", faceCombo, row++);

            final IdentifiedComboBox<CharSet> bodyCombo =
                new IdentifiedComboBox<CharSet>(CharSetDao.values(character.getSex(),
                        character.getRace()), character.getCharSet()) {
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
        archetypePanel.setBorder(BorderFactory.createTitledBorder(Messages.get("Archetype")));
        add(archetypePanel,
            new GridBagConstraints(0, panelRow, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(3, 6, 3, 6), 0, 0));

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

        final IdentifiedComboBox<Race> race =
            new IdentifiedComboBox<Race>(RaceDao.values(), character.getRace()) {
                protected void valueChanged(Race item) {
                    character.setRace(item);
                }
            };

        addInput(archetypePanel, "Race", race, row++);

        final IdentifiedComboBox<Culture> culture =
            new IdentifiedComboBox<Culture>(CultureDao.values(), character.getCulture()) {
                protected void valueChanged(Culture item) {
                    character.setCulture(item);
                }
            };

        addInput(archetypePanel, "Culture", culture, row++);

        final IdentifiedComboBox<Profession> profession =
            new IdentifiedComboBox<Profession>(ProfessionDao.values(), character.getProfession()) {
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

        final EnumComboBox<CasterType> casterType =
            new EnumComboBox<CasterType>(CasterType.values(), character.getCasterType()) {
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

        final EnumComboBox<CasterRace> casterRace =
            new EnumComboBox<CasterRace>(CasterRace.values(), character.getCasterRace()) {
                protected void valueChanged(CasterRace item) {
                    character.setCasterRace(item);
                }

                protected boolean accept(CasterRace item) {
                    return item != CasterRace.dwarf;
                }
            };

        addInput(archetypePanel, "CasterRace", casterRace, row++);
    }

    private JComponent createMiscFields() {
        int row = 0;
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(Messages.get("MiscFigures")));

        final JSpinner level = new JSpinner(new SpinnerNumberModel(character.getLevel(), 1, 40, 1));
        level.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setLevel(((Number) level.getValue()).intValue());
                }
            });
        addInput(panel, "Stufe", level, row++);

        final JSpinner xp =
            new JSpinner(new SpinnerNumberModel(character.getAbenteuerpunkte(), 0, 9999999, 1));
        xp.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setAbenteuerpunkte(((Number) xp.getValue()).intValue());
                }
            });
        addInput(panel, "XP", xp, row++);

        final JSpinner up =
            new JSpinner(new SpinnerNumberModel(character.getSteigerungspunkte(), 0, 9999999, 1));
        up.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setSteigerungspunkte(((Number) up.getValue()).intValue());
                }
            });
        addInput(panel, "UpgradeXP", up, row++);

        if (character.isPlayerCharacter()) {
            final JSpinner money =
                new JSpinner(new SpinnerNumberModel(character.getMoneyAmount(), 0, 9999999, 1));
            money.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        character.setMoneyAmount(((Number) money.getValue()).intValue());
                    }
                });

            addInput(panel, "Money", money, row++);
        }

        return panel;
    }

    private void addInput(JComponent parent, String label, JComponent input, int row) {
        addInput(parent, label, null, input, row);
    }

    private void addInput(JComponent parent, String label, String infoLabel, JComponent input,
        int row) {
        parent.add(new InfoLabel(label, infoLabel),
            new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));

        if (input != null) {
            parent.add(input,
                new GridBagConstraints(1, row, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));
        }
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
