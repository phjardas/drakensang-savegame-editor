/*
 * Savegame.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui.savegame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.jardas.drakensang.DrakensangException;

public class Savegame implements Comparable<Savegame> {
	private File file;
	private String name;
	private String hero;
	private Date changeDate;
	private int level;

	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getHero() {
		return this.hero;
	}

	public void setHero(String hero) {
		this.hero = hero;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int compareTo(Savegame o) {
		if (o == this) {
			return 0;
		}

		if ((o == null) || (o.getChangeDate() == null)) {
			return -1;
		}

		if (getChangeDate() == null) {
			return 1;
		}

		return getChangeDate().compareTo(o.getChangeDate());
	}

	private static void loadInfoFile(Savegame game) {
		File infoFile = new File(game.getFile().getParentFile(), game.getFile()
				.getName().replace(".dsa", ".nfo"));
		byte[] data = loadFile(infoFile);
		int offset = data[9];

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int part = 0;

		for (int i = 16 + offset; i < data.length; i++) {
			int c = data[i];

			if (part == 0 && (char) c == ',') {
				game.setHero(new String(out.toByteArray()));
				out.reset();
				part++;
			} else if (part == 1 && c < 30) {
				String levelString = new String(out.toByteArray());
				game.setLevel(Integer.parseInt(levelString
						.replace("Level:", "").trim()));
				out.reset();
				part++;
			} else if (part == 2 && c != 0) {
				out.reset();
				out.write(c);
				part++;
			} else {
				out.write(c);
			}
		}

		game.setName(new String(out.toByteArray()));
	}

	private static byte[] loadFile(File infoFile) {
		try {
			FileInputStream fis = new FileInputStream(infoFile);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int i;

			while ((i = fis.read()) > -1) {
				out.write(i);
			}

			fis.close();
			return out.toByteArray();
		} catch (IOException e) {
			throw new DrakensangException("Error reading from " + infoFile
					+ ": " + e, e);
		}
	}

	public static Savegame load(File directory) {
		final File[] files = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".dsa");
			}
		});

		if ((files == null) || (files.length != 1)) {
			throw new IllegalArgumentException("No savegame found at "
					+ directory);
		}

		File file = files[0];

		final Savegame game = new Savegame();
		game.setFile(file);
		game.setChangeDate(new Date(file.lastModified()));
		loadInfoFile(game);

		return game;
	}
}
