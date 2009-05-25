package de.jardas.drakensang.gui.inventory.wizard;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.Character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;


public class CharacterPanel extends NewItemWizardPanel {
    private final List<Character> characters;
    private final JComboBox characterBox;

    CharacterPanel() {
        super("wizard.item.character.title");

        characters = new ArrayList<Character>(CharacterDao.getCharacters());
        Collections.sort(characters,
            new Comparator<Character>() {
                private final Collator collator = Collator
                    .getInstance();

                public int compare(Character o1, Character o2) {
                    if (o1.isPlayerCharacter()) {
                        return -1;
                    }

                    if (o2.isPlayerCharacter()) {
                        return 1;
                    }

                    return collator.compare(getCharacterName(o1),
                        getCharacterName(o2));
                }
            });

        characterBox = new JComboBox(new DefaultComboBoxModel(
                    characters.toArray()) {
                    @Override
                    public Object getElementAt(int index) {
                        return getCharacterName(characters.get(index));
                    }
                });

        getContentPanel().setLayout(new GridBagLayout());

        Insets insets = new Insets(3, 6, 3, 6);
        int row = 0;

        getContentPanel()
            .add(new JLabel(Messages.get("wizard.item.character.label")),
            new GridBagConstraints(0, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        getContentPanel()
            .add(characterBox,
            new GridBagConstraints(0, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));
    }

    private String getCharacterName(Character character) {
        return character.isLocalizeLookAtText()
        ? Messages.get(character.getLookAtText()) : character.getLookAtText();
    }

    Character getSelectedCharacter() {
        return characters.get(characterBox.getSelectedIndex());
    }
}
