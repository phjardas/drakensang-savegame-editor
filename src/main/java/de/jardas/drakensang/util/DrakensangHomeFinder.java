package de.jardas.drakensang.util;

import java.io.File;

import java.util.ArrayList;
import java.util.List;


public class DrakensangHomeFinder {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DrakensangHomeFinder.class);
    private static final String[] DRIVES = { "C", "D", "E", "F", };
    private static final String[] PROGRAMS = { "Programme", "Program Files", };

    public static File findDrakensangHome() {
        final List<File> candidates = collectCandidates();

        LOG.debug("Drakensang home candidates: " + candidates);

        for (File candidate : candidates) {
            if ((candidate != null) && candidate.isFile()) {
                final File dir = candidate.getParentFile();
                LOG.debug("Found Drakensang home at " + dir);

                return dir;
            }
        }

        LOG.info("Found no Drakensang home");

        return null;
    }

    private static List<File> collectCandidates() {
        final List<File> candidates = new ArrayList<File>();

        final String home = WindowsRegistry.getDrakensangHome();

        if (home != null) {
            candidates.add(new File(home + "/drakensang.exe"));
        }

        for (String drive : DRIVES) {
            for (String program : PROGRAMS) {
                candidates.add(new File(drive + ":/" + program +
                        "/Drakensang/drakensang.exe"));
            }
        }

        return candidates;
    }
}
