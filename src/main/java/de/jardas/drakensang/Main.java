package de.jardas.drakensang;

import de.jardas.drakensang.gui.MainFrame;

import java.io.File;


public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("SQLite.JDBCDriver").newInstance();

            MainFrame frame = new MainFrame();

            frame.setVisible(true);
            frame.loadSavegame(new File(
                    "c:/opt/workspaces/abudhabi/drakensang/savegame.dsa"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
