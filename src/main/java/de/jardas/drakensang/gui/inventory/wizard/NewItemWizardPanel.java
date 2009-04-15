package de.jardas.drakensang.gui.inventory.wizard;

import java.awt.BorderLayout;

import javax.swing.JPanel;


class NewItemWizardPanel extends JPanel {
    private JPanel contentPanel;

    public NewItemWizardPanel(String titleKey) {
        super();

        setLayout(new BorderLayout());

        contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    JPanel getContentPanel() {
        return contentPanel;
    }
}
