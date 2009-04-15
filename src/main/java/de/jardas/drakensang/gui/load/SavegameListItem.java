/*
 * SavegameListItem.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.load;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.savegame.Savegame;
import de.jardas.drakensang.model.savegame.SavegameIcon;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SavegameListItem extends JPanel {
    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
            @Override
            protected DateFormat initialValue() {
                return DateFormat.getDateTimeInstance();
            }
        };

    private Color regularBackground;

    public SavegameListItem(final Savegame savegame,
        final SavegameListener savegameListener) {
        super();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEtchedBorder());

        int row = 0;

        add(new JLabel(savegame.getName() + " ("
                + savegame.getDirectory().getName() + ")"),
            new GridBagConstraints(1, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        add(new JLabel(savegame.getHero()),
            new GridBagConstraints(1, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        add(new JLabel("Level " + savegame.getLevel()),
            new GridBagConstraints(1, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        add(new JLabel(Messages.get(savegame.getWorldMapKey())),
            new GridBagConstraints(1, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        add(new JLabel(DATE_FORMAT.get().format(savegame.getChangeDate())),
            new GridBagConstraints(1, row++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 6, 3, 6), 0, 0));

        add(new JLabel(new SavegameIcon(savegame, 150)),
            new GridBagConstraints(0, 0, 1, row, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(6, 6, 6, 6), 0, 0));

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    savegameListener.loadSavegame(savegame);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (regularBackground == null) {
                        regularBackground = getBackground();
                    }

                    setBackground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(regularBackground);
                }
            });
    }
}
