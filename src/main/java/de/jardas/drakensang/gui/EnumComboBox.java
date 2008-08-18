/*
 * EnumComboBox.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Messages;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;


public abstract class EnumComboBox<E extends Enum<E>> extends JComboBox {
    public EnumComboBox(E[] enumeration, E selected) {
        super();
        setModel(new DefaultComboBoxModel());
        
        replaceValues(enumeration, selected);

        addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        @SuppressWarnings("unchecked")
                        LocalizedEnumItem selected = (LocalizedEnumItem) e
                            .getItem();
                        valueChanged(selected.getItem());
                    }
                }
            });
    }

    public void replaceValues(E[] enumeration, E selected) {
        for (E item : enumeration) {
            if (accept(item)) {
                getMutableModel()
                    .addElement(new LocalizedEnumItem(item,
                        getLabel(toString(item))));
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
                @SuppressWarnings("unchecked")
                LocalizedEnumItem el = (LocalizedEnumItem) getMutableModel()
                                                               .getElementAt(i);

                if (el.getItem() == selected) {
                    setSelectedIndex(i);
                }
            }
        }
    }

    private String getLabel(String key) {
        return Messages.get(key);
    }

    protected abstract void valueChanged(E item);

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
}
