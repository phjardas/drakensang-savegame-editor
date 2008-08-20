/*
 * AdvantagesPanel.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.Advantage;
import de.jardas.drakensang.model.Advantage.Effect;
import de.jardas.drakensang.model.Character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class AdvantagesPanel extends JPanel {
    private Character character;

    public AdvantagesPanel() {
        setLayout(new GridBagLayout());
    }

    private void update() {
        removeAll();
        addAdvantageFields();
    }

    private void addAdvantageFields() {
        List<Advantage> advantages = new ArrayList<Advantage>(Arrays.asList(
                    Advantage.values()));
        Collections.sort(advantages,
            new Comparator<Advantage>() {
                private final Collator collator = Collator.getInstance();

                public int compare(Advantage o1, Advantage o2) {
                    return collator.compare(getName(o1), getName(o2));
                }
            });

        int row = 0;

        for (Advantage advantage : advantages) {
            addAdvantageField(advantage, row++);
        }
    }

    private void addAdvantageField(final Advantage advantage, int row) {
        final JCheckBox box = new JCheckBox();
        box.setSelected(getCharacter().getAdvantages().contains(advantage));
        box.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (box.isSelected()) {
                        getCharacter().getAdvantages().add(advantage);
                    } else if (!getCharacter().getAdvantages()
                                        .contains(advantage)) {
                        getCharacter().getAdvantages().remove(advantage);
                    }
                }
            });

        add(box,
            new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));

        add(new InfoLabel(advantage.getNameKey(), advantage.getInfoKey()),
            new GridBagConstraints(1, row, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));

        StringBuffer effects = new StringBuffer();

        for (Effect effect : advantage.getEffects()) {
            if (effects.length() > 0) {
                effects.append(", ");
            }

            effects.append(Messages.get(effect.getTargetNameKey()));
            effects.append(" ");
            effects.append((effect.getModifier() > 0) ? "+" : "");
            effects.append(effect.getModifier());
        }

        add(new JLabel(effects.toString()),
            new GridBagConstraints(2, row, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));
    }

    private String getName(Advantage adv) {
        return Messages.get(adv.getNameKey());
    }

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character character) {
        if (this.character == character) {
            return;
        }

        this.character = character;
        update();
    }
}
