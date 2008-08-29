/*
 * SavegameListPanel.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.load;

import de.jardas.drakensang.model.savegame.Savegame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.List;

import javax.swing.JPanel;


public class SavegameListPanel extends JPanel {
    public SavegameListPanel(final List<Savegame> savegames,
        final SavegameListener savegameListener) {
        super();
        setLayout(new GridBagLayout());

        int row = 0;

        for (Savegame savegame : savegames) {
            add(new SavegameListItem(savegame, savegameListener),
                new GridBagConstraints(0, row++, 1, 1, 1, 0,
                    GridBagConstraints.NORTHWEST,
                    GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));
        }
    }
}
