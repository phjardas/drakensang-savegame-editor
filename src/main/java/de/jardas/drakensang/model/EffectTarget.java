package de.jardas.drakensang.model;

import org.apache.commons.lang.ArrayUtils;

public enum EffectTarget {
	Attribute,
    Talent,
    Spell,
    MagicResistance,
    Life,
    LifeRegeneration,
    LifeRegenerationFrequency,
    LifeRegenerationFrequencyCombat,
    Mana,
    ManaRegeneration,
    ManaRegenerationFrequency,
    ManaRegenerationFrequencyCombat,
    Endurance,
    EnduranceRegeneration,
    EnduranceRegenerationFrequency,
    EnduranceRegenerationFrequencyCombat,
	Dodge;
	
    public static EffectTarget getTargetType(String name) {
        if (ArrayUtils.contains(de.jardas.drakensang.model.Attribute.KEYS, name)) {
            return Attribute;
        }

        if ("MR".equals(name)) {
            return MagicResistance;
        }

        if ("LEmax".equals(name)) {
            return Life;
        }

        if ("Reg_LE".equals(name)) {
            return LifeRegeneration;
        }
        
        if ("Reg_LE_freq".equals(name)) {
        	return LifeRegenerationFrequency;
        }
        
        if ("Reg_LE_freq_combat".equals(name)) {
        	return LifeRegenerationFrequencyCombat;
        }

        if ("AEmax".equals(name)) {
            return Mana;
        }

        if ("Reg_AE".equals(name)) {
            return ManaRegeneration;
        }
        
        if ("Reg_AE_freq".equals(name)) {
        	return ManaRegenerationFrequency;
        }
        
        if ("Reg_AE_freq_combat".equals(name)) {
        	return ManaRegenerationFrequencyCombat;
        }

        if ("AUmax".equals(name)) {
            return Endurance;
        }

        if ("Reg_AU".equals(name)) {
            return EnduranceRegeneration;
        }

        if ("Reg_AU_freq".equals(name)) {
        	return EnduranceRegenerationFrequency;
        }
        
        if ("Reg_AU_freq_combat".equals(name)) {
        	return EnduranceRegenerationFrequencyCombat;
        }

        if ("AW".equals(name)) {
        	return Dodge;
        }

        if (name.startsWith("Ta")) {
            return Talent;
        }
        
        if (name.startsWith("Za")) {
        	return Spell;
        }

        throw new IllegalArgumentException("Unknown effect target '" + name + "'");
    }
}
