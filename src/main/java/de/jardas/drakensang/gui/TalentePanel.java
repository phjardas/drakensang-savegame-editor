package de.jardas.drakensang.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jardas.drakensang.model.Talente;

public class TalentePanel extends IntegerMapPanel<Talente> {
	private static Map<String, String> GROUPS = new HashMap<String, String>();

	static {
		GROUPS.put("TaArmbrust", "Fernkampf");
		GROUPS.put("TaBogen", "Fernkampf");
		GROUPS.put("TaWurfwaffen", "Fernkampf");

		GROUPS.put("TaDolche", "Nahkampf");
		GROUPS.put("TaHiebwaffen", "Nahkampf");
		GROUPS.put("TaFechtwaffen", "Nahkampf");
		GROUPS.put("TaSaebel", "Nahkampf");
		GROUPS.put("TaSchwerter", "Nahkampf");
		GROUPS.put("TaSpeere", "Nahkampf");
		GROUPS.put("TaStaebe", "Nahkampf");
		GROUPS.put("TaRaufen", "Nahkampf");
		GROUPS.put("TaZwHiebwaffen", "Nahkampf");
		GROUPS.put("TaZwSchwerter", "Nahkampf");

		GROUPS.put("TaAlchimie", "Handwerk");
		GROUPS.put("TaBogenbau", "Handwerk");
		GROUPS.put("TaFallenEntschaerfen", "Handwerk");
		GROUPS.put("TaSchloesser", "Handwerk");
		GROUPS.put("TaSchmieden", "Handwerk");

		GROUPS.put("TaBetoeren", "Gesellschaft");
		GROUPS.put("TaEtikette", "Gesellschaft");
		GROUPS.put("TaFeilschen", "Gesellschaft");
		GROUPS.put("TaMenschenkenntnis", "Gesellschaft");
		GROUPS.put("TaUeberreden", "Gesellschaft");

		GROUPS.put("TaFallenstellen", "Natur");
		GROUPS.put("TaPflanzenkunde", "Natur");
		GROUPS.put("TaTierkunde", "Natur");
		GROUPS.put("TaWildnisleben", "Natur");

		GROUPS.put("TaGassenwissen", "Wissen");
		GROUPS.put("TaHeilkundeGift", "Wissen");
		GROUPS.put("TaHeilkundeWunden", "Wissen");
		GROUPS.put("TaMagiekunde", "Wissen");

		GROUPS.put("TaSchleichen", "Koerper");
		GROUPS.put("TaSelbstbeherrschung", "Koerper");
		GROUPS.put("TaSinnenschaerfe", "Koerper");
		GROUPS.put("TaTaschendiebstahl", "Koerper");
		GROUPS.put("TaZwergennase", "Koerper");
	}

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
		String group = GROUPS.get(key);

		return "talentgroup." + (group != null ? group : "None");
	}

	@Override
	protected String getLocalKey(String key) {
		if (key.startsWith("Ta")) {
			key = key.substring(2);
		}

		if ("ZwHiebwaffen".equals(key)) {
			return "Zweihandhiebwaffen";
		}

		if ("ZwSchwerter".equals(key)) {
			return "Zweihandschwerter";
		}

		if ("Schloesser".equals(key)) {
			return "SchloesserKnacken";
		}

		if ("Ueberreden".equals(key)) {
			return "UeberredenUeberzeugen";
		}

		return key;
	}

	@Override
	protected JComponent createField(final String key, int value) {
		JComponent taField = super.createField(key, value);
		String adjustKey = "TaATAdjust" + key.substring(2);

		if (getValues().contains(adjustKey)) {
			JPanel wrapper = new JPanel();
			wrapper.setLayout(new GridBagLayout());
			wrapper.add(taField, new GridBagConstraints(0, 0, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));

			int adjustValue = getValues().get(adjustKey);

			if (adjustValue < -100) {
				adjustValue = 0;
			}

			final JSpinner adjust = new JSpinner(new SpinnerNumberModel(
					adjustValue, -100, 100, 1));
			adjust.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					getValues().set(key,
							((Number) adjust.getValue()).intValue());
				}
			});
			wrapper.add(adjust, new GridBagConstraints(1, 0, 1, 1, 0, 0,
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

	@Override
	protected String getInfoKey(String key) {
		return "Info" + getLocalKey(key);
	}
}
