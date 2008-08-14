package de.jardas.drakensang;

import de.jardas.drakensang.dao.CharacterDao;
import de.jardas.drakensang.gui.MainFrame;
import de.jardas.drakensang.model.Character;

import java.io.File;

import java.util.Set;


public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("SQLite.JDBCDriver").newInstance();

            MainFrame frame = new MainFrame();

            frame.setVisible(true);
            frame.loadSavegame(new File("c:/opt/workspaces/abudhabi/drakensang/savegame.dsa"));

//             CharacterDao dao = new CharacterDao("c:/opt/workspaces/abudhabi/drakensang/savegame.dsa");
//             Set<Character> characters = dao.getCharacters();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
