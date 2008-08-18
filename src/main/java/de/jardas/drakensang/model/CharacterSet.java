package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Static;


public enum CharacterSet {
    archetype_mi_krieger_generated("archetype_MI_KR_M", "archetype_MI_KR_W"), 
    archetype_mi_bogenschuetze_generated("archetype_MI_BS_M", "archetype_MI_BS_W"), 
    archetype_mi_soldat_generated("archetype_MI_SD_M", "archetype_MI_SD_W"), 
    archetype_mi_kampfmagier_generated("archetype_MI_KM_M", "archetype_MI_KM_W"), 
    archetype_mi_heilmagier_generated("archetype_MI_HM_M", "archetype_MI_HM_W"), 
    archetype_mi_scharlatan_generated("archetype_MI_SL_M", "archetype_MI_SL_W"), 
    archetype_mi_streuner_generated("archetype_MI_ST_M", "archetype_MI_ST_W"), 
    archetype_mi_einbrecher_generated("archetype_MI_EI_M", "archetype_MI_EI_W"), 
    archetype_mi_taschendieb_generated("archetype_MI_TA_M", "archetype_MI_TA_W"), 
    archetype_tu_elementarist_generated("archetype_TU_EL_M", "archetype_TU_EL_W"), 
    archetype_tu_antimagier_generated("archetype_TU_MM_M", "archetype_TU_MM_W"), 
    archetype_tu_alchemist_generated("archetype_TU_AL_M", "archetype_TU_AL_W"), 
    archetype_tw_pirat_generated("archetype_TW_PI_M", "archetype_TW_PI_W"), 
    archetype_am_krieger_generated(null, "archetype_AM_KU_W"), 
    archetype_el_waldlaeufer_generated("archetype_EL_WL_M", "archetype_EL_WL_W"), 
    archetype_el_zauberweber_generated("archetype_EL_ZA_M", "archetype_EL_ZA_W"), 
    archetype_el_kaempfer_generated("archetype_EL_KA_M", "archetype_EL_KA_W"), 
    archetype_zw_soeldner_generated("archetype_ZW_SO_M", null), 
    archetype_zw_sappeur_generated("archetype_ZW_SA_M", null), 
    archetype_zw_prospektor_generated("archetype_ZW_PR_M", null);
    
    private final String archetypeMale;
    private final String archetypeFemale;

    private CharacterSet(final String archetypeMale,
        final String archetypeFemale) {
        this.archetypeMale = archetypeMale;
        this.archetypeFemale = archetypeFemale;
    }

    public String getArchetype(Sex sex) {
        return (sex == Sex.maennlich) ? archetypeMale : archetypeFemale;
    }

    public String getIcon(Sex sex) {
        return Static.get("IconBrush", getArchetype(sex), "Id",
            "_Template_PC_CharWizard");
    }
}
