package de.jardas.drakensang.gui.inventory.wizard;

class CharacterDescriptor extends NewItemWizardPanelDescriptor {
    private final CharacterPanel panel;

    CharacterDescriptor(String id) {
        super(id, null);
        panel = new CharacterPanel();
        setPanelComponent(panel);
    }

    public Object getNextPanelDescriptor() {
        return NewItemWizard.GROUP_PANEL;
    }

    public Object getBackPanelDescriptor() {
        return null;
    }

    @Override
    public void aboutToHidePanel() {
        super.aboutToHidePanel();

        getNewItemWizard().setCharacter(panel.getSelectedCharacter());
    }
}
