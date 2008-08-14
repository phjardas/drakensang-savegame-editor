package de.jardas.drakensang.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import de.jardas.drakensang.model.InventoryItem;

public class InventoryItemsPanel<I extends InventoryItem> extends JPanel {
	private JTable list = new JTable();
	private List<I> items;

	public InventoryItemsPanel() {
		setLayout(new BorderLayout());
		add(new JScrollPane(list), BorderLayout.CENTER);
	}

	private void update() {
		list.setModel(createTableModel());
	}

	private TableModel createTableModel() {
		return new AbstractTableModel() {
			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public int getRowCount() {
				return items.size();
			}

			@Override
			public Object getValueAt(int row, int col) {
				I item = items.get(row);

				switch (col) {
				case 0:
					return item.getId();
				case 1:
					return item.getCount();
				default:
					return null;
				}
			}
		};
	}

	public Set<I> getItems() {
		return new HashSet<I>(items);
	}

	public void setItems(Set<I> items) {
		this.items = new ArrayList<I>(items);
		update();
	}
}
