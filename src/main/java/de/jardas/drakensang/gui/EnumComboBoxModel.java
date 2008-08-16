package de.jardas.drakensang.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.jardas.drakensang.dao.Messages;

public class EnumComboBoxModel<E extends Enum<E>> extends DefaultComboBoxModel {
	private EnumComboBoxModel(E[] enumeration) {
		super();

		for (E item : enumeration) {
			addElement(new LocalizedEnumItem<E>(item, getLabel(item)));
		}
	}

	public static <E extends Enum<E>> JComboBox createComboBox(E[] enumeration,
			E selected, final Listener<E> listener) {
		JComboBox box = new JComboBox(new EnumComboBoxModel<E>(enumeration));

		box.addItemListener(new ItemListener() {
			@Override
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
			box.setSelectedIndex(selected.ordinal());
		}

		return box;
	}

	public String getLabel(E item) {
		return Messages.get(item.name());
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

	public static interface Listener<E extends Enum<E>> {
		void valueChanged(E item);
	}
}
