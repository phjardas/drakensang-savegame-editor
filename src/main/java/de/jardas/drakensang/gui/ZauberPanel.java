package de.jardas.drakensang.gui;

import java.util.HashMap;
import java.util.Map;

import de.jardas.drakensang.model.Zauberfertigkeiten;

public class ZauberPanel extends IntegerMapPanel<Zauberfertigkeiten> {
	private static final Map<String, String> KEYS = new HashMap<String, String>();

	static {
		KEYS.put("Adlerauge", "AdleraugeLuchsenohr");
		KEYS.put("AeroFugumVakuum", "AeroFugoVakuum");
		KEYS.put("Analys", "AnalysArcanstruktur");
		KEYS.put("Axxeleratus", "AxxeleratusBlitzgeschwind");
		KEYS.put("Balsam", "BalsamSalabunde");
		KEYS.put("Blitz", "BlitzDichFind");
		KEYS.put("Corpofrigo", "CorpofrigoKaelteschock");
		KEYS.put("Duplicatus", "DuplicatusDoppelbild");
		KEYS.put("EscliptifactusSchattenkraft", "EcliptifactusSchattenkraft");
		KEYS.put("Eisenrost", "EisenrostUndPatina");
		KEYS.put("FlimFlam", "FlimFlamFunkel");
		KEYS.put("Foramen", "ForamenForaminor");
		KEYS.put("Fulminictus", "FulminictusDonnerkeil");
		KEYS.put("Gardianum", "GardianumZauberschild");
		KEYS.put("TatzeSchwinge", "HilfreicheTatzeRettendeSchwinge");
		KEYS.put("Horriphobus", "HorriphobusSchreckgestalt");
		KEYS.put("Ignifaxius", "IgnifaxiusFlammenstrahl");
		KEYS.put("Ignisphaero", "IgnisphaeroFeuerball");
		KEYS.put("Kulminatio", "KulminatioKugelblitz");
		KEYS.put("Paralysis", "ParalysisStarrWieStein");
		KEYS.put("Plumbumbarum", "PlumBarumSchwererArm");
		KEYS.put("RuheKoerper", "RuheKoerperRuheGeist");
		KEYS.put("Sensibar", "SensibarEmpathicus");
		KEYS.put("Somnigravis", "SomnigravisTieferSchlaf");
		KEYS.put("Odem", "Odemwolke");
	}

	@Override
	protected String getLocalKey(String key) {
		if (key.startsWith("Za")) {
			key = key.substring(2);
		}

		if (KEYS.containsKey(key)) {
			return KEYS.get(key);
		}

		return super.getLocalKey(key);
	}
	
	@Override
	protected String getInfoKey(String key) {
		return "Info" + getLocalKey(key);
	}
}
