package de.jardas.drakensang.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.jardas.drakensang.dao.Messages;

public class EnumComboBoxModel<E extends Enum<E>> extends DefaultComboBoxModel {
	private EnumComboBoxModel(E[] enumeration, Listener<E> listener) {
		super();

		for (E item : enumeration) {
			if (listener.accept(item)) {
				addElement(new LocalizedEnumItem<E>(item, getLabel(listener.toString(item))));
			}
		}
	}

	public static <E extends Enum<E>> JComboBox createComboBox(E[] enumeration,
			E selected, final Listener<E> listener) {
		EnumComboBoxModel<E> model = new EnumComboBoxModel<E>(enumeration, listener);
		JComboBox box = new JComboBox(model);

		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					@SuppressWarnings("unchecked")
					LocalizedEnumItem<E> selected = (LocalizedEnumItem<E>) e
							.getItem();
					listener.valueChanged(selected.getItem());
				}
			}
		});

		if (selected != null) {
			for (int i = 0; i < model.getSize(); i++) {
				@SuppressWarnings("unchecked")
				LocalizedEnumItem<E> el = (LocalizedEnumItem<E>) model.getElementAt(i);
				if (el.getItem() == selected) {
					box.setSelectedIndex(i);
				}
			}
		}

		return box;
	}

	public String getLabel(String key) {
		return Messages.get(key);
	}

	private static class LocalizedEnumItem<E extends Enum<E>> {
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

	public abstract static class Listener<E extends Enum<E>> {
		public abstract void valueChanged(E item);
		
		public boolean accept(E item) {
			return toString(item) != null;
		}
		
		public String toString(E item) {
			return item.toString();
		}
	}
}
