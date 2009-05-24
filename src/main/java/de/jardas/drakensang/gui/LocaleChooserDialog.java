package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.LocaleOption;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.util.WordWrap;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;


public abstract class LocaleChooserDialog extends JDialog {
    private final Locale[] locales = LocaleOption.getAvailableLocales();
    private final JComboBox chooser = new JComboBox(new LocaleChooserModel());

    public LocaleChooserDialog() {
        super((Frame) null, Messages.get("languagechooser.title"));

        setLayout(new GridBagLayout());

        int row = 0;
        int col = 0;

        add(new JLabel(Messages.get("languagechooser.welcome")),
            new GridBagConstraints(col++, row, 3, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 10, 10), 0, 0));

        row++;
        col = 0;

        add(chooser,
            new GridBagConstraints(col++, row, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 6), 0, 0));

        add(new JButton(new AbstractAction(Messages.get("languagechooser.ok")) {
                public void actionPerformed(ActionEvent e) {
                    onLocaleChosen(locales[chooser.getSelectedIndex()]);
                }
            }),
            new GridBagConstraints(col++, row, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        add(new JButton(new AbstractAction(Messages.get("languagechooser.exit")) {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            }),
            new GridBagConstraints(col++, row, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 3, 0, 10), 0, 0));

        row++;
        col = 0;

        final JLabel notice = new JLabel(WordWrap.addHtmlNewlines(Messages.get(
                        "languagechooser.notice")));
        notice.setFont(notice.getFont().deriveFont(Font.PLAIN));
        add(notice,
            new GridBagConstraints(col++, row, 3, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 10, 10), 0, 0));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public abstract void onLocaleChosen(Locale locale);

    private class LocaleChooserModel extends DefaultComboBoxModel {
        public LocaleChooserModel() {
            super(locales);
        }

        @Override
        public Object getElementAt(int index) {
            return Messages.get("locale." + locales[index]);
        }
    }
}
