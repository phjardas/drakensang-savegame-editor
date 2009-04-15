package de.jardas.drakensang.gui.inventory.wizard;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.wizard.Wizard;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.inventory.InventoryItem;

import java.awt.Dialog;
import java.awt.Frame;


public class NewItemWizard extends Wizard {
    static final String CHARACTER_PANEL = "character";
    static final String GROUP_PANEL = "group";
    static final String ARCHETYPE_PANEL = "archetype";
    static final String DETAILS_PANEL = "details";
    private Character character;
    private Class<?extends InventoryItem> itemGroup;
    private InventoryItem item;

    public NewItemWizard(Dialog owner) {
        super(owner);
        initialize();
    }

    public NewItemWizard(Frame owner) {
        super(owner);
        initialize();
    }

    private void initialize() {
        getDialog().setTitle(Messages.get("wizard.item.title"));

        registerWizardPanel(new CharacterDescriptor(CHARACTER_PANEL));
        registerWizardPanel(new GroupDescriptor(GROUP_PANEL));
        registerWizardPanel(new ArchetypeDescriptor(ARCHETYPE_PANEL));
        registerWizardPanel(new DetailsDescriptor(DETAILS_PANEL));

        setCurrentPanel(CHARACTER_PANEL);

        final int result = showModalDialog();

        if (result == FINISH_RETURN_CODE) {
            save();
        }
    }

    private void save() {
        getCharacter().getInventory().add(item);
    }

    Class<?extends InventoryItem> getItemGroup() {
        return itemGroup;
    }

    void setItemGroup(Class<?extends InventoryItem> itemGroup) {
        this.itemGroup = itemGroup;
    }

    InventoryItem getItem() {
        return item;
    }

    void setItem(InventoryItem item) {
        this.item = item;
    }

    Character getCharacter() {
        return character;
    }

    void setCharacter(Character character) {
        this.character = character;
    }
}
