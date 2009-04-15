package de.jardas.drakensang.gui.inventory.wizard;

class DetailsDescriptor extends NewItemWizardPanelDescriptor {
    private final DetailsPanel panel;

    DetailsDescriptor(String id) {
        super(id, null);
        panel = new DetailsPanel();
        setPanelComponent(panel);
    }

    public Object getNextPanelDescriptor() {
        return FINISH;
    }

    public Object getBackPanelDescriptor() {
        return NewItemWizard.ARCHETYPE_PANEL;
    }

    @Override
    public void aboutToDisplayPanel() {
        super.aboutToDisplayPanel();

        panel.setItem(getNewItemWizard().getItem());
    }

    @Override
    public void aboutToHidePanel() {
        super.aboutToHidePanel();

        getNewItemWizard().setItem(panel.getItem());
    }
}
