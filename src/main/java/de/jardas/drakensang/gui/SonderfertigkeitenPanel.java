package de.jardas.drakensang.gui;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;

import de.jardas.drakensang.dao.Static;
import de.jardas.drakensang.model.Sonderfertigkeiten;

public class SonderfertigkeitenPanel extends
		IntegerMapPanel<Sonderfertigkeiten> {
	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected String getGroupKey(String key) {
		return Static.get("AttrCategory", key, "AtAttr", "_Template_Attacks");
	}

	@Override
	protected String getLocalKey(String key) {
		return Static.get("Name", key, "AtAttr", "_Template_Attacks");
	}

	@Override
	protected String getInfoKey(String key) {
		return Static.get("Description", key, "AtAttr", "_Template_Attacks");
	}
    
    @Override
    protected ImageIcon getInfoIcon(String key) {
    	String icon = Static.get("IconBrush", key, "AtAttr", "_Template_Attacks");
		URL url = MainFrame.class.getResource(icon + ".png");
		return new ImageIcon(url);
    }

	@Override
	protected JComponent createField(final String key, int value) {
		final JCheckBox box = new JCheckBox();
		box.setSelected(value >= 0);
		box.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				getValues().set(key, box.isSelected() ? 0 : -500);
			}
		});

		return box;
	}
}
