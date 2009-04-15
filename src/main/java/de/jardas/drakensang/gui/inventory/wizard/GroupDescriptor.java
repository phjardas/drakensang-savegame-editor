package de.jardas.drakensang.gui.inventory.wizard;

class GroupDescriptor extends NewItemWizardPanelDescriptor {
    private final GroupPanel panel;

    GroupDescriptor(String id) {
        super(id, null);
        panel = new GroupPanel();
        setPanelComponent(panel);
    }

    public Object getNextPanelDescriptor() {
        return NewItemWizard.ARCHETYPE_PANEL;
    }

    public Object getBackPanelDescriptor() {
        return NewItemWizard.CHARACTER_PANEL;
    }

    @Override
    public void aboutToHidePanel() {
        super.aboutToHidePanel();

        getNewItemWizard().setItemGroup(panel.getItemGroup());
    }
}
