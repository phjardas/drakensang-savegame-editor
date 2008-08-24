package de.jardas.drakensang.gui.inventory.wizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
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
	private InventoryItem item;

	public NewItemPanel(final InventoryDao inventoryDao,
			final List<Character> characters) {
		this.inventoryDao = inventoryDao;

		final JPanel detailsPanel = new JPanel();
		detailsPanel.add(new JLabel("x"));

		final JComboBox characterBox = new JComboBox(new DefaultComboBoxModel(
				characters.toArray()) {
			@Override
			public Object getElementAt(int index) {
				return getCharacterName(characters.get(index));
			}
		});

		final JComboBox templateBox = new JComboBox();

		templateBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					InventoryItem selected = ((TemplateModel) templateBox
							.getModel()).getSelectedInventoryItem();
					item = selected;

					InventoryItemRenderer renderer = InventoryItemRenderer
							.getRenderer(item);
					
					detailsPanel.removeAll();
					detailsPanel.add(renderer.renderSpecial(item));
				}
			}
		});

		final JComboBox inventoryType = new JComboBox(new DefaultComboBoxModel(
				InventoryItem.TYPES) {
			@Override
			public Object getElementAt(int index) {
				return Messages.get("inventorygroup."
						+ InventoryItem.TYPES[index].getSimpleName());
			}
		});

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

		setLayout(new GridBagLayout());

		Insets insets = new Insets(3, 6, 3, 6);
		int row = 0;

		add(new JLabel("Wer soll den neuen Gegenstand erhalten?"),
				new GridBagConstraints(0, row++, 1, 1, 0, 0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, insets, 0, 0));

		add(characterBox, new GridBagConstraints(0, row++, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0));

		add(new JLabel("Welche Art von Gegenstand möchtest du erzeugen?"),
				new GridBagConstraints(0, row++, 1, 1, 0, 0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, insets, 0, 0));

		add(inventoryType, new GridBagConstraints(0, row++, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0));

		add(new JLabel("Wähle einen Prototypen:"), new GridBagConstraints(0,
				row++, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, insets, 0, 0));

		add(templateBox, new GridBagConstraints(0, row++, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0));

		add(new JLabel("Details:"), new GridBagConstraints(0, row++, 1, 1, 0,
				0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0));

		add(detailsPanel, new GridBagConstraints(0, row++, 1, 1, 0, 0,
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
			Collections.sort(items, new Comparator<InventoryItem>() {
				private final Collator collator = Collator.getInstance();

				public int compare(InventoryItem o1, InventoryItem o2) {
					return collator.compare(getName(o1), getName(o2));
				}
			});

			for (InventoryItem item : items) {
				addElement(new Item(item));
			}
		}

		public InventoryItem getSelectedInventoryItem() {
			return ((Item) getSelectedItem()).getItem();
		}

		private String getName(InventoryItem item) {
			return Messages.get(InventoryItemRenderer.getRenderer(item)
					.getNameKey(item));
		}

		private class Item {
			private final InventoryItem item;

			public Item(InventoryItem item) {
				super();
				this.item = item;
			}

			@Override
			public String toString() {
				return getName(getItem());
			}

			public InventoryItem getItem() {
				return item;
			}
		}
	}
}
