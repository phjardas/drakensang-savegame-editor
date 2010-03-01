package de.jardas.drakensang.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import de.jardas.drakensang.gui.inventory.InventoryPanel;
import de.jardas.drakensang.shared.Launcher;
import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.model.Character;

public class CharacterPanel extends JPanel {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
			.getLogger(CharacterPanel.class);
	private Character character;
	private JTabbedPane tabs = new JTabbedPane();
	private CharacterInfoPanel infoPanel = new CharacterInfoPanel();
	private FiguresPanel figuresPanel = new FiguresPanel();
	private AdvantagesPanel advantagesPanel = new AdvantagesPanel();
	private TalentePanel talentePanel = new TalentePanel();
	private ZauberPanel zauberPanel = new ZauberPanel();
	private SonderfertigkeitenPanel sonderPanel = new SonderfertigkeitenPanel();
	private InventoryPanel inventoryPanel = new InventoryPanel();
	private boolean initialized;

	private void initialize() {
		if (initialized) {
			return;
		}

		tabs.addTab(Messages.get("CharacterSheet"), new JScrollPane(infoPanel));
		tabs.addTab(Messages.get("FiguresSheet"), new JScrollPane(figuresPanel));
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
		((MainFrame) Launcher.getMainFrame()).setBusy(true);

		initialize();

		infoPanel.setCharacter(character);
		figuresPanel.setCharacter(character);
		advantagesPanel.setCharacter(character);
		talentePanel.setCharacter(character);
		zauberPanel.setCharacter(character);
		sonderPanel.setValues(character.getSonderfertigkeiten());
		inventoryPanel.setInventory(character.getInventory());

		((MainFrame) Launcher.getMainFrame()).setBusy(false);

		repaint();
	}
}
