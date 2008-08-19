package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Static;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.Talente;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.net.URL;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;


public class TalentePanel extends IntegerMapPanel<Talente> {
    private Character character;

    @Override
    protected boolean isVisible(String key) {
        return !key.startsWith("TaATAdjust");
    }

    @Override
    protected boolean isGrouped() {
        return true;
    }

    @Override
    protected String getGroupKey(String key) {
        return Static.get("TaCategory", key, "TaAttr", "_Template_Talent");
    }

    @Override
    protected String getLocalKey(String key) {
        return Static.get("Name", key, "TaAttr", "_Template_Talent");
    }

    @Override
    protected JComponent createSpecial(String key) {
        return new JLabel(createAttributesLabel(key));
    }

    public void onAttributeChange(String attributeKey, int value) {
        for (Map.Entry<String, JComponent> entry : getSpecials().entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            final String key = entry.getKey();
            final JLabel label = (JLabel) entry.getValue();
            label.setText(createAttributesLabel(key));
        }
    }

    private String createAttributesLabel(String key) {
        final String[] attrs = Talente.getAttributes(key);
        StringBuffer attBuffer = new StringBuffer();

        for (String attr : attrs) {
            if ((attr != null) && (attr.trim().length() > 0)) {
                if (attBuffer.length() > 0) {
                    attBuffer.append(", ");
                }

                attBuffer.append(attr);
                attBuffer.append("=");
                attBuffer.append(getCharacter().getAttribute().get(attr));
            }
        }

        return attBuffer.toString();
    }

    @Override
    protected JComponent createField(final String key, int value) {
        JComponent taField = super.createField(key, value);
        String adjustKey = getAdjustAttributeKey(key);

        if (getValues().contains(adjustKey)) {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new GridBagLayout());
            wrapper.add(taField,
                new GridBagConstraints(0, 0, 1, 1, 0, 0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

            int adjustValue = getValues().get(adjustKey);

            if (adjustValue < -100) {
                adjustValue = 0;
            }

            final JSpinner adjust = new JSpinner(new SpinnerNumberModel(
                        adjustValue, -100, 100, 1));
            adjust.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        getValues()
                            .set(key, ((Number) adjust.getValue()).intValue());
                    }
                });
            wrapper.add(adjust,
                new GridBagConstraints(1, 0, 1, 1, 0, 0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 6, 0, 0), 0, 0));

            wrapper.add(new InfoLabel(null, "talent.adjustinfo"),
                new GridBagConstraints(2, 0, 1, 1, 0, 0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 6, 0, 0), 0, 0));

            return wrapper;
        }

        return taField;
    }

    private String getAdjustAttributeKey(final String key) {
        try {
            return Static.get("TaATAdjustAttr", key, "TaAttr",
                "_Template_Talent");
        } catch (MissingResourceException e) {
            return null;
        }
    }
    
    @Override
    protected String getInfoKey(String key) {
        return Static.get("Description", key, "TaAttr", "_Template_Talent");
    }
    
    @Override
    protected ImageIcon getInfoIcon(String key) {
    	String icon = Static.get("IconBrush", key, "TaAttr", "_Template_Talent");
		URL url = MainFrame.class.getResource(icon + ".png");
		return new ImageIcon(url);
    }

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        setValues(character.getTalente());
    }
}
