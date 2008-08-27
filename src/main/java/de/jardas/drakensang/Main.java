package de.jardas.drakensang;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.ExceptionDialog;
import de.jardas.drakensang.gui.InfoLabel;
import de.jardas.drakensang.gui.MainFrame;
import de.jardas.drakensang.util.WindowsRegistry;

import java.io.File;

import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;


public final class Main {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(Main.class);
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(Main.class.getPackage()
                                                                                    .getName()
            + ".messages");
    private static MainFrame frame = null;

    private Main() {
        // utility class
    }

    public static MainFrame getFrame() {
        return Main.frame;
    }

    public static void main(String[] args) {
        LOG.info("Starting up!");
        
        try {
            Class.forName("SQLite.JDBCDriver").newInstance();

            checkSettings();

            frame = new MainFrame();

            checkForUpdates();

            frame.setVisible(true);
            frame.loadDefaultSavegame();
        } catch (Exception e) {
            LOG.error("Uncaught exception: " + e, e);

            if (frame != null) {
                frame.setVisible(false);
            }

            new ExceptionDialog(frame, e).setVisible(true);

            LOG.info("Shutting down...");
            
            System.exit(1);
        }
    }

    private static void checkForUpdates() {
        try {
            VersionInformation newestVersion = VersionInformation
                .getNewestVersion();
            Settings settings = Settings.getInstance();

            if (!newestVersion.getVersion().equals(getCurrentVersion())
                    && !newestVersion.getVersion()
                                         .equals(settings
                        .getLatestVersionInformation())) {
                settings.setLatestVersionInformation(newestVersion.getVersion());
                settings.save();

                showNewVersionAvailableDialog(newestVersion);
            }
        } catch (Exception e) {
            LOG.error("Error checking for new version: " + e, e);
        }
    }

    private static String getCurrentVersion() {
        ResourceBundle bundle = ResourceBundle.getBundle(Main.class.getPackage()
                                                                   .getName()
                + ".version");

        return bundle.getString("version");
    }

    private static void showNewVersionAvailableDialog(
        VersionInformation newestVersion) {
        StringBuilder msg = new StringBuilder();
        msg.append("Version ").append(newestVersion.getVersion())
           .append(" is now available!\n\n");

        for (String change : newestVersion.getChangelog()) {
            msg.append("- ").append(change).append("\n");
        }

        msg.append("\nVisit http://www.jardas.de/drakensang/ to download it.");

        JOptionPane.showMessageDialog(frame, msg.toString(),
            "Drakensang Savegame Editor " + newestVersion.getVersion()
            + " available", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void checkSettings() {
        Settings settings = Settings.getInstance();

        LOG.debug("Testing connection to "
            + Settings.getInstance().getDrakensangHome());

        if (!Messages.testConnection()) {
            Messages.resetConnection();

            File[] candidates = {
                    (WindowsRegistry.getDrakensangHome() != null)
                    ? new File(WindowsRegistry.getDrakensangHome()
                        + "/drakensang.exe") : null,
                    new File("C:/Programme/Drakensang/drakensang.exe"),
                    new File("D:/Programme/Drakensang/drakensang.exe"),
                    new File("C:/Program Files/Drakensang/drakensang.exe"),
                    new File("D:/Program Files/Drakensang/drakensang.exe"),
                    new File("C:/Spiele/Drakensang/drakensang.exe"),
                    new File("D:/Spiele/Drakensang/drakensang.exe"),
                    new File("C:/Games/Drakensang/drakensang.exe"),
                    new File("D:/Games/Drakensang/drakensang.exe"),
                };

            LOG.debug("Drakensang home candidates: "
                + Arrays.toString(candidates));

            for (File candidate : candidates) {
                if ((candidate != null) && candidate.isFile()) {
                    LOG.debug("Found Drakensang home at "
                        + candidate.getParentFile());

                    settings.setDrakensangHome(candidate.getParentFile());
                    settings.save();

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

        LOG.info("No Drakensang home selected, shutting down...");
        System.exit(0);

        return null;
    }
}
