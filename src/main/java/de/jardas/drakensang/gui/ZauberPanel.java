package de.jardas.drakensang.gui;

import java.util.HashMap;
import java.util.Map;

import de.jardas.drakensang.model.Zauberfertigkeiten;

public class ZauberPanel extends IntegerMapPanel<Zauberfertigkeiten> {
	private static final Map<String, String> KEYS = new HashMap<String, String>();
	private static Map<String, String> GROUPS = new HashMap<String, String>();

	static {
		KEYS.put("Adlerauge", "AdleraugeLuchsenohr");
		KEYS.put("AeroFugumVakuum", "AeroFugoVakuum");
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

		GROUPS.put("ZaAdlerauge", "attribut");
		GROUPS.put("ZaAeroFugumVakuum", "kampf");
		GROUPS.put("ZaArmatrutz", "attribut");
		GROUPS.put("ZaAttributoMU", "attribut");
		GROUPS.put("ZaAttributoKL", "attribut");
		GROUPS.put("ZaAttributoIN", "attribut");
		GROUPS.put("ZaAttributoCH", "attribut");
		GROUPS.put("ZaAttributoFF", "attribut");
		GROUPS.put("ZaAttributoGE", "attribut");
		GROUPS.put("ZaAttributoKO", "attribut");
		GROUPS.put("ZaAttributoKK", "attribut");
		GROUPS.put("ZaAxxeleratus", "attribut");
		GROUPS.put("ZaBalsam", "heilung");
		GROUPS.put("ZaBlitz", "kampf");
		GROUPS.put("ZaCorpofrigo", "kampf");
		GROUPS.put("ZaDschinenruf", "beschwoerung");
		GROUPS.put("ZaDuplicatus", "beschwoerung");
		GROUPS.put("ZaEscliptifactusSchattenkraft", "beschwoerung");
		GROUPS.put("ZaEigenschaftWiederherstellen", "attribut");
		GROUPS.put("ZaEisenrost", "kampf");
		GROUPS.put("ZaEiseskaelteKaempferherz", "attribut");
		GROUPS.put("ZaElementarerDiener", "beschwoerung");
		GROUPS.put("ZaFalkenaugeMeisterschuss", "attribut");
		GROUPS.put("ZaFavilludoFunkentanz", "beschwoerung");
		GROUPS.put("ZaFlimFlam", "spezial");
		GROUPS.put("ZaForamen", "attribut");
		GROUPS.put("ZaFulminictus", "kampf");
		GROUPS.put("ZaGardianum", "schutz");
		GROUPS.put("ZaHerrUeberDasTierreich", "beherrschung");
		GROUPS.put("ZaTatzeSchwinge", "beschwoerung");
		GROUPS.put("ZaHorriphobus", "beherrschung");
		GROUPS.put("ZaIgnifaxius", "kampf");
		GROUPS.put("ZaIgnisphaero", "kampf");
		GROUPS.put("ZaKlarumPurum", "heilung");
		GROUPS.put("ZaKulminatio", "kampf");
		GROUPS.put("ZaParalysis", "kampf");
		GROUPS.put("ZaPlumbumbarum", "kampf");
		GROUPS.put("ZaPsychostabilis", "attribut");
		GROUPS.put("ZaRuheKoerper", "heilung");
		GROUPS.put("ZaSanftmut", "beherrschung");
		GROUPS.put("ZaSeidenzungeElfenwort", "attribut");
		GROUPS.put("ZaSensibar", "attribut");
		GROUPS.put("ZaSkelettarius", "beschwoerung");
		GROUPS.put("ZaSomnigravis", "beherrschung");
		GROUPS.put("ZaOdem", "attribut");
	}

	@Override
	protected boolean isGrouped() {
		return true;
	}

	@Override
	protected String getGroupKey(String key) {
		String group = GROUPS.get(key);

		return "spellgroup." + (group != null ? group : "None");
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
