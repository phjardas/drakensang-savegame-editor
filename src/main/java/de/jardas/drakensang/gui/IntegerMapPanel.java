package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.IntegerMap;

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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;


public abstract class IntegerMapPanel<M extends IntegerMap> extends JPanel {
    private static final int COLUMNS = 2;
    private final List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private final Map<String, JComponent> labels = new HashMap<String, JComponent>();
    private final Map<String, JComponent> fields = new HashMap<String, JComponent>();
    private final Map<String, JComponent> specials = new HashMap<String, JComponent>();
    private M values;

    protected void update() {
        labels.clear();
        fields.clear();
        specials.clear();

        removeAll();
        setLayout(new GridBagLayout());

        addFields();
    }

    protected boolean isVisible(String key) {
        return true;
    }

    protected void addFields() {
        List<String> keys = new ArrayList<String>();

        for (String key : Arrays.asList(values.getKeys())) {
            if (isVisible(key)) {
                keys.add(key);
            }
        }

        sortKeys(keys);

        JComponent parent = null;
        String currentGroupKey = null;
        Status status = new Status();
        int parentRow = 0;

        for (String key : keys) {
            String groupKey = getGroupKey(key);

            if (isGrouped()
                    && ((parent == null) || (currentGroupKey == null)
                    || !currentGroupKey.equals(groupKey))) {
                parent = new JPanel();
                parent.setLayout(new GridBagLayout());
                parent.setBorder(BorderFactory.createTitledBorder(Messages.get(
                            groupKey)));
                status = new Status();
                add(parent,
                    new GridBagConstraints(0, parentRow++, 1, 1, 0, 0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));
                currentGroupKey = groupKey;
            }

            int value = values.get(key);
            addField(key, value, isGrouped() ? parent : this, status,
                createSpecial(key));
            status.advance();
        }

        add(new JLabel(),
            new GridBagConstraints(isGrouped() ? 1 : COLUMNS,
                isGrouped() ? parentRow : status.getRow(), 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
    }

    protected JComponent createSpecial(String key) {
        return null;
    }

    protected void sortKeys(List<String> keys) {
        Collections.sort(keys, getKeyComparator());
    }

    protected Comparator<String> getKeyComparator() {
        return new Comparator<String>() {
                private final Collator collator = Collator.getInstance();

                public int compare(String s0, String s1) {
                    if (isGrouped()) {
                        String g0 = Messages.get(getGroupKey(s0));
                        String g1 = Messages.get(getGroupKey(s1));
                        int groupCompare = collator.compare(g0, g1);

                        if (groupCompare != 0) {
                            return groupCompare;
                        }
                    }

                    return collator.compare(getName(s0), getName(s1));
                }
            };
    }

    protected boolean isGrouped() {
        return false;
    }

    protected String getGroupKey(String key) {
        return null;
    }

    protected void addField(final String key, int value, JComponent parent,
        Status status, JComponent special) {
        final JComponent label = createLabel(key);
        final JComponent spinner = createField(key, value);

        Insets insets = new Insets(3, 6, 3, 6);
        parent.add(label,
            new GridBagConstraints(status.getColumn(), status.getRow(), 1, 1,
                0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets,
                0, 0));
        parent.add(spinner,
            new GridBagConstraints(status.getColumn() + 1, status.getRow(), 1,
                1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, insets, 0, 0));

        if (special != null) {
            parent.add(special,
                new GridBagConstraints(status.getColumn() + 2, status.getRow(),
                    1, 1, 0, 0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, insets, 0, 0));
        }

        labels.put(key, label);
        fields.put(key, spinner);
        specials.put(key, special);
    }

    protected JComponent createField(final String key, int value) {
        final JSpinner spinner = new JSpinner(new SpinnerNumberModel(value,
                    -1000, 100, 1));
        spinner.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    final int val = ((Number) spinner.getValue()).intValue();
                    handleChange(key, val);

                    for (ChangeListener changeListener : changeListeners) {
                        changeListener.valueChanged(key, val);
                    }
                }
            });

        return spinner;
    }

    protected InfoLabel createLabel(final String key) {
        return new InfoLabel(getLocalKey(key), getInfoKey(key), getInfoIcon(key));
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
    
    protected ImageIcon getInfoIcon(String key) {
    	return null;
    }

    protected void handleChange(String key, int value) {
        values.set(key, value);
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

    public void addChangeListener(ChangeListener changeListener) {
        this.changeListeners.add(changeListener);
    }

    public Map<String, JComponent> getFields() {
        return this.fields;
    }

    public Map<String, JComponent> getLabels() {
        return this.labels;
    }

    public Map<String, JComponent> getSpecials() {
        return this.specials;
    }

    public static interface ChangeListener {
        void valueChanged(String key, int value);
    }

    private static class Status {
        private int column = 0;
        private int row = 0;

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
        }

        public void reset() {
            column = 0;
            row = 0;
        }

        public void advance() {
            column += 2;

            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
        }
    }
}
