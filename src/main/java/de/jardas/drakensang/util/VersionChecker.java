package de.jardas.drakensang.util;

import de.jardas.drakensang.Main;
import de.jardas.drakensang.Settings;
import de.jardas.drakensang.VersionInformation;


public class VersionChecker implements Runnable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(VersionChecker.class);

    public void run() {
        try {
            VersionInformation newestVersion = VersionInformation
                .getNewestVersion();
            Settings settings = Settings.getInstance();

            if (!newestVersion.getVersion().equals(Main.getCurrentVersion())
                    && !newestVersion.getVersion()
                                         .equals(settings
                        .getLatestVersionInformation())) {
                settings.setLatestVersionInformation(newestVersion.getVersion());
                settings.save();

                onNewVersionDetected(newestVersion);
            } else {
                LOG.info("No new version available.");
            }
        } catch (Exception e) {
            LOG.error("Error checking for new version: " + e, e);
        }
    }

    protected void onNewVersionDetected(VersionInformation newestVersion) {
    }
}
