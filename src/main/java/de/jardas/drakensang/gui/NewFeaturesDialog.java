package de.jardas.drakensang.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import de.jardas.drakensang.FeatureHistory.Feature;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.util.WordWrap;

public class NewFeaturesDialog extends JDialog {
	private JToolBar toolbar;
	private FeatureListPanel list;
	private JButton okButton;

	public NewFeaturesDialog(Feature[] features, Frame parent) {
		super(parent, Messages.get("title"), true);

		setLayout(new BorderLayout());

		list = new FeatureListPanel(features);

		okButton = new JButton(new AbstractAction(Messages.get("Weiter")) {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.add(okButton);

		final JLabel title = new JLabel("  "
				+ Messages.get("newfeatures.title"));
		title.setFont(title.getFont().deriveFont(Font.BOLD,
				title.getFont().getSize() * 1.5f));

		add(title, BorderLayout.NORTH);
		add(new JScrollPane(list), BorderLayout.CENTER);
		add(toolbar, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(okButton);

		pack();
		setLocationRelativeTo(parent);
	}

	private class FeatureListPanel extends JPanel {
		public FeatureListPanel(final Feature[] features) {
			super();
			setLayout(new GridBagLayout());

			int row = 0;

			for (final Feature feature : features) {
				final String description = feature.getDescription(Locale
						.getDefault().getLanguage());
				final JLabel item = new JLabel(WordWrap
						.addHtmlNewlines(description));
				// item.setBorder(BorderFactory.createEtchedBorder());

				add(item, new GridBagConstraints(0, row++, 1, 1, 1, 0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10,
								10), 0, 0));
			}
		}
	}
}
