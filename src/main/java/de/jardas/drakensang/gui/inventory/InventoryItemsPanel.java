package de.jardas.drakensang.gui.inventory;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.model.inventory.InventoryItem;
import de.jardas.drakensang.shared.model.inventory.Money;

public class InventoryItemsPanel extends JPanel {
	private final JTabbedPane tabs = new JTabbedPane();
	private List<InventoryItem> items;

	public InventoryItemsPanel() {
		setLayout(new BorderLayout());
	}

	private void update() {
		tabs.removeAll();
		add(tabs, BorderLayout.CENTER);

		final Map<String, List<InventoryItem>> map = new HashMap<String, List<InventoryItem>>();

		for (InventoryItem item : items) {
			if (item instanceof Money) {
				continue;
			}
			
			final String group = getGroupTitle(item.getClass());
			List<InventoryItem> list = map.get(group);

			if (list == null) {
				list = new ArrayList<InventoryItem>();
				map.put(group, list);
			}

			list.add(item);
		}

		final List<String> groups = new ArrayList<String>(map.keySet());
		Collections.sort(groups, Collator.getInstance());

		for (String group : groups) {
			final JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			int row = 0;

			for (InventoryItem item : map.get(group)) {
				int col = 0;

				for (JComponent comp : getRenderer(item).createComponents(item)) {
					if (comp != null) {
						panel.add(comp, new GridBagConstraints(col, row, 1, 1,
								0, 0, GridBagConstraints.WEST,
								GridBagConstraints.NONE,
								new Insets(3, 6, 3, 6), 0, 0));
					}

					col++;
				}

				row++;
			}

			panel.add(new JLabel(), new GridBagConstraints(0, row, 1, 1, 0, 1,
					GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
					new Insets(0, 0, 0, 0), 0, 0));

			tabs.addTab(group, new JScrollPane(panel));
		}

		repaint();
	}

	private String getGroupTitle(Class<? extends InventoryItem> currentClass) {
		return Messages.get("inventorygroup." + currentClass.getSimpleName());
	}

	private <I extends InventoryItem> InventoryItemRenderer<I> getRenderer(
			I item) {
		return InventoryItemRenderer.getRenderer(item);
	}

	public Set<InventoryItem> getItems() {
		return new HashSet<InventoryItem>(items);
	}

	public void setItems(Set<InventoryItem> items) {
		this.items = new ArrayList<InventoryItem>(items);

		Collections.sort(this.items, new Comparator<InventoryItem>() {
			private final Collator collator = Collator.getInstance();

			public int compare(InventoryItem o1, InventoryItem o2) {
				int classCompare = collator.compare(
						getGroupTitle(o1.getClass()), getGroupTitle(o2
								.getClass()));

				if (classCompare != 0) {
					return classCompare;
				}

				return collator.compare(
						getRenderer(o1).getItemName(o1.getId()),
						getRenderer(o2).getItemName(o2.getId()));
			}
		});

		update();
	}
}
