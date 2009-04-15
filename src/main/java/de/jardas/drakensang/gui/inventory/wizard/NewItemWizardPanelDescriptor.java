package de.jardas.drakensang.gui.inventory.wizard;

import de.jardas.drakensang.gui.wizard.WizardPanelDescriptor;

import java.awt.Component;


abstract class NewItemWizardPanelDescriptor extends WizardPanelDescriptor {
    NewItemWizardPanelDescriptor(Object id, Component panel) {
        super(id, panel);
    }

    NewItemWizardPanelDescriptor() {
        super();
    }

    NewItemWizard getNewItemWizard() {
        return (NewItemWizard) getWizard();
    }
}
