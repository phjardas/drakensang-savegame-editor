/*
 * Level.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import de.jardas.drakensang.dao.Messages;


public class Level {
    private String id;
    private String name;
    private String worldMapLocation;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorldMapLocation() {
		return this.worldMapLocation;
	}

	public void setWorldMapLocation(String worldMapLocation) {
		this.worldMapLocation = worldMapLocation;
	}
	
	public String getLocalizedName() {
		return Messages.get(getName());
	}

	@Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
