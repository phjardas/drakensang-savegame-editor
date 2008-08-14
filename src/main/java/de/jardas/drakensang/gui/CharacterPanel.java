package de.jardas.drakensang.gui;

import de.jardas.drakensang.gui.inventory.InventoryPanel;
import de.jardas.drakensang.model.Character;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class CharacterPanel extends JPanel {
    private Character character;
    private JTabbedPane tabs = new JTabbedPane();
    private AttributePanel attributesPanel = new AttributePanel();
    private TalentePanel talentePanel = new TalentePanel();
    private ZauberPanel zauberPanel = new ZauberPanel();
    private SonderfertigkeitenPanel sonderPanel = new SonderfertigkeitenPanel();
    private InventoryPanel inventoryPanel = new InventoryPanel();

    public CharacterPanel() {
        tabs.addTab("Attribute", attributesPanel);
        tabs.addTab("Talente", talentePanel);
        tabs.addTab("Sonderfertigkeiten", sonderPanel);
        tabs.addTab("Zauberfertigkeiten", zauberPanel);
        tabs.addTab("Inventar", inventoryPanel);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        if (character == this.character) {
            return;
        }

        this.character = character;
        attributesPanel.setValues(character.getAttribute());
        talentePanel.setValues(character.getTalente());
        zauberPanel.setValues(character.getZauberfertigkeiten());
        sonderPanel.setValues(character.getSonderfertigkeiten());
        inventoryPanel.setInventory(character.getInventory());

        repaint();
    }
}
