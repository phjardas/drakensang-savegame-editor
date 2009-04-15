package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.dao.Static;
import de.jardas.drakensang.model.inventory.InventoryItem;
import de.jardas.drakensang.model.inventory.Shield;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ShieldRenderer extends InventoryItemRenderer<Shield> {
    public JComponent renderSpecial(final Shield shield) {
        final JSpinner atSpinner = new JSpinner(new SpinnerNumberModel(
                    shield.getAttackeMod(), -10, 10, 1));
        atSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    shield.setAttackeMod(((Number) atSpinner.getValue())
                        .intValue());
                }
            });

        final JSpinner paSpinner = new JSpinner(new SpinnerNumberModel(
                    shield.getParadeMod(), -10, 10, 1));
        paSpinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    shield.setParadeMod(((Number) paSpinner.getValue()).intValue());
                }
            });

        final JPanel panel = new JPanel();

        panel.add(atSpinner);
        panel.add(new JLabel("/"));
        panel.add(paSpinner);

        return panel;
    }

    @Override
    public String getNameKey(Shield item) {
        return Static.get("LookAtText", item.getId(), "Id", "_Template_Shield");
    }

    @Override
    public String getInfoKey(Shield item) {
        return Static.get("InfoIdentified", item.getId(), "Id",
            "_Template_Shield");
    }

    @Override
    public String renderInlineInfo(Shield item) {
        return item.getAttackeMod() + "/" + item.getParadeMod();
    }

    @Override
    public boolean isApplicable(InventoryItem item) {
        return item instanceof Shield;
    }
}
