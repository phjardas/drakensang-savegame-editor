package de.jardas.drakensang.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import de.jardas.drakensang.dao.Messages;

public class InfoLabel extends JComponent {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
	.getLogger(InfoLabel.class);
	private static final ImageIcon HELP_ICON = new ImageIcon(InfoLabel.class
			.getResource("images/help.png"));

	public InfoLabel(String key) {
		this(key, null);
	}

	public InfoLabel(String key, String infoKey) {
		super();

		setLayout(new GridBagLayout());
		final String name = Messages.get(key);
		
		if (name.contains("obsolete")) {
			LOG.warn("Obsolete: " + key);
		}

		add(new JLabel(name), new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		if (infoKey != null) {
			try {
				final String info = addNewLines(Messages.getRequired(infoKey));

				JComponent anchor = new JLabel("?");
				anchor.setForeground(Color.BLUE);
				anchor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				anchor.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JOptionPane.showMessageDialog(InfoLabel.this, info,
								name, JOptionPane.INFORMATION_MESSAGE);
					}
				});

				add(anchor, new GridBagConstraints(1, 0, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 3, 0, 0), 0, 0));
			} catch (MissingResourceException e) {
				LOG.warn("Missing info for '" + infoKey + "'.");
			}
		}
	}

	private String addNewLines(String in) {
		StringBuffer out = new StringBuffer();
		String[] words = in.split("\\s+");
		int line = 0;

		for (String word : words) {
			if (line + word.length() + 1 > 80) {
				out.append("\n");
				line = 0;
			} else if (out.length() > 0) {
				out.append(" ");
				line++;
			}

			out.append(word.replace("\\n", "\n"));
			line += word.length();
		}

		return out.toString();
	}
}