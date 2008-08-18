/*
 * Footer.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Messages;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class Footer extends JPanel {
    public Footer() {
        setLayout(new FlowLayout(FlowLayout.TRAILING, 15, 2));
        add(new JLabel(Messages.get("footer")));
        add(new JLabel("http://www.jardas.de/drakensang/"));
    }
}
