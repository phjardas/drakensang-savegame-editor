package de.jardas.drakensang.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import de.jardas.drakensang.Main;
import de.jardas.drakensang.gui.inventory.InventoryPanel;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.shared.db.Messages;

public class CharacterPanel extends JPanel {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(CharacterPanel.class);
	private Character character;
	private JTabbedPane tabs = new JTabbedPane();
	private AttributePanel attributesPanel = new AttributePanel();
	private CharacterInfoPanel infoPanel = new CharacterInfoPanel(
			attributesPanel);
	private AdvantagesPanel advantagesPanel = new AdvantagesPanel();
	private TalentePanel talentePanel = new TalentePanel();
	private ZauberPanel zauberPanel = new ZauberPanel();
	private SonderfertigkeitenPanel sonderPanel = new SonderfertigkeitenPanel();
	private InventoryPanel inventoryPanel = new InventoryPanel();
	private boolean initialized;

	public CharacterPanel() {
		attributesPanel.addChangeListener(new IntegerMapPanel.ChangeListener() {
			public void valueChanged(String key, int value) {
				talentePanel.onAttributeChange(key, value);
			}
		});
	}

	private void initialize() {
		if (initialized) {
			return;
		}

		tabs.addTab(Messages.get("CharacterSheet"), new JScrollPane(infoPanel));
		tabs.addTab(Messages.get("Advantages"),
				new JScrollPane(advantagesPanel));
		tabs.addTab(Messages.get("Talents"), new JScrollPane(talentePanel));
		tabs
				.addTab(Messages.get("SpecialSkills"), new JScrollPane(
						sonderPanel));
		tabs.addTab(Messages.get("Spells"), new JScrollPane(zauberPanel));
		tabs.addTab(Messages.get("Inventory"), inventoryPanel);

		setLayout(new BorderLayout());
		add(tabs, BorderLayout.CENTER);
		initialized = true;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		if (character == this.character) {
			LOG.debug("Not updating character currently being displayed: "
					+ character.getId());

			return;
		}

		LOG.debug("Updating displayed character: " + character.getId());

		this.character = character;
		Main.getFrame().setBusy(true);

		initialize();

		infoPanel.setCharacter(character);
		attributesPanel.setValues(character.getAttribute());
		advantagesPanel.setCharacter(character);
		talentePanel.setCharacter(character);
		zauberPanel.setValues(character.getZauberfertigkeiten());
		sonderPanel.setValues(character.getSonderfertigkeiten());
		inventoryPanel.setInventory(character.getInventory());

		Main.getFrame().setBusy(false);

		repaint();
	}
}
