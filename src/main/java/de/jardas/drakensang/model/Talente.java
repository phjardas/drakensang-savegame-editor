package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Static;


public class Talente extends IntegerMap {
    @Override
    public String[] getKeys() {
        return new String[] {
            "TaArmbrust", "TaDolche", "TaATAdjustDolche", "TaFechtwaffen",
            "TaATAdjustFechtwaffen", "TaHiebwaffen", "TaATAdjustHiebwaffen",
            "TaSaebel", "TaATAdjustSaebel", "TaSchwerter", "TaATAdjustSchwerter",
            "TaSpeere", "TaATAdjustSpeere", "TaStaebe", "TaATAdjustStaebe",
            "TaZwHiebwaffen", "TaATAdjustZwHiebwaffen", "TaZwSchwerter",
            "TaATAdjustZwSchwerter", "TaRaufen", "TaATAdjustRaufen", "TaBogen",
            "TaWurfwaffen", "TaSchleichen", "TaSelbstbeherrschung",
            "TaSinnenschaerfe", "TaTaschendiebstahl", "TaZwergennase",
            "TaFallenstellen", "TaPflanzenkunde", "TaTierkunde",
            "TaWildnisleben", "TaMagiekunde", "TaHeilkundeWunden",
            "TaHeilkundeGift", "TaGassenwissen", "TaAlchimie", "TaBogenbau",
            "TaSchmieden", "TaFallenEntschaerfen", "TaSchloesser", "TaFeilschen",
            "TaEtikette", "TaBetoeren", "TaUeberreden", "TaMenschenkenntnis",
        };
    }

    public static String[] getAttributes(String key) {
        final String t1 = Static.get("TaP1", key, "TaAttr", "_template_talent");
        final String t2 = Static.get("TaP2", key, "TaAttr", "_template_talent");
        final String t3 = Static.get("TaP3", key, "TaAttr", "_template_talent");

        return new String[] { t1, t2, t3 };
    }
    
    public static String getNameKey(String key) {
    	return Static.get("Name", key, "TaAttr", "_Template_Talent");
    }
}
