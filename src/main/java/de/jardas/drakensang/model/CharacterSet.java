package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Static;


public enum CharacterSet {
    archetype_mi_krieger("archetype_MI_KR_M", "archetype_MI_KR_W"), 
    archetype_mi_bogenschuetze("archetype_MI_BS_M", "archetype_MI_BS_W"), 
    archetype_mi_soldat("archetype_MI_SD_M", "archetype_MI_SD_W"), 
    archetype_mi_kampfmagier("archetype_MI_KM_M", "archetype_MI_KM_W"), 
    archetype_mi_heilmagier("archetype_MI_HM_M", "archetype_MI_HM_W"), 
    archetype_mi_scharlatan("archetype_MI_SL_M", "archetype_MI_SL_W"), 
    archetype_mi_streuner("archetype_MI_ST_M", "archetype_MI_ST_W"), 
    archetype_mi_einbrecher("archetype_MI_EI_M", "archetype_MI_EI_W"), 
    archetype_mi_taschendieb("archetype_MI_TA_M", "archetype_MI_TA_W"), 
    archetype_tu_elementarist("archetype_TU_EL_M", "archetype_TU_EL_W"), 
    archetype_tu_antimagier("archetype_TU_MM_M", "archetype_TU_MM_W"), 
    archetype_tu_alchemist("archetype_TU_AL_M", "archetype_TU_AL_W"), 
    archetype_tw_pirat("archetype_TW_PI_M", "archetype_TW_PI_W"), 
    archetype_am_krieger(null, "archetype_AM_KU_W"), 
    archetype_el_waldlaeufer("archetype_EL_WL_M", "archetype_EL_WL_W"), 
    archetype_el_zauberweber("archetype_EL_ZA_M", "archetype_EL_ZA_W"), 
    archetype_el_kaempfer("archetype_EL_KA_M", "archetype_EL_KA_W"), 
    archetype_zw_soeldner("archetype_ZW_SO_M", null), 
    archetype_zw_sappeur("archetype_ZW_SA_M", null), 
    archetype_zw_prospektor("archetype_ZW_PR_M", null);
    
    private final String archetypeMale;
    private final String archetypeFemale;

    private CharacterSet(final String archetypeMale,
        final String archetypeFemale) {
        this.archetypeMale = archetypeMale;
        this.archetypeFemale = archetypeFemale;
    }

    public String getArchetype(Sex sex) {
        return (sex == Sex.male) ? archetypeMale : archetypeFemale;
    }

    public String getIcon(Sex sex) {
        return Static.get("IconBrush", getArchetype(sex), "Id",
            "_Template_PC_CharWizard");
    }
}
