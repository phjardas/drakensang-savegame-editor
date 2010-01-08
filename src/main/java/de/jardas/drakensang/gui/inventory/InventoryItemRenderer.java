package de.jardas.drakensang.gui.inventory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.gui.InfoLabel;
import de.jardas.drakensang.shared.model.inventory.InventoryItem;

public abstract class InventoryItemRenderer<I extends InventoryItem> {
	private static List<InventoryItemRenderer<? extends InventoryItem>> RENDERERS = new ArrayList<InventoryItemRenderer<? extends InventoryItem>>();

	static {
		RENDERERS.add(new WeaponRenderer());
		RENDERERS.add(new ShieldRenderer());
		RENDERERS.add(new ArmorRenderer());
		RENDERERS.add(new DefaultRenderer());
	}

	public static <I extends InventoryItem> InventoryItemRenderer<I> getRenderer(
			I item) {
		for (InventoryItemRenderer<? extends InventoryItem> renderer : RENDERERS) {
			if (renderer.isApplicable(item)) {
				@SuppressWarnings("unchecked")
				final InventoryItemRenderer<I> r = (InventoryItemRenderer<I>) renderer;

				return r;
			}
		}

		throw new IllegalArgumentException("Can't render " + item);
	}

	protected List<JComponent> createComponents(final I item) {
		List<JComponent> components = new ArrayList<JComponent>();
		components.add(renderLabel(item));
		components.add(renderCounter(item));
		components.add(renderSpecial(item));

		return components;
	}

	public JComponent renderLabel(final I item) {
		final InfoLabel label = new InfoLabel(getNameKey(item),
				getInfoKey(item));

		if (item.getSlot() != null) {
			final JLabel nameLabel = label.getNameLabel();
			final String text = nameLabel.getText() + " ("
					+ Messages.get(item.getSlot().name()) + ")";
			nameLabel.setText(text);
			nameLabel.setForeground(Color.BLUE.darker());
		}

		return label;
	}

	public String renderInlineInfo(final I item) {
		return null;
	}

	public String getNameKey(final I item) {
		return "lookat_" + item.getId();
	}

	public String getInfoKey(final I item) {
		return "infoid_" + item.getId();
	}

	public JComponent renderCounter(final I item) {
		if (item.getMaxCount() <= 1) {
			return null;
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

	public JComponent renderSpecial(final I item) {
		return null;
	}

	protected String getItemName(String key) {
		return Messages.get("lookat_" + key);
	}

	public boolean isApplicable(InventoryItem item) {
		return true;
	}
}
