package de.jardas.drakensang.gui;

import de.jardas.drakensang.model.Sonderfertigkeiten;

public class SonderfertigkeitenPanel extends
		IntegerMapPanel<Sonderfertigkeiten> {
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
}
