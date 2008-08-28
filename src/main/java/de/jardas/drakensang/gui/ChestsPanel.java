/*
 * ChestsPanel.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.LevelDao;
import de.jardas.drakensang.model.Chest;
import de.jardas.drakensang.model.Level;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.text.Collator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ChestsPanel extends JPanel {
    public ChestsPanel() {
        setLayout(new GridBagLayout());
    }

    public void update() {
        removeAll();

        int row = 0;

        add(new JLabel("Level:"),
            new GridBagConstraints(0, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        final ChestSelection chestSelection = new ChestSelection();
        final LevelSelection levelSelection = new LevelSelection(LevelDao
                .getLevels());
        final JComboBox levelBox = new JComboBox(levelSelection);
        final JComboBox chestBox = new JComboBox(chestSelection);
        add(levelBox,
            new GridBagConstraints(0, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        levelBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    chestSelection.setChests(LevelDao.getChests(
                            levelSelection.getSelectedLevel()));
                }
            });

        add(new JLabel("Chests:"),
            new GridBagConstraints(0, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(chestBox,
                new GridBagConstraints(0, row++, 1, 1, 0, 0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
    }

    private static class LevelSelection extends DefaultComboBoxModel {
        private final List<Level> levels;

        public LevelSelection(List<Level> levels) {
            this.levels = levels;

            Collections.sort(this.levels,
                new Comparator<Level>() {
                    private final Collator collator = Collator.getInstance();

                    public int compare(Level o1, Level o2) {
                        return collator.compare(o1.getLocalizedName(),
                            o2.getLocalizedName());
                    }
                });

            for (Level level : levels) {
                addElement(new LevelItem(level));
            }
        }

        public Level getSelectedLevel() {
            return ((LevelItem) getSelectedItem()).getLevel();
        }

        private static class LevelItem {
            private final Level level;

            public LevelItem(final Level level) {
                super();
                this.level = level;
            }

            public Level getLevel() {
                return this.level;
            }

            @Override
            public String toString() {
                return level.getLocalizedName();
            }
        }
    }

    private static class ChestSelection extends DefaultComboBoxModel {
        private List<Chest> chests;

        public void setChests(List<Chest> chests) {
            removeAllElements();

            this.chests = chests;

            if (this.chests != null) {
                Collections.sort(this.chests,
                    new Comparator<Chest>() {
                        private final Collator collator = Collator.getInstance();

                        public int compare(Chest o1, Chest o2) {
                            return collator.compare(o1.getLocalizedName(),
                                o2.getLocalizedName());
                        }
                    });

                for (Chest chest : chests) {
                    addElement(new ChestItem(chest));
                }
            }
        }

        public Chest getSelectedChest() {
            return ((ChestItem) getSelectedItem()).getChest();
        }

        private static class ChestItem {
            private final Chest chest;

            public ChestItem(final Chest chest) {
                super();
                this.chest = chest;
            }

            public Chest getChest() {
                return this.chest;
            }

            @Override
            public String toString() {
                return chest.getLocalizedName();
            }
        }
    }
}
