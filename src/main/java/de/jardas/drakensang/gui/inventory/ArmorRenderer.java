package de.jardas.drakensang.gui.inventory;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.shared.gui.InfoLabel;
import de.jardas.drakensang.shared.model.inventory.Armor;
import de.jardas.drakensang.shared.model.inventory.InventoryItem;


public class ArmorRenderer extends InventoryItemRenderer<Armor> {
    private static final int COLUMNS = 4;

    @Override
    public JComponent renderSpecial(final Armor armor) {
        JPanel panel = new JPanel();
        Status status = new Status();
        panel.setLayout(new GridBagLayout());

        addSpinner(new ArmorAccess("head") {
                public int getValue() {
                    return armor.getRuestungKopf();
                }

                public void setValue(int value) {
                    armor.setRuestungKopf(value);
                }
            }, panel, status);

        addSpinner(new ArmorAccess("breast") {
                public int getValue() {
                    return armor.getRuestungBrust();
                }

                public void setValue(int value) {
                    armor.setRuestungBrust(value);
                }
            }, panel, status);

        addSpinner(new ArmorAccess("back") {
                public int getValue() {
                    return armor.getRuestungRuecken();
                }

                public void setValue(int value) {
                    armor.setRuestungRuecken(value);
                }
            }, panel, status);

        addSpinner(new ArmorAccess("stomach") {
                public int getValue() {
                    return armor.getRuestungBauch();
                }

                public void setValue(int value) {
                    armor.setRuestungBauch(value);
                }
            }, panel, status);

        addSpinner(new ArmorAccess("leftarm") {
                public int getValue() {
                    return armor.getRuestungArmLinks();
                }

                public void setValue(int value) {
                    armor.setRuestungArmLinks(value);
                }
            }, panel, status);

        addSpinner(new ArmorAccess("rightarm") {
                public int getValue() {
                    return armor.getRuestungArmRechts();
                }

                public void setValue(int value) {
                    armor.setRuestungArmRechts(value);
                }
            }, panel, status);

        addSpinner(new ArmorAccess("leftleg") {
                public int getValue() {
                    return armor.getRuestungBeinLinks();
                }

                public void setValue(int value) {
                    armor.setRuestungBeinLinks(value);
                }
            }, panel, status);

        addSpinner(new ArmorAccess("rightleg") {
                public int getValue() {
                    return armor.getRuestungBeinRechts();
                }

                public void setValue(int value) {
                    armor.setRuestungBeinRechts(value);
                }
            }, panel, status);

        return panel;
    }

    private void addSpinner(final ArmorAccess access, JComponent parent,
        Status status) {
        Insets insets = new Insets(3, 6, 3, 6);

        parent.add(new InfoLabel(access.getNameKey(), null),
            new GridBagConstraints(2 * status.getColumn(), status.getRow(), 1,
                1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                insets, 0, 0));

        final JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                    access.getValue(), 0, 100, 1));
        parent.add(spinner,
            new GridBagConstraints((2 * status.getColumn()) + 1,
                status.getRow(), 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, insets, 0, 0));

        spinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    access.setValue(((Number) spinner.getValue()).intValue());
                }
            });

        status.advance();
    }

    @Override
    public boolean isApplicable(InventoryItem item) {
        return item instanceof Armor;
    }

    private static abstract class ArmorAccess {
        private final String name;

        public ArmorAccess(String name) {
            super();
            this.name = name;
        }

        public abstract int getValue();

        public abstract void setValue(int value);

        public String getNameKey() {
            return "armor." + name;
        }
    }

    private static class Status {
        private int column = 0;
        private int row = 0;

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
        }

        public void advance() {
            column++;

            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
        }
    }
}
