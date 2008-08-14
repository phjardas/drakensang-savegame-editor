package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.jardas.drakensang.model.IntegerMap;

public class IntegerMapPanel<M extends IntegerMap> extends JPanel {
	private M values;
	private final Map<String, JLabel> labels = new HashMap<String, JLabel>();
	private final Map<String, JTextField> fields = new HashMap<String, JTextField>();
	private final ResourceBundle bundle;
	private int currentRow = 0;
	private int currentCol = 0;

	public IntegerMapPanel(String bundleName) {
		bundle = ResourceBundle.getBundle(getClass().getPackage().getName()
				+ "." + bundleName);
	}

	protected void update() {
		labels.clear();
		fields.clear();
		removeAll();
		setLayout(new GridBagLayout());

		addFields();

		repaint();
	}

	protected void addFields() {
		for (String key : values.getKeys()) {
			int value = values.get(key);
			addField(key, value);
		}
	}

	protected void addField(String key, int value) {
		String name;

		try {
			name = bundle.getString(key);
		} catch (MissingResourceException e) {
			name = key;
		}

		JLabel label = new JLabel(name);
		JTextField field = new JTextField(String.valueOf(value));

		labels.put(key, label);
		fields.put(key, field);

		Insets insets = new Insets(3, 6, 3, 6);
		add(label, new GridBagConstraints(2 * currentRow, currentCol, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0,
				0));
		add(field, new GridBagConstraints(2 * currentRow + 1, currentCol, 1, 1,
				1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				insets, 0, 0));

		advanceRowAndColumn();
	}

	private void advanceRowAndColumn() {
		currentRow = (currentRow + 1) % 2;

		if (currentRow == 0) {
			currentCol++;
		}
	}

	public M getValues() {
		return values;
	}

	public void setValues(M values) {
		if (values == this.values) {
			return;
		}

		this.values = values;
		update();
	}
}
