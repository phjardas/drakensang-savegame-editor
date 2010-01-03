package de.jardas.drakensang.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Archetype {
	private final String id;
	private final String archetypeClass;
	private final String icon;
	private final Race race;
	private final Culture culture;
	private final Profession profession;
	private final CasterType casterType;
	private final CasterRace casterRace;
	private final Sex sex;
	private final String[] hairs;
	private final String[] faces;
	private final String[] bodies;

	public Archetype(String id, String archetypeClass, String icon, Race race,
			Culture culture, Profession profession, CasterType casterType,
			CasterRace casterRace, Sex sex, String[] hairs, String[] faces,
			String[] bodies) {
		this.id = id;
		this.archetypeClass = archetypeClass;
		this.icon = icon;
		this.race = race;
		this.culture = culture;
		this.profession = profession;
		this.casterType = casterType;
		this.casterRace = casterRace;
		this.sex = sex;
		this.hairs = hairs;
		this.faces = faces;
		this.bodies = bodies;
	}

	public String getId() {
		return id;
	}

	public String getArchetypeClass() {
		return archetypeClass;
	}

	public String getIcon() {
		return icon;
	}

	public Race getRace() {
		return race;
	}

	public Culture getCulture() {
		return culture;
	}

	public Profession getProfession() {
		return profession;
	}

	public CasterType getCasterType() {
		return casterType;
	}

	public CasterRace getCasterRace() {
		return casterRace;
	}

	public Sex getSex() {
		return sex;
	}

	public String[] getHairs() {
		return hairs;
	}

	public String[] getFaces() {
		return faces;
	}

	public String[] getBodies() {
		return bodies;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE).toString();
	}
}
