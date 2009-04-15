package de.jardas.drakensang.gui.wizard;

import de.jardas.drakensang.DrakensangException;


public class WizardPanelNotFoundException extends DrakensangException {
    public WizardPanelNotFoundException(Object id) {
        super("Wizard panel not found: " + id);
    }
}
