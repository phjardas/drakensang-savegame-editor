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

import java.text.MessageFormat;

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
    private final JLabel aeMax = new JLabel();
    private final JLabel leMax = new JLabel();
    private final JLabel fernkampfBasis = new JLabel();
    private final JLabel paradeBasis = new JLabel();
    private final JLabel attackeBasis = new JLabel();
    private final JLabel ausdauerMax = new JLabel();
    private final JLabel magieresistenz = new JLabel();
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
        panel.setBorder(BorderFactory.createTitledBorder(Messages.get(
                    "BaseValues")));
        add(panel,
            new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(3, 6, 3, 6), 0, 0));

        final JSpinner le = new JSpinner(new SpinnerNumberModel(
                    character.getLebensenergie(), 0, 1000, 1));
        le.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setLebensenergie(((Number) le.getValue()).intValue());
                    updateDerivedFields();
                }
            });
        addInput(panel, "LE", le, row++);

        final JSpinner leBonus = new JSpinner(new SpinnerNumberModel(
                    character.getLebensenergieBonus(), -100, 100, 1));
        leBonus.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setLebensenergieBonus(((Number) leBonus.getValue()).intValue());
                    updateDerivedFields();
                }
            });
        addInput(panel, "LEbonus", leBonus, row++);
        addInput(panel, "LEmax", leMax, row++);

        final JSpinner ae = new JSpinner(new SpinnerNumberModel(
                    character.getAstralenergie(), 0, 1000, 1));
        ae.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setAstralenergie(((Number) ae.getValue()).intValue());
                    updateDerivedFields();
                }
            });
        addInput(panel, "AE", ae, row++);

        final JSpinner aeBonus = new JSpinner(new SpinnerNumberModel(
                    character.getAstralenergieBonus(), -100, 100, 1));
        aeBonus.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setAstralenergieBonus(((Number) aeBonus.getValue()).intValue());
                    updateDerivedFields();
                }
            });
        addInput(panel, "AEbonus", aeBonus, row++);
        addInput(panel, "AEmax", aeMax, row++);

        addInput(panel, "maximaleAusdauer", ausdauerMax, row++);
        addInput(panel, "Magieresistenz", magieresistenz, row++);
        addInput(panel, "AttackeBasis", attackeBasis, row++);
        addInput(panel, "ParadeBasis", paradeBasis, row++);
        addInput(panel, "Fernkampf-Basis", fernkampfBasis, row++);
    }

    public void updateDerivedFields() {
        aeMax.setText(String.valueOf(character.getAstralenergieMax()));
        leMax.setText(String.valueOf(character.getLebensenergieMax()));
        attackeBasis.setText(String.valueOf(character.getAttackeBasis()));
        paradeBasis.setText(String.valueOf(character.getParadeBasis()));
        fernkampfBasis.setText(String.valueOf(character.getFernkampfBasis()));
        ausdauerMax.setText(String.valueOf(character.getAusdauer()));
        magieresistenz.setText(String.valueOf(character.getMagieresistenz()));
    }

    private void addNumberFields(int panelRow) {
        final JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        add(container,
            new GridBagConstraints(0, panelRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        container.add(createMiscFields(container),
            new GridBagConstraints(0, 0, 1, 1, .5, .5,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        container.add(createSpeedFields(container),
            new GridBagConstraints(1, 0, 1, 1, .5, .5,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));
    }

    private JComponent createMiscFields(JPanel container) {
        int row = 0;
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(Messages.get(
                    "MiscFigures")));

        final JSpinner level = new JSpinner(new SpinnerNumberModel(
                    character.getLevel(), 1, 40, 1));
        level.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setLevel(((Number) level.getValue()).intValue());
                }
            });
        addInput(panel, "Stufe", level, row++);

        final JSpinner xp = new JSpinner(new SpinnerNumberModel(
                    character.getAbenteuerpunkte(), 0, 100000, 1));
        xp.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setAbenteuerpunkte(((Number) xp.getValue()).intValue());
                }
            });
        addInput(panel, "XP", xp, row++);

        final JSpinner up = new JSpinner(new SpinnerNumberModel(
                    character.getSteigerungspunkte(), 0, 100000, 1));
        up.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setSteigerungspunkte(((Number) up.getValue()).intValue());
                }
            });
        addInput(panel, "UpgradeXP", up, row++);

        if (character.isPlayerCharacter()) {
            final JSpinner money = new JSpinner(new SpinnerNumberModel(
                        character.getMoneyAmount(), 0, 9999999, 1));
            money.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        character.setMoneyAmount(((Number) money.getValue()).intValue());
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
        panel.setBorder(BorderFactory.createTitledBorder(Messages.get(
                    "SpeedFields")));

        final JSpinner sneak = new JSpinner(new SpinnerNumberModel(
                    character.getSneakSpeed(), 0, 100, .1));
        sneak.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setSneakSpeed(((Number) sneak.getValue()).doubleValue());
                }
            });
        addInput(panel, "SneakSpeed", sneak, row++);

        final JSpinner walk = new JSpinner(new SpinnerNumberModel(
                    character.getWalkSpeed(), 0, 100, .1));
        walk.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setWalkSpeed(((Number) walk.getValue()).doubleValue());
                }
            });
        addInput(panel, "WalkSpeed", walk, row++);

        final JSpinner run = new JSpinner(new SpinnerNumberModel(
                    character.getRunSpeed(), 0, 100, .1));
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

    private void addArchetypeFields(int panelRow) {
        int row = 0;

        final JPanel archetypePanel = new JPanel();
        archetypePanel.setLayout(new GridBagLayout());
        archetypePanel.setBorder(BorderFactory.createTitledBorder(Messages.get(
                    "Archetype")));
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
                    updateDerivedFields();
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

        final EnumComboBox<Culture> culture = new EnumComboBox<Culture>(Culture.values(),
                character.getCulture()) {
                protected void valueChanged(Culture item) {
                    character.setCulture(item);
                    updateDerivedFields();
                }
            };

        addInput(archetypePanel, "Culture", culture, row++);

        final EnumComboBox<Profession> profession = new EnumComboBox<Profession>(Profession.values(),
                character.getProfession()) {
                protected void valueChanged(Profession item) {
                    character.setProfession(item);
                    updateDerivedFields();
                }
            };

        addInput(archetypePanel, "Profession", profession, row++);

        final EnumComboBox<Sex> sex = new EnumComboBox<Sex>(Sex.values(),
                character.getSex()) {
                protected void valueChanged(Sex item)
                    throws ChangeRejectedException {
                    if (character.getCharacterSet().getArchetype(item) == null) {
                        String message = MessageFormat.format(Messages.get(
                                    "error.sexAppearance.message"),
                                Messages.get(character.getCharacterSet()
                                                      .getArchetype(character.getSex())));
                        String title = Messages.get("error.sexAppearance.title");
                        throw new ChangeRejectedException(message, title);
                    }

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

        final JCheckBox magician = new JCheckBox();
        magician.setSelected(character.isMagician());
        magician.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    character.setMagician(magician.isSelected());
                }
            });
        addInput(archetypePanel, "Magician", magician, row++);

        final EnumComboBox<CasterType> casterType = new EnumComboBox<CasterType>(CasterType.values(),
                character.getCasterType()) {
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

        final EnumComboBox<CasterRace> casterRace = new EnumComboBox<CasterRace>(CasterRace.values(),
                character.getCasterRace()) {
                protected void valueChanged(CasterRace item) {
                    character.setCasterRace(item);
                }

                protected boolean accept(CasterRace item) {
                    return item != CasterRace.dwarf;
                }
            };

        addInput(archetypePanel, "CasterRace", casterRace, row++);
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
        String url = character.getCharacterSet().getIcon(character.getSex()) +
            ".png";

        return new ImageIcon(getClass().getResource(url));
    }

    private void addInput(JComponent parent, String label, JComponent input,
        int row) {
        addInput(parent, label, null, input, row);
    }

    private void addInput(JComponent parent, String label, String infoLabel,
        JComponent input, int row) {
        parent.add(new InfoLabel(label, infoLabel),
            new GridBagConstraints(0, row, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(3, 6, 3, 6), 0, 0));

        if (input != null) {
            parent.add(input,
                new GridBagConstraints(1, row, 1, 1, 0, 0,
                    GridBagConstraints.NORTHWEST,
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
