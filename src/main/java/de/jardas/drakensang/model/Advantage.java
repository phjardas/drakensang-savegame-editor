package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Static;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public enum Advantage {
	begabung_dolche, begabung_fechtwaffen, 
    begabung_hiebwaffen, begabung_saebel, begabung_schwerter, begabung_speere, 
    begabung_staebe, begabung_zwhiebwaffen, begabung_zwschwerter, 
    begabung_raufen, begabung_nahkampf, begabung_armbrust, begabung_bogen, 
    begabung_wurfwaffen, begabung_fernkampf, begabung_schleichen, 
    begabung_selbstbeherrschung, begabung_sinnenschaerfe, 
    begabung_taschendiebstahl, begabung_zwergennase, begabung_koerper, 
    begabung_fallenstellen, begabung_wildnisleben, begabung_pflanzenkunde, 
    begabung_tierkunde, begabung_natur, begabung_magiekunde, 
    begabung_heilkundewunden, begabung_heilkundegift, begabung_gassenwissen, 
    begabung_wissen, begabung_alchimie, begabung_bogenbau, begabung_schmieden, 
    begabung_schloesser, begabung_fallenentschaerfen, begabung_handwerk, 
    begabung_betoeren, begabung_ueberreden, begabung_feilschen, 
    begabung_menschenkenntnis, begabung_etikette, unfaehigkeit_dolche, 
    unfaehigkeit_fechtwaffen, unfaehigkeit_hiebwaffen, unfaehigkeit_saebel, 
    unfaehigkeit_schwerter, unfaehigkeit_speere, unfaehigkeit_staebe, 
    unfaehigkeit_zwhiebwaffen, unfaehigkeit_zwschwerter, unfaehigkeit_raufen, 
    unfaehigkeit_nahkampf, unfaehigkeit_armbrust, unfaehigkeit_bogen, 
    unfaehigkeit_wurfwaffen, unfaehigkeit_fernkampf, unfaehigkeit_schleichen, 
    unfaehigkeit_selbstbeherrschung, unfaehigkeit_sinnenschaerfe, 
    unfaehigkeit_taschendiebstahl, unfaehigkeit_zwergennase, 
    unfaehigkeit_koerper, unfaehigkeit_fallenstellen, unfaehigkeit_wildnisleben, 
    unfaehigkeit_pflanzenkunde, unfaehigkeit_tierkunde, unfaehigkeit_natur, 
    unfaehigkeit_magiekunde, unfaehigkeit_heilkundewunden, 
    unfaehigkeit_heilkundegift, unfaehigkeit_gassenwissen, unfaehigkeit_wissen, 
    unfaehigkeit_alchimie, unfaehigkeit_bogenbau, unfaehigkeit_schmieden, 
    unfaehigkeit_schloesser, unfaehigkeit_fallenentschaerfen, 
    unfaehigkeit_handwerk, unfaehigkeit_betoeren, unfaehigkeit_ueberreden, 
    unfaehigkeit_feilschen, unfaehigkeit_menschenkenntnis, 
    unfaehigkeit_etikette, unfaehigkeit_gesellschaft, gutes_aussehen, 
    herausragende_sinne, eingeschraenkte_sinne, le_bonus, ae_bonus, au_bonus, 
    le_malus, ae_malus, au_malus, magieresistenz, niedrige_magieresistenz, 
    schnelle_heilung, astrale_regeneration, ausdauernd, langsame_heilung, 
    langsame_astrale_regeneration, kurzatmig, eisern, elfische_weltsicht, 
    gefahreninstinkt, soziale_anpassungsfaehigkeit, entfernungssinn, weltfremd;

    private final Effect[] effects;

    private Advantage() {
        effects = loadEffects(name());
    }

    private static Effect[] loadEffects(String name) {
        final String[] tokens = Static.get("AttributeModifier", name, "Id",
                "_Template_Advantages").trim().split("\\s*;\\s*");
        List<Effect> effects = new ArrayList<Effect>(tokens.length);

        for (String token : tokens) {
            if (token.trim().length() == 0) {
                continue;
            }

            final String[] nameAndMod = token.split(":");

            String targetName = nameAndMod[0];
            EffectTarget targetType = EffectTarget.getTargetType(targetName);
            int modifier = Integer.valueOf(nameAndMod[1].startsWith("+")
                    ? nameAndMod[1].substring(1) : nameAndMod[1]);
            effects.add(new Effect(targetType, targetName, modifier));
        }

        return effects.toArray(new Effect[effects.size()]);
    }

    public String getNameKey() {
        return Static.get("Name", name(), "Id", "_Template_Advantages");
    }

    public String getInfoKey() {
        return Static.get("Description", name(), "Id", "_Template_Advantages");
    }

    public Effect[] getEffects() {
        return this.effects;
    }

    public static String serialize(Collection<Advantage> advantages) {
        StringBuffer out = new StringBuffer();

        for (Advantage advantage : advantages) {
            if (out.length() > 0) {
                out.append(";");
            }

            out.append(advantage.name());
        }

        return out.toString();
    }

    public static class Effect {
        private final EffectTarget targetType;
        private final String targetName;
        private final int modifier;

        public Effect(final EffectTarget targetType, final String targetName,
            final int modifier) {
            super();
            this.targetType = targetType;
            this.targetName = targetName;
            this.modifier = modifier;
        }

        public int getModifier() {
            return this.modifier;
        }

        public String getTargetName() {
            return this.targetName;
        }

        public EffectTarget getTargetType() {
            return this.targetType;
        }

        public String getTargetNameKey() {
            switch (getTargetType()) {
            case Talent:
                return Talente.getNameKey(getTargetName());
                
            case MagicResistance:
            	return "Magieresistenz";
            	
            case Life:
            	return "LE";
            	
            case Mana:
            	return "AE";
            	
            case Endurance:
            	return "AU";

            default:
                return getTargetName();
            }
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
    public static enum EffectTarget {
    	Attribute, Talent, MagicResistance, Life, 
        LifeRegeneration, Mana, ManaRegeneration, Endurance, 
        EnduranceRegeneration;
    	
    	public static EffectTarget getTargetType(
            String name) {
            if (ArrayUtils.contains(de.jardas.drakensang.model.Attribute.KEYS,
                        name)) {
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

            if ("AEmax".equals(name)) {
                return Mana;
            }

            if ("Reg_AE".equals(name)) {
                return ManaRegeneration;
            }

            if ("AUmax".equals(name)) {
                return Endurance;
            }

            if ("Reg_AU".equals(name)) {
                return EnduranceRegeneration;
            }

            if (name.startsWith("Ta")) {
                return Talent;
            }

            return null;
        }
    }
}
