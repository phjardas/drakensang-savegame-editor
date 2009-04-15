package de.jardas.drakensang.gui;

import de.jardas.drakensang.Main;
import de.jardas.drakensang.dao.Messages;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.MutableComboBoxModel;

public abstract class EnumComboBox<E extends Enum<E>> extends JComboBox {
	private E current = null;

	public EnumComboBox(E[] enumeration, final E selected) {
		super();
		setModel(new DefaultComboBoxModel());

		replaceValues(enumeration, selected);

		addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					LocalizedEnumItem selected = (LocalizedEnumItem) evt
							.getItem();
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

	public void replaceValues(E[] enumeration, E selected) {
		removeAllItems();

		for (E item : enumeration) {
			if (accept(item)) {
				getMutableModel().addElement(
						new LocalizedEnumItem(item, getLabel(toString(item))));
			}
		}

		setSelected(selected);
	}

	private MutableComboBoxModel getMutableModel() {
		return ((MutableComboBoxModel) getModel());
	}

	public void setSelected(E selected) {
		if (selected != null) {
			for (int i = 0; i < getMutableModel().getSize(); i++) {
				LocalizedEnumItem el = (LocalizedEnumItem) getMutableModel()
						.getElementAt(i);

				if (el.getItem() == selected) {
					setSelectedIndex(i);
					this.current = selected;
				}
			}
		}
	}

	private String getLabel(String key) {
		return Messages.get(key);
	}

	protected abstract void valueChanged(E item) throws ChangeRejectedException;

	protected boolean accept(E item) {
		return toString(item) != null;
	}

	protected String toString(E item) {
		return item.toString();
	}

	private class LocalizedEnumItem {
		private final E item;
		private final String label;

		public LocalizedEnumItem(E item, String label) {
			super();
			this.item = item;
			this.label = label;
		}

		public E getItem() {
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
