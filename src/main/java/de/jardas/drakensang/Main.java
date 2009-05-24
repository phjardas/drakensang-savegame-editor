package de.jardas.drakensang;

import de.jardas.drakensang.dao.LocaleOption;
import de.jardas.drakensang.dao.LocaleOption.LocaleNotFoundException;
import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.gui.ExceptionDialog;
import de.jardas.drakensang.gui.LocaleChooserDialog;
import de.jardas.drakensang.gui.MainFrame;
import de.jardas.drakensang.gui.util.WordWrap;
import de.jardas.drakensang.util.DrakensangHomeFinder;

import java.io.File;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;


public final class Main {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Main.class);
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(Main.class.getPackage()
                                                                                    .getName() +
            ".messages", Locale.getDefault());
    private static MainFrame frame = null;

    private Main() {
        // utility class
    }

    public static MainFrame getFrame() {
        return Main.frame;
    }

    public static void main(String[] args) {
        LOG.info("Starting up Drakensang Savegame Editor");

        try {
            Class.forName("org.sqlite.JDBC");

            checkSettings();

            final Locale locale = getUserLocale();

            if (locale != null) {
                setUserLocale(locale);
                showMainFrame();
            } else {
                showLanguageChooser();
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private static void showMainFrame() {
        frame = new MainFrame();
        frame.setVisible(true);
        frame.showLoadDialog();
    }

    private static void showLanguageChooser() {
        LOG.debug("Showing language chooser dialog.");
        new LocaleChooserDialog() {
                @Override
                public void onLocaleChosen(Locale locale) {
                	setVisible(false);
                    Main.setUserLocale(locale);
                    showMainFrame();
                }

                @Override
                public void onAbort() {
                    System.exit(1);
                }
            };
    }

    public static void handleException(Exception e) {
        LOG.error("Uncaught exception: " + e, e);

        if (frame != null) {
            frame.setVisible(false);
        }

        new ExceptionDialog(frame, e).setVisible(true);

        LOG.info("Shutting down...");

        System.exit(1);
    }

    public static String getCurrentVersion() {
        ResourceBundle bundle = ResourceBundle.getBundle(Main.class.getPackage()
                                                                   .getName() +
                ".version");

        return bundle.getString("version");
    }

    private static void checkSettings() {
        final Settings settings = Settings.getInstance();
        LOG.debug("Testing connection to " +
            Settings.getInstance().getDrakensangHome());

        if (!Messages.testConnection()) {
            Messages.resetConnection();

            final File home = DrakensangHomeFinder.findDrakensangHome();

            if (home != null) {
                settings.setDrakensangHome(home);
                settings.save();

                return;
            }
        }

        while (!Messages.testConnection()) {
            Messages.resetConnection();
            settings.setDrakensangHome(locateDrakensangHome(settings));
        }
    }

    private static Locale getUserLocale() {
        final Locale locale = Settings.getInstance().getLocale();

        if (locale != null) {
            LOG.info("Found locale in settings: " + locale);

            return locale;
        }

        try {
            return LocaleOption.guessLocale();
        } catch (LocaleNotFoundException e) {
            LOG.warn("Locale not found: " + e, e);

            return null;
        }
    }

    public static void setUserLocale(Locale locale) {
        LOG.debug("Setting locale to '" + locale + "'.");
        Locale.setDefault(locale);
        Settings.getInstance().setLocale(locale);
        Settings.getInstance().save();
        Messages.reload();
    }

    private static File locateDrakensangHome(Settings settings) {
        JOptionPane.showMessageDialog(null,
            WordWrap.addNewlines(BUNDLE.getString("drakensang.home.info")),
            BUNDLE.getString("drakensang.home.title"),
            JOptionPane.WARNING_MESSAGE);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(BUNDLE.getString("drakensang.home.title"));
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("c:/Program Files"));

        fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
        fileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() ||
                    f.getName().equals("drakensang.exe");
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
