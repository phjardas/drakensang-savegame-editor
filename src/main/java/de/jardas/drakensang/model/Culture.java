package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Static;

public enum Culture {
	Amazone,
	Ambosszwerg,
	Andergaster,
	Auelf,
	Garetier,
	Horasier,
	Mhanadistani,
	Mittelreicher,
	Gjalskerlaender,
	Novadi,
	Thorwaler,
	Waldelf;
	
	public int getLebensenergieModifikator() {
		return Integer.parseInt(Static.get("LEMax", name(), "id", "_template_culture"));
	}
	
	public int getAusdauerModifikator() {
		return Integer.parseInt(Static.get("AUMax", name(), "id", "_template_culture"));
	}
	
	public int getAstralenergieModifikator() {
		return Integer.parseInt(Static.get("AEMax", name(), "id", "_template_culture"));
	}
	
	public int getMagieresistenzModifikator() {
		return Integer.parseInt(Static.get("MR", name(), "id", "_template_culture"));
	}
}
