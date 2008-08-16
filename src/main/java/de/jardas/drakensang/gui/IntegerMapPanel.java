package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.IntegerMap;

public abstract class IntegerMapPanel<M extends IntegerMap> extends JPanel {
	private M values;
	private final Map<String, JComponent> labels = new HashMap<String, JComponent>();
	private final Map<String, JComponent> fields = new HashMap<String, JComponent>();
	private int currentRow = 0;
	private int currentCol = 0;

	protected void update() {
		labels.clear();
		fields.clear();
		removeAll();
		setLayout(new GridBagLayout());
		currentRow = 0;
		currentCol = 0;

		addFields();

		repaint();
	}

	protected void addFields() {
		List<String> keys = new ArrayList<String>(Arrays.asList(values
				.getKeys()));

		sortKeys(keys);

		for (String key : values.getKeys()) {
			int value = values.get(key);
			addField(key, value);
		}

		add(new JLabel(), new GridBagConstraints(4, currentRow + 1, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
	}

	protected void sortKeys(List<String> keys) {
		Collections.sort(keys, getKeyComparator());
	}

	protected Comparator<String> getKeyComparator() {
		return new Comparator<String>() {
			private final Collator collator = Collator.getInstance();

			@Override
			public int compare(String s0, String s1) {
				return collator.compare(getName(s0), getName(s1));
			}
		};
	}

	protected void addField(final String key, int value) {
		final InfoLabel label = new InfoLabel(getLocalKey(key), getInfoKey(key));

		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(value,
				-1000, 50, 1));
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				handleChange(key, ((Number) spinner.getValue()).intValue());
			}
		});

		labels.put(key, label);
		fields.put(key, spinner);

		Insets insets = new Insets(3, 6, 3, 6);
		add(label, new GridBagConstraints(2 * currentCol, currentRow, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0,
				0));
		add(spinner, new GridBagConstraints((2 * currentCol) + 1, currentRow,
				1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, insets, 0, 0));

		advanceRowAndColumn();
	}

	protected String getName(final String key) {
		String localKey = getLocalKey(key);
		return Messages.get(localKey);
	}

	protected String getLocalKey(String key) {
		return key;
	}

	protected String getInfoKey(String key) {
		return null;
	}

	protected void handleChange(String key, int value) {
		values.set(key, value);
	}

	private void advanceRowAndColumn() {
		currentCol = (currentCol + 1) % 2;

		if (currentCol == 0) {
			currentRow++;
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
