/*
 * SavegameService.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.savegame;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jardas.drakensang.util.WindowsRegistry;

public class SavegameService {
	public static Savegame getLatestSavegame() {
		List<Savegame> savegames = getSavegames();
		Collections.sort(savegames, Collections.reverseOrder());

		return savegames.isEmpty() ? null : savegames.get(0);
	}

	public static List<Savegame> getSavegames() {
		File savedir = getSavesDirectory();
		File[] files = savedir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		List<Savegame> savegames = new ArrayList<Savegame>(files.length);

		for (File file : files) {
			try {
				savegames.add(Savegame.load(file));
			} catch (IllegalArgumentException e) {
				// ignore
			}
		}
		return savegames;
	}

	public static File getSavesDirectory() {
		File documentsDir = new File(WindowsRegistry
				.getCurrentUserPersonalFolderPath());
		File savedir = new File(documentsDir,
				"Drakensang/profiles/default/save/");
		return savedir;
	}
}
