package de.jardas.drakensang.gui;

import java.net.URL;

import javax.swing.ImageIcon;

import de.jardas.drakensang.dao.Static;
import de.jardas.drakensang.model.Zauberfertigkeiten;

public class ZauberPanel extends IntegerMapPanel<Zauberfertigkeiten> {
	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected String getGroupKey(String key) {
		return Static.get("CategoryName", key, "ZaAttr", "_Template_Zauber");
	}

	@Override
	protected String getLocalKey(String key) {
		return Static.get("Name", key, "ZaAttr", "_Template_Zauber");
	}

	@Override
	protected String getInfoKey(String key) {
		return Static.get("Description", key, "ZaAttr", "_Template_Zauber");
	}
    
    @Override
    protected ImageIcon getInfoIcon(String key) {
    	String icon = Static.get("ZaIcon", key, "ZaAttr", "_Template_Zauber");
		URL url = MainFrame.class.getResource(icon + ".png");
		return new ImageIcon(url);
    }
}
