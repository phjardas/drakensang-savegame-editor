/*
 * SavegameSelectionListener.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.load;

import de.jardas.drakensang.model.savegame.Savegame;

public interface SavegameListener {
	void loadSavegame(Savegame savegame);
}
