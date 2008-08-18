/*
 * InventoryItemRenderer.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.inventory;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.InfoLabel;
import de.jardas.drakensang.model.InventoryItem;

public class InventoryItemRenderer {
	protected List<JComponent> createComponents(final InventoryItem item) {
		List<JComponent> components = new ArrayList<JComponent>();
		components.add(renderLabel(item));
		components.add(renderCounter(item));
		components.add(renderSpecial(item));

		return components;
	}

	protected JComponent renderLabel(final InventoryItem item) {
		return new InfoLabel(getNameKey(item), getInfoKey(item), new ImageIcon(
				getClass().getResource(
						"../" + item.getIcon().toLowerCase() + ".png")));
	}

	protected String getNameKey(final InventoryItem item) {
		return "lookat_" + item.getId();
	}

	protected String getInfoKey(final InventoryItem item) {
		return "infoid_" + item.getId();
	}

	protected JComponent renderCounter(final InventoryItem item) {
		if (item.getMaxCount() <= 1) {
			return new JLabel("" + item.getCount());
		}

		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(item
				.getCount(), 1, item.getMaxCount(), 1));
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				item.setCount(((Number) spinner.getValue()).intValue());
			}
		});

		return spinner;
	}

	protected JComponent renderSpecial(final InventoryItem item) {
		return null;
	}

	protected String getItemName(String key) {
		return Messages.get("lookat_" + key);
	}

	public boolean isApplicable(InventoryItem item) {
		return true;
	}
}
