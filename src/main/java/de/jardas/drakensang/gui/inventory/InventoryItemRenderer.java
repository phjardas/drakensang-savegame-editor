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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.Main;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.InfoLabel;
import de.jardas.drakensang.gui.MainFrame;
import de.jardas.drakensang.model.InventoryItem;

public class InventoryItemRenderer {
	private static InventoryItemRenderer[] RENDERERS = { new WeaponRenderer(),
			new ShieldRenderer(), new ArmorRenderer(), new MoneyRenderer(),
			new InventoryItemRenderer() };

	public static InventoryItemRenderer getRenderer(InventoryItem item) {
		for (InventoryItemRenderer renderer : RENDERERS) {
			if (renderer.isApplicable(item)) {
				return renderer;
			}
		}

		throw new IllegalArgumentException("Can't render " + item);
	}

	protected List<JComponent> createComponents(final InventoryItem item) {
		List<JComponent> components = new ArrayList<JComponent>();
		components.add(renderLabel(item));
		components.add(renderCounter(item));
		components.add(renderSpecial(item));
		components.add(renderActions(item));

		return components;
	}

	public JComponent renderLabel(final InventoryItem item) {
		return new InfoLabel(getNameKey(item), getInfoKey(item), new ImageIcon(
				MainFrame.class.getResource(item.getIcon().toLowerCase()
						+ ".png")));
	}

	public String getNameKey(final InventoryItem item) {
		return "lookat_" + item.getId();
	}

	public String getInfoKey(final InventoryItem item) {
		return "infoid_" + item.getId();
	}

	public JComponent renderCounter(final InventoryItem item) {
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

	public JComponent renderSpecial(final InventoryItem item) {
		return null;
	}

	protected JComponent renderActions(final InventoryItem item) {
		JPanel panel = new JPanel();

		panel.add(new JButton(new AbstractAction("löschen") {
			public void actionPerformed(ActionEvent evt) {
				String message = item.isQuestItem() ? "Dies ist ein Quest-Gegenstand. Willst du ihn wirklich löschen?"
						: "Willst du diesen Gegenstand wirklich löschen?";
				int result = JOptionPane.showConfirmDialog(Main.getFrame(),
						message, "Gegenstand löschen",
						JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION) {
					item.getInventory().remove(item);
				}
			}
		}));

		// FIXME löschen ist noch nicht so recht interessant.
		return null;
	}

	protected String getItemName(String key) {
		return Messages.get("lookat_" + key);
	}

	public boolean isApplicable(InventoryItem item) {
		return true;
	}
}
