package de.jardas.drakensang.gui;

import de.jardas.drakensang.model.IntegerMap;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public abstract class IntegerMapPanel<M extends IntegerMap> extends JPanel {
    private M values;
    private final Map<String, JLabel> labels = new HashMap<String, JLabel>();
    private final Map<String, JComponent> fields = new HashMap<String, JComponent>();
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

    protected void addField(final String key, int value) {
        String name;

        try {
            name = bundle.getString(key);
        } catch (MissingResourceException e) {
            name = key;
        }

        final JLabel label = new JLabel(name);
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
        add(label,
            new GridBagConstraints(2 * currentRow, currentCol, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
        add(spinner,
            new GridBagConstraints((2 * currentRow) + 1, currentCol, 1, 1, 1,
                0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));

        advanceRowAndColumn();
    }

    protected void handleChange(String key, int value) {
        values.set(key, value);
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
