package de.jardas.drakensang.gui;

import de.jardas.drakensang.shared.model.IntegerMap;
import de.jardas.drakensang.shared.model.Sonderfertigkeit;
import de.jardas.drakensang.shared.model.Sonderfertigkeiten;

class SonderfertigkeitenIntegerMap extends IntegerMap {
	private static final String[] KEYS;
	private final Sonderfertigkeiten fertigkeiten;

	static {
		KEYS = new String[Sonderfertigkeit.values().length];

		int i = 0;
		for (Sonderfertigkeit sf : Sonderfertigkeit.values()) {
			KEYS[i++] = sf.name();
		}
	}

	public SonderfertigkeitenIntegerMap(Sonderfertigkeiten fertigkeiten) {
		this.fertigkeiten = fertigkeiten;

		for (Sonderfertigkeit sf : Sonderfertigkeit.values()) {
			set(sf.name(), fertigkeiten.contains(sf) ? 1 : -500);
		}
	}

	@Override
	public void set(String name, int value) {
		super.set(name, value);

		if (value >= 0) {
			fertigkeiten.add(Sonderfertigkeit.valueOf(name));
		} else {
			fertigkeiten.remove(Sonderfertigkeit.valueOf(name));
		}
	}

	@Override
	public String[] getKeys() {
		return KEYS;
	}
}