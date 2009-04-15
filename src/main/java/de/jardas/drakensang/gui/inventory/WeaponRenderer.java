package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.inventory.InventoryItem;
import de.jardas.drakensang.model.inventory.Weapon;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class WeaponRenderer extends InventoryItemRenderer<Weapon> {
    @Override
    public JComponent renderSpecial(final Weapon weapon) {
        final JLabel schadenLabel = new JLabel(getSchadenText(weapon));

        final JSpinner diceSpinner = new JSpinner(new SpinnerNumberModel(
                    weapon.getSchaden().getDiceMultiplier(), 0, 10, 1));
        diceSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    weapon.getSchaden()
                          .setDiceMultiplier(((Number) diceSpinner.getValue())
                        .intValue());
                    schadenLabel.setText(getSchadenText(weapon));
                }
            });

        final JSpinner addSpinner = new JSpinner(new SpinnerNumberModel(
                    weapon.getSchaden().getAddition(), -100, 100, 1));
        addSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    weapon.getSchaden()
                          .setAddition(((Number) addSpinner.getValue()).intValue());
                    schadenLabel.setText(getSchadenText(weapon));
                }
            });

        final JPanel panel = new JPanel();

        panel.add(diceSpinner);
        panel.add(new JLabel(Messages.get("D6") + "+"));
        panel.add(addSpinner);
        panel.add(schadenLabel);

        return panel;
    }

    private String getSchadenText(Weapon weapon) {
        return " (" + weapon.getSchaden().getMinimum() + "-"
        + weapon.getSchaden().getMaximum() + " " + Messages.get("TP") + ")";
    }

    @Override
    public String renderInlineInfo(Weapon weapon) {
        return weapon.getSchaden().toString();
    }

    @Override
    public boolean isApplicable(InventoryItem item) {
        return item instanceof Weapon;
    }
}
