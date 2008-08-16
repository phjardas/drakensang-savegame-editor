package de.jardas.drakensang.model;

import java.util.ResourceBundle;

public enum CharacterSet {
	archetype_am_krieger_generated,
	archetype_el_kaempfer_generated,
	archetype_el_waldlaeufer_generated,
	archetype_el_zauberweber_generated,
	archetype_mi_bogenschuetze_generated,
	archetype_mi_einbrecher_generated,
	archetype_mi_heilmagier_generated,
	archetype_mi_kampfmagier_generated,
	archetype_mi_krieger_generated,
	archetype_mi_scharlatan_generated,
	archetype_mi_soldat_generated,
	archetype_mi_streuner_generated,
	archetype_mi_taschendieb_generated,
	archetype_tu_alchemist_generated,
	archetype_tu_antimagier_generated,
	archetype_tu_elementarist_generated,
	archetype_tw_pirat_generated,
	archetype_zw_prospektor_generated,
	archetype_zw_sappeur_generated,
	archetype_zw_soeldner_generated;
	
	private static final ResourceBundle ARCHETYPES = ResourceBundle
		.getBundle(CharacterSet.class.getPackage().getName() + ".archetypes");
	
	public String getArchetype(Sex sex) {
		String[] archetypes = ARCHETYPES.getString(name()).split(",");
		
		if (archetypes.length <= sex.ordinal()) return null;
		String archetype = archetypes[sex.ordinal()];
		
		return archetype != null && archetype.length() > 0 ? archetype.trim() : null;
	}
}
