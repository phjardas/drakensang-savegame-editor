package de.jardas.drakensang;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Locale;
import java.util.Properties;


public class Settings {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Settings.class);
    private static final File SETTINGS_FILE = new File(System.getProperty(
                "user.home"), ".drakensang-editor/settings.properties");
    private static Settings instance;
    private File drakensangHome;
    private String latestVersionInformation;
    private Locale locale = Locale.getDefault();

    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = load();
        }

        return instance;
    }

    public File getDrakensangHome() {
        return drakensangHome;
    }

    public void setDrakensangHome(File drakensangHome) {
        this.drakensangHome = drakensangHome;
    }

    public String getLatestVersionInformation() {
        return this.latestVersionInformation;
    }

    public void setLatestVersionInformation(String latestVersionInformation) {
        this.latestVersionInformation = latestVersionInformation;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public synchronized void save() {
        Properties props = new Properties();
        props.setProperty("drakensang.home",
            getDrakensangHome().getAbsolutePath());

        if (getLatestVersionInformation() != null) {
            props.setProperty("latestVersionInformation",
                getLatestVersionInformation());
        }

        if (getLocale() != null) {
            props.setProperty("locale", getLocale().toString());
        }

        try {
            SETTINGS_FILE.getParentFile().mkdirs();
            props.store(new FileOutputStream(SETTINGS_FILE), null);
            LOG.debug("Settings saved to " + SETTINGS_FILE + ".");
        } catch (IOException e) {
            LOG.error("Error writing settings to " + SETTINGS_FILE + ": " + e, e);
        }
    }

    private static synchronized Settings load() {
        LOG.debug("Loading settings from " + SETTINGS_FILE);

        Settings settings = new Settings();

        try {
            Properties props = new Properties();
            FileInputStream reader = new FileInputStream(SETTINGS_FILE);
            props.load(reader);
            reader.close();

            if (props.get("drakensang.home") != null) {
                settings.setDrakensangHome(new File(props.getProperty(
                            "drakensang.home")));
            }

            if (props.get("latestVersionInformation") != null) {
                settings.setLatestVersionInformation(props.getProperty(
                        "latestVersionInformation"));
            }

            if (props.get("locale") != null) {
                settings.setLocale(new Locale(props.getProperty("locale")));
            }
        } catch (IOException e) {
            LOG.info("No settings found at " + SETTINGS_FILE + ": " + e);
        }

        LOG.debug("Loaded settings: " + settings);

        return settings;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this).toString();
    }
}
