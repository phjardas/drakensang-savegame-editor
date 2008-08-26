/*
 * WindowsRegistry.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;


public final class WindowsRegistry {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(WindowsRegistry.class);
    private static final String REGQUERY_UTIL = "reg query ";
    private static final String REGSTR_TOKEN = "REG_SZ";
    private static final String PERSONAL_FOLDER = "\"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
        + "Explorer\\Shell Folders\" /v Personal";
    private static final String DRAKENSANG_HOME = "\"Computer\\HKEY_CURRENT_USER\\Software\\DTP\\Drakensang\""
        + " /v target_folder";

    private WindowsRegistry() {
        // utility class
    }

    public static String getCurrentUserPersonalFolderPath() {
        return getRegistryValue(PERSONAL_FOLDER);
    }

    public static String getDrakensangHome() {
        return getRegistryValue(DRAKENSANG_HOME);
    }

    public static String getRegistryValue(final String query) {
        try {
            Process process = Runtime.getRuntime().exec(REGQUERY_UTIL + query);
            StreamReader reader = new StreamReader(process.getInputStream());

            reader.start();
            process.waitFor();
            reader.join();

            String result = reader.getResult();
            int p = result.indexOf(REGSTR_TOKEN);

            if (p == -1) {
                LOG.debug("No value found in registry for '" + query + "'.");

                return null;
            }

            final String value = result.substring(p + REGSTR_TOKEN.length())
                                       .trim();
            LOG.debug("Registry value '" + query + "' resolved to '" + value
                + "'.");

            return value;
        } catch (Exception e) {
            LOG.warn("Error reading from registry '" + query + "': " + e, e);

            return null;
        }
    }

    private static class StreamReader extends Thread {
        private final InputStream is;
        private final StringWriter sw = new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;

                while ((c = is.read()) != -1) {
                    sw.write(c);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading from registry: " + e,
                    e);
            }
        }

        public String getResult() {
            return sw.toString();
        }
    }
}
