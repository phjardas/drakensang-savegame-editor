package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Messages;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class Footer extends JPanel {
    public Footer() {
        setLayout(new FlowLayout(FlowLayout.TRAILING, 15, 2));
        add(new JLabel(Messages.get("footer")));
    }
}
