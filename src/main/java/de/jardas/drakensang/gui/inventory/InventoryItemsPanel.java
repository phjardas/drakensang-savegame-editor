package de.jardas.drakensang.gui.inventory;

import de.jardas.drakensang.model.InventoryItem;

import java.awt.BorderLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


public class InventoryItemsPanel<I extends InventoryItem> extends JPanel {
    private static final ResourceBundle BUNDLE = ResourceBundle
        .getBundle(InventoryItemsPanel.class.getPackage().getName()
            + ".inventory");
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
                public int getColumnCount() {
                    return InventoryItemsPanel.this.getColumnCount();
                }

                public int getRowCount() {
                    return items.size();
                }

                public Object getValueAt(int row, int col) {
                    I item = items.get(row);

                    return renderColumn(item, col);
                }
            };
    }

    protected int getColumnCount() {
        return 2;
    }

    protected Object renderColumn(I item, int col) {
        switch (col) {
        case 0:

            try {
                return BUNDLE.getString(item.getId());
            } catch (MissingResourceException e) {
                return item.getId();
            }

        case 1:
            return item.getCount();

        default:
            throw new IllegalArgumentException("Illegal column " + col);
        }
    }

    public Set<I> getItems() {
        return new HashSet<I>(items);
    }

    public void setItems(Set<I> items) {
        this.items = new ArrayList<I>(items);
        update();
    }
}
