package de.jardas.drakensang.gui.inventory.wizard;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.inventory.InventoryItemRenderer;
import de.jardas.drakensang.model.inventory.InventoryItem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;


class DetailsPanel extends NewItemWizardPanel {
    private InventoryItem item;

    DetailsPanel() {
        super("wizard.item.details.title");
        getContentPanel().setLayout(new GridBagLayout());
    }

    InventoryItem getItem() {
        return item;
    }

    void setItem(InventoryItem item) {
        this.item = item;

        replaceComponents();
    }

    private void replaceComponents() {
        getContentPanel().removeAll();

        final Insets insets = new Insets(3, 6, 3, 6);
        int row = 0;

        final InventoryItemRenderer<InventoryItem> renderer = InventoryItemRenderer
            .getRenderer(item);

        final JComponent label = renderer.renderLabel(item);
        getContentPanel()
            .add(label,
            new GridBagConstraints(0, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        JComponent special = renderer.renderSpecial(item);

        if (special == null) {
            special = new JLabel(Messages.get("wizard.item.details.nospecial"));
        }

        getContentPanel()
            .add(special,
            new GridBagConstraints(0, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));
    }
}
