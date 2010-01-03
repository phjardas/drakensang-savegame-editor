package de.jardas.drakensang.gui;

import de.jardas.drakensang.Main;
import de.jardas.drakensang.dao.Messages;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.MutableComboBoxModel;

public abstract class StringComboBox extends JComboBox {
	private String current = null;

	public StringComboBox(String[] items, String selected) {
		super();
		setModel(new DefaultComboBoxModel());

		replaceValues(items, selected);

		addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					LocalizedItem selected = (LocalizedItem) evt.getItem();
					try {
						valueChanged(selected.getItem());
						current = selected.getItem();
					} catch (ChangeRejectedException e) {
						JOptionPane.showMessageDialog(Main.getFrame(), e
								.getMessage(), e.getTitle(),
								JOptionPane.ERROR_MESSAGE);
						setSelected(current);
					}
				}
			}
		});
	}

	public void replaceValues(String[] enumeration, String selected) {
		removeAllItems();

		for (String item : enumeration) {
			if (accept(item)) {
				getMutableModel().addElement(
						new LocalizedItem(item, getLabel(toString(item))));
			}
		}

		setSelected(selected);
	}

	private MutableComboBoxModel getMutableModel() {
		return ((MutableComboBoxModel) getModel());
	}

	public void setSelected(String selected) {
		if (selected != null) {
			for (int i = 0; i < getMutableModel().getSize(); i++) {
				LocalizedItem el = (LocalizedItem) getMutableModel()
						.getElementAt(i);

				if (el.getItem().equals(selected)) {
					setSelectedIndex(i);
					this.current = selected;
				}
			}
		}
	}

	protected String getLabel(String key) {
		return Messages.get(key);
	}

	protected abstract void valueChanged(String item)
			throws ChangeRejectedException;

	protected boolean accept(String item) {
		return toString(item) != null;
	}

	protected String toString(String item) {
		return item.toString();
	}

	private class LocalizedItem {
		private final String item;
		private final String label;

		public LocalizedItem(String item, String label) {
			super();
			this.item = item;
			this.label = label;
		}

		public String getItem() {
			return item;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	public static class ChangeRejectedException extends Exception {
		private final String title;

		public ChangeRejectedException(String message, String title) {
			super(message);
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}
}
