package de.jardas.drakensang.gui.inventory.wizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.jardas.drakensang.dao.InventoryDao;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.inventory.InventoryItemRenderer;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.InventoryItem;

public class NewItemPanel extends JPanel {
	private final InventoryDao inventoryDao;

	public NewItemPanel(final InventoryDao inventoryDao,
			final List<Character> characters) {
		this.inventoryDao = inventoryDao;
		setLayout(new GridBagLayout());

		Insets insets = new Insets(3, 6, 3, 6);
		int row = 0;

		add(new JLabel("Wer soll den neuen Gegenstand erhalten?"),
				new GridBagConstraints(0, row++, 1, 1, 0, 0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, insets, 0, 0));

		add(new JComboBox(new DefaultComboBoxModel(characters.toArray()) {
			@Override
			public Object getElementAt(int index) {
				return getCharacterName(characters.get(index));
			}
		}), new GridBagConstraints(0, row++, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0));

		add(new JLabel("Welche Art von Gegenstand möchtest du erzeugen?"),
				new GridBagConstraints(0, row++, 1, 1, 0, 0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, insets, 0, 0));

		final JComboBox inventoryType = new JComboBox(new DefaultComboBoxModel(
				InventoryItem.TYPES) {
			@Override
			public Object getElementAt(int index) {
				return Messages.get("inventorygroup."
						+ InventoryItem.TYPES[index].getSimpleName());
			}
		});
		final JComboBox templateBox = new JComboBox();
		inventoryType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					int index = inventoryType.getSelectedIndex();
					@SuppressWarnings("unchecked")
					Class<? extends InventoryItem> type = (Class<? extends InventoryItem>) InventoryItem.TYPES[index];
					templateBox.setModel(new TemplateModel(type));
				}
			}
		});
		add(inventoryType, new GridBagConstraints(0, row++, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0));

		add(new JLabel("Wähle einen Prototypen:"), new GridBagConstraints(0,
				row++, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, insets, 0, 0));

		add(templateBox, new GridBagConstraints(0, row++, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0));

	}

	private String getCharacterName(Character character) {
		return character.isLocalizeLookAtText() ? Messages.get(character
				.getLookAtText()) : character.getLookAtText();
	}

	private class TemplateModel extends DefaultComboBoxModel {
		private final List<InventoryItem> items;

		public TemplateModel(Class<? extends InventoryItem> itemClass) {
			items = inventoryDao.loadInventory(itemClass);

			for (InventoryItem item : items) {
				addElement(new Item(item));
			}
		}

		private class Item {
			private final InventoryItem item;

			public Item(InventoryItem item) {
				super();
				this.item = item;
			}

			@Override
			public String toString() {
				String key = InventoryItemRenderer.getRenderer(item)
						.getNameKey(item);
				return Messages.get(key);
			}

			public InventoryItem getItem() {
				return item;
			}
		}
	}
}
