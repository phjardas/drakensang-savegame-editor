/*
 * Test.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.savegame;

import java.awt.Image;

import java.io.File;

import javax.swing.ImageIcon;


public class SavegameIcon extends ImageIcon {
    public SavegameIcon(Savegame savegame) {
        this(savegame, 1000);
    }

    public SavegameIcon(Savegame savegame, int maxWidth) {
        File imageFile = new File(savegame.getFile().getParentFile(),
                savegame.getFile().getName().replace(".dsa", ".jpg"));
        ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());

        int width = (icon.getIconWidth() > maxWidth) ? maxWidth
                                                     : icon.getIconWidth();
        int height = (width * 3) / 4;

        Image scaled = icon.getImage()
                           .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        setImage(scaled);
    }
}
