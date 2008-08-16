package de.jardas.drakensang.gui;

import de.jardas.drakensang.model.Talente;

public class TalentePanel extends IntegerMapPanel<Talente> {
	@Override
	protected String getLocalKey(String key) {
		if (key.startsWith("Ta") && !key.contains("Adjust")) {
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
}
