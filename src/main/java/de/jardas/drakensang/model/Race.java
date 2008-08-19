/*
 * Race.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Static;

public enum Race {
	Elf,
	Mittellaender,
	Thorwaler,
	Tulamide,
	Zwerg;
	
	public int getLebensenergieModifikator() {
		return Integer.parseInt(Static.get("LEMax", name(), "id", "_template_race"));
	}
	
	public int getAusdauerModifikator() {
		return Integer.parseInt(Static.get("AUMax", name(), "id", "_template_race"));
	}
	
	public int getAstralenergieModifikator() {
		return Integer.parseInt(Static.get("AEMax", name(), "id", "_template_race"));
	}
	
	public int getMagieresistenzModifikator() {
		return Integer.parseInt(Static.get("MR", name(), "id", "_template_race"));
	}
}
