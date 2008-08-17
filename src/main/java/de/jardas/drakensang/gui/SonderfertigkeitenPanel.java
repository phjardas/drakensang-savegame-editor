package de.jardas.drakensang.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.model.Sonderfertigkeiten;

public class SonderfertigkeitenPanel extends
		IntegerMapPanel<Sonderfertigkeiten> {
	private static Map<String, String> GROUPS = new HashMap<String, String>();

	static {
		GROUPS.put("SF_Ausweichen1", "defensiv");
		GROUPS.put("SF_Ausweichen2", "defensiv");
		GROUPS.put("SF_Ausweichen3", "defensiv");
		GROUPS.put("SF_Ausdauernd1", "defensiv");
		GROUPS.put("SF_Ausdauernd2", "defensiv");
		GROUPS.put("SF_Ausdauernd3", "defensiv");
		GROUPS.put("SF_Ruestungsgewoehnung1", "defensiv");
		GROUPS.put("SF_Ruestungsgewoehnung2", "defensiv");
		GROUPS.put("SF_Ruestungsgewoehnung3", "defensiv");
		GROUPS.put("SF_Schildkampf1", "defensiv");
		GROUPS.put("SF_Schildkampf2", "defensiv");
		GROUPS.put("SF_Schildkampf3", "defensiv");

		GROUPS.put("SF_DefensiverKampfstil1", "nahkampf");
		GROUPS.put("SF_DefensiverKampfstil2", "nahkampf");
		GROUPS.put("SF_DefensiverKampfstil3", "nahkampf");
		GROUPS.put("SF_OffensiverKampfstil1", "nahkampf");
		GROUPS.put("SF_OffensiverKampfstil2", "nahkampf");
		GROUPS.put("SF_OffensiverKampfstil3", "nahkampf");
		GROUPS.put("SF_Meisterparade", "nahkampf");
		GROUPS.put("SF_Klingenwand", "nahkampf");
		GROUPS.put("SF_Windmuehle", "nahkampf");
		GROUPS.put("SF_Finte", "nahkampf");
		GROUPS.put("SF_GezielterStich", "nahkampf");
		GROUPS.put("SF_Todesstoss", "nahkampf");
		GROUPS.put("SF_Klingensturm", "nahkampf");
		GROUPS.put("SF_Umreissen", "nahkampf");
		GROUPS.put("SF_Wuchtschlag", "nahkampf");
		GROUPS.put("SF_Niederwerfen", "nahkampf");
		GROUPS.put("SF_Befreiungsschlag", "nahkampf");
		GROUPS.put("SF_Hammerschlag", "nahkampf");

		GROUPS.put("SF_GezielterSchuss", "fernkampf");
		GROUPS.put("SF_Scharfschuetze", "fernkampf");
		GROUPS.put("SF_Meisterschuetze", "fernkampf");
		GROUPS.put("SF_Eisenhagel", "fernkampf");
		GROUPS.put("SF_Pfeilhagel", "fernkampf");
		GROUPS.put("SF_Lademeister", "fernkampf");
		GROUPS.put("SF_GezielterWurf", "fernkampf");
		GROUPS.put("SF_Kraftwurf", "fernkampf");
		GROUPS.put("SF_Meisterwurf", "fernkampf");
	}

	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected String getGroupKey(String key) {
		String group = GROUPS.get(key);

		return "specialgroup." + (group != null ? group : "None");
	}

	@Override
	protected String getLocalKey(String key) {
		if (key.startsWith("SF_")) {
			key = key.substring(3);
		}

		if ("Meisterparade".equals(key)) {
			return "MeisterParade";
		}

		return key;
	}

	@Override
	protected String getInfoKey(String key) {
		String name = getLocalKey(key);

		if ("GezielterStich".equals(name)) {
			name = "Gezielter Stich";
		}

		return "Info" + name;
	}

	@Override
	protected JComponent createField(final String key, int value) {
		final JCheckBox box = new JCheckBox();
		box.setSelected(value >= 0);
		box.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				getValues().set(key, box.isSelected() ? 0 : -500);
			}
		});

		return box;
	}
}
