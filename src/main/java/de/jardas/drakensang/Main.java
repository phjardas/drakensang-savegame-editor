package de.jardas.drakensang;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.InfoLabel;
import de.jardas.drakensang.gui.MainFrame;

import java.io.File;

import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;


public final class Main {
    private static final ResourceBundle BUNDLE = ResourceBundle
        .getBundle(Main.class.getPackage().getName() + ".messages");

    private Main() {
        // utility class
    }

    public static void main(String[] args) {
        try {
            Class.forName("SQLite.JDBCDriver").newInstance();

            checkSettings();

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            frame.loadDefaultSavegame();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, BUNDLE.getString("error") + e,
                BUNDLE.getString("error.title"), JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static void checkSettings() {
        Settings settings = Settings.getInstance();

        if (!Messages.testConnection()) {
            Messages.resetConnection();

            File[] candidates = {
            		new File("C:/Programme/Drakensang/drakensang.exe"),
                    new File("C:/Program Files/Drakensang/drakensang.exe"),
                };

            for (File candidate : candidates) {
                if (candidate.isFile()) {
                    settings.setDrakensangHome(candidate.getParentFile());

                    return;
                }
            }
        }

        while (!Messages.testConnection()) {
            Messages.resetConnection();
            settings.setDrakensangHome(getDrakensangHome(settings));
        }

        settings.save();
    }

    private static File getDrakensangHome(Settings settings) {
        JOptionPane.showMessageDialog(null,
            InfoLabel.addNewLines(BUNDLE.getString("drakensang.home.info")),
            BUNDLE.getString("drakensang.home.title"),
            JOptionPane.WARNING_MESSAGE);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(BUNDLE.getString("drakensang.home.title"));
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("c:/Program Files"));

        fileChooser.removeChoosableFileFilter(fileChooser
            .getChoosableFileFilters()[0]);
        fileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory()
                    || f.getName().equals("drakensang.exe");
                }

                public String getDescription() {
                    return "Drakensang (drakensang.exe)";
                }
            });

        int result = fileChooser.showDialog(null,
                BUNDLE.getString("drakensang.home.button"));

        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getParentFile();
        }

        System.exit(0);

        return null;
    }
}
