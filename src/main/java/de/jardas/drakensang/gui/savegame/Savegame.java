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

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.io.FilenameFilter;

import java.util.Date;


public class Savegame implements Comparable<Savegame> {
    private File file;
    private String name;
    private String hero;
    private Date changeDate;

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

        // File infoFile = new File(directory, file.getName().replace(".dsa", ".nfo"));
        final Savegame game = new Savegame();
        game.setFile(file);
        game.setName(file.getParentFile().getName()
                         .replaceAll("^savegame_(.*)", "$1"));
        game.setChangeDate(new Date(file.lastModified()));

        return game;
    }

    public static void main(String[] args) {
        File dir = new File(System.getProperty("user.home"),
                "Eigene Dateien/Drakensang/profiles/default/save/savegame_74");
        Savegame game = Savegame.load(dir);
        System.out.println(game);
    }
}
