package de.jardas.drakensang;

public class WebStart {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(WebStart.class);

    public static void main(String[] args) {
        loadNativeLibraries();
        Main.main(args);
    }

    private static void loadNativeLibraries() {
        final String os = System.getProperty("os.name");
        LOG.debug("Loading " + os + " native libraries ..");

        if (os.startsWith("Windows")) {
            System.loadLibrary("sqlite_jni");
        } else {
            throw new RuntimeException("OS '" + os + "' not yet supported.");
        }
    }
}
