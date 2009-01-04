package de.jardas.drakensang.gui;

public final class Browser {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Browser.class);
    private static final String OS = System.getProperty("os.name").toLowerCase();

    static {
        LOG.debug("Operating system: '" + OS + "'.");
    }

    public static void open(String url) {
        try {
            final Runtime rt = Runtime.getRuntime();

            if (OS.indexOf("win") >= 0) {
                // this doesn't support showing urls in the form of "page.html#nameLink" 
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (OS.indexOf("mac") >= 0) {
                rt.exec("open " + url);
            } else if ((OS.indexOf("nix") >= 0) || (OS.indexOf("nux") >= 0)) {
                // Do a best guess on unix until we get a platform independent way
                // Build a list of browsers to try, in this order.
                String[] browsers = {
                        "epiphany", "firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"
                    };

                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                final StringBuffer cmd = new StringBuffer();

                for (int i = 0; i < browsers.length; i++) {
                    cmd.append(((i == 0) ? "" : " || ") + browsers[i] + " \"" +
                        url + "\" ");
                }

                rt.exec(new String[] { "sh", "-c", cmd.toString() });
            } else {
                LOG.warn("Error opening url '" + url +
                    "': Unsupported operating system '" + OS + "'.");
            }
        } catch (Exception e) {
            LOG.error("Error opening url '" + url + "': " + e, e);
        }
    }
}
