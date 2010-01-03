package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Static;

public enum Profession {
	Alchimist,
	Amazone,
	Barbar,
	Bogenschuetze,
	Dieb,
	Einbrecher,
	Elementarist,
	Geode,
	Heilmagier,
	Kaempfer,
	Kampfmagier,
	Krieger,
	Metamagier,
	Pirat,
	Prospektor,
	Sappeur,
	Scharlatan,
	Soeldner,
	Soldat,
	Streuner,
	Waldlaeufer,
	Zauberweber;
	
	public int getLebensenergieModifikator() {
		return Integer.parseInt(Static.get("LEMax", name(), "id", "_template_profession"));
	}
	
	public int getAusdauerModifikator() {
		return Integer.parseInt(Static.get("AUMax", name(), "id", "_template_profession"));
	}
	
	public int getAstralenergieModifikator() {
		return Integer.parseInt(Static.get("AEMax", name(), "id", "_template_profession"));
	}
	
	public int getMagieresistenzModifikator() {
		return Integer.parseInt(Static.get("MR", name(), "id", "_template_profession"));
	}
}
