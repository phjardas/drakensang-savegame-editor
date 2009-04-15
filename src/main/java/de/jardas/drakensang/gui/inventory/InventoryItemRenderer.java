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

import de.jardas.drakensang.Main;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.InfoLabel;
import de.jardas.drakensang.gui.MainFrame;
import de.jardas.drakensang.model.inventory.InventoryItem;

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


public abstract class InventoryItemRenderer<I extends InventoryItem> {
    private static List<InventoryItemRenderer<?extends InventoryItem>> RENDERERS =
        new ArrayList<InventoryItemRenderer<?extends InventoryItem>>();

    static {
        RENDERERS.add(new WeaponRenderer());
        RENDERERS.add(new ShieldRenderer());
        RENDERERS.add(new ArmorRenderer());
        RENDERERS.add(new MoneyRenderer());
        RENDERERS.add(new DefaultRenderer());
    }

    public static <I extends InventoryItem> InventoryItemRenderer<I> getRenderer(
        I item) {
        for (InventoryItemRenderer<?extends InventoryItem> renderer : RENDERERS) {
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
        components.add(renderActions(item));

        return components;
    }

    public JComponent renderLabel(final I item) {
        return new InfoLabel(getNameKey(item), getInfoKey(item),
            new ImageIcon(MainFrame.class.getResource(item.getIcon()
                                                          .toLowerCase()
                    + ".png")));
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
            return new JLabel("" + item.getCount());
        }

        final JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                    item.getCount(), 1, item.getMaxCount(), 1));
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

    protected JComponent renderActions(final I item) {
        JPanel panel = new JPanel();

        panel.add(new JButton(new AbstractAction("löschen") {
                public void actionPerformed(ActionEvent evt) {
                    String message = item.isQuestItem()
                            ? "Dies ist ein Quest-Gegenstand. Willst du ihn wirklich löschen?"
                            : "Willst du diesen Gegenstand wirklich löschen?";
                    int result = JOptionPane.showConfirmDialog(
                                Main.getFrame(), message, "Gegenstand löschen",
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
