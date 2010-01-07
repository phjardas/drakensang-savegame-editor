package de.jardas.drakensang.model.savegame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.io.PackReader;
import de.jardas.drakensang.shared.io.PackReader.PackData;

public class Savegame implements Comparable<Savegame> {
	private int version;
	private File directory;
	private File file;
	private String name;
	private String hero;
	private Date changeDate;
	private int level;
	private String worldMap;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

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

	public String getWorldMap() {
		return worldMap;
	}

	public void setWorldMap(String worldMap) {
		this.worldMap = worldMap;
	}

	public String getWorldMapKey() {
		return "name_" + getWorldMap();
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static void loadInfoFile(Savegame game) {
		final File infoFile = new File(game.getFile().getParentFile(), game
				.getFile().getName().replace(".dsa", ".nfo"));
		FileInputStream in = null;

		try {
			in = new FileInputStream(infoFile);
			final PackData info = PackReader.read(in);
			game.setVersion(info.getVersion());
			
			final String[] data = info.getData();
			game.setWorldMap(data[0]);

			final String hero = data[1];
			game.setHero(hero.split(",")[0]);
			game.setLevel(Integer.parseInt(hero.split(":")[1].trim()));
			game.setName(data[2]);
		} catch (IOException e) {
			throw new DrakensangException("Error reading save game info from "
					+ infoFile + ": " + e, e);
		} finally {
			IOUtils.closeQuietly(in);
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
		game.setDirectory(directory);
		game.setFile(file);
		game.setChangeDate(new Date(file.lastModified()));
		loadInfoFile(game);

		return game;
	}
}
