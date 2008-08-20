package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.inventory.InventoryPanel;
import de.jardas.drakensang.model.Character;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


public class CharacterPanel extends JPanel {
    private final MainFrame mainFrame;
    private Character character;
    private JTabbedPane tabs = new JTabbedPane();
    private AttributePanel attributesPanel = new AttributePanel();
    private CharacterInfoPanel infoPanel = new CharacterInfoPanel(attributesPanel);
    private AdvantagesPanel advantagesPanel = new AdvantagesPanel();
    private TalentePanel talentePanel = new TalentePanel();
    private ZauberPanel zauberPanel = new ZauberPanel();
    private SonderfertigkeitenPanel sonderPanel = new SonderfertigkeitenPanel();
    private InventoryPanel inventoryPanel = new InventoryPanel();

    public CharacterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        attributesPanel.addChangeListener(new IntegerMapPanel.ChangeListener() {
                public void valueChanged(String key, int value) {
                    talentePanel.onAttributeChange(key, value);
                }
            });

        tabs.addTab(Messages.get("CharacterSheet"), new JScrollPane(infoPanel));
        tabs.addTab(Messages.get("Advantages"), new JScrollPane(advantagesPanel));
        tabs.addTab(Messages.get("Talents"), new JScrollPane(talentePanel));
        tabs.addTab(Messages.get("SpecialSkills"), new JScrollPane(sonderPanel));
        tabs.addTab(Messages.get("Spells"), new JScrollPane(zauberPanel));
        tabs.addTab(Messages.get("Inventory"), inventoryPanel);

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
        this.mainFrame.setBusy(true);

        infoPanel.setCharacter(character);
        attributesPanel.setValues(character.getAttribute());
        advantagesPanel.setCharacter(character);
        talentePanel.setCharacter(character);
        zauberPanel.setValues(character.getZauberfertigkeiten());
        sonderPanel.setValues(character.getSonderfertigkeiten());
        inventoryPanel.setInventory(character.getInventory());

        this.mainFrame.setBusy(false);

        repaint();
    }
}
