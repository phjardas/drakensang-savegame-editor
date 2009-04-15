package de.jardas.drakensang.gui.inventory.wizard;

class ArchetypeDescriptor extends NewItemWizardPanelDescriptor {
    private final ArchetypePanel panel;

    ArchetypeDescriptor(String id) {
        super(id, null);
        panel = new ArchetypePanel();
        setPanelComponent(panel);
    }

    public Object getNextPanelDescriptor() {
        return NewItemWizard.DETAILS_PANEL;
    }

    public Object getBackPanelDescriptor() {
        return NewItemWizard.GROUP_PANEL;
    }

    @Override
    public void aboutToDisplayPanel() {
        super.aboutToDisplayPanel();

        panel.setItemGroup(getNewItemWizard().getItemGroup());
    }

    @Override
    public void aboutToHidePanel() {
        super.aboutToHidePanel();

        getNewItemWizard().setItem(panel.createItem());
    }
}
