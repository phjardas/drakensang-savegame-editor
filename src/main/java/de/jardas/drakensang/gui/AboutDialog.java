package de.jardas.drakensang.gui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import de.jardas.drakensang.shared.db.Messages;
import de.jardas.drakensang.shared.gui.Browser;


public class AboutDialog extends JDialog {
    public AboutDialog(Frame parent) {
        super(parent, Messages.get("title"), true);
        setLayout(new GridBagLayout());

        int row = 0;
        final Insets insets = new Insets(4, 8, 4, 8);

        final JLabel title = new JLabel(Messages.get("title"));
        title.setFont(title.getFont()
                           .deriveFont(Font.BOLD,
                title.getFont().getSize() * 1.2f));
        add(title,
            new GridBagConstraints(0, row++, 2, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        add(new JLabel(Messages.get("about.version")),
            new GridBagConstraints(0, row, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));
        add(new JLabel(Messages.get("version")),
            new GridBagConstraints(1, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        add(new JLabel(Messages.get("about.author")),
            new GridBagConstraints(0, row, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));
        add(new JLabel("Philipp Jardas"),
            new GridBagConstraints(1, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        add(new JLabel(Messages.get("about.website")),
            new GridBagConstraints(0, row, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        final JLabel website = new JLabel(
                "<html><a href=\"#\">http://www.jardas.de/drakensang2/</a></html>");
        website.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        website.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Browser.open("http://www.jardas.de/drakensang2/");
                }
            });
        add(website,
            new GridBagConstraints(1, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        add(new JLabel(Messages.get("about.translations")),
            new GridBagConstraints(0, row, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));
        add(new JLabel(
                Messages.get("about.translators")),
            new GridBagConstraints(1, row++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        final JButton ok = new JButton(new AbstractAction("OK") {
                    public void actionPerformed(ActionEvent arg0) {
                        setVisible(false);
                    }
                });
        add(ok,
            new GridBagConstraints(0, row++, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));

        pack();
        setLocationRelativeTo(parent);
    }
}
