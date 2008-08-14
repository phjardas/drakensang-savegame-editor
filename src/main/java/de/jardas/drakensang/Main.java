package de.jardas.drakensang;

import java.io.File;
import java.util.Set;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.gui.MainFrame;
import de.jardas.drakensang.model.Character;

public class Main {
	public static void main(String[] args) {
		try {
			Class.forName("SQLite.JDBCDriver").newInstance();
			
			MainFrame frame = new MainFrame();
			frame.setVisible(true);
			frame.loadSavegame(new File("c:/java/drakensang/savegame.dsa"));

			// CharacterDao dao = new
			// CharacterDao("c:/java/drakensang/savegame.dsa");
			// Set<Character> characters = dao.getCharacters();
			// Character first = characters.iterator().next();
			// dao.save(first);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
