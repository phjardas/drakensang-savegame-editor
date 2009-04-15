package de.jardas.drakensang;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import java.net.URL;


public class VersionInformation {
    private String version;
    private String[] changelog;

    private VersionInformation(String version, String[] changelog) {
        super();
        this.version = version;
        this.changelog = changelog;
    }

    public String[] getChangelog() {
        return this.changelog;
    }

    public void setChangelog(String[] changelog) {
        this.changelog = changelog;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static VersionInformation getNewestVersion() {
        String content = loadVersionContent();
        return load(content);
    }

	protected static VersionInformation load(String content) {
		String[] tokens = content.split("\n+");
        String ver = tokens[0];

        String[] changes = new String[tokens.length - 1];

        for (int i = 1; i < tokens.length; i++) {
            changes[i - 1] = tokens[i];
        }

        return new VersionInformation(ver, changes);
	}

    private static String loadVersionContent() {
        try {
            Object connection = new URL(
                    "http://www.jardas.de/drakensang/version.txt").getContent();
            InputStream is = (InputStream) connection;
            InputStreamReader reader = new InputStreamReader(is);
            StringWriter writer = new StringWriter();

            while (true) {
                try {
                    if (!reader.ready()) {
                        break;
                    }

                    writer.append((char) reader.read());
                } catch (IOException e) {
                    break;
                }
            }

            writer.flush();
            reader.close();
            is.close();

            return writer.toString().trim();
        } catch (Exception e) {
            throw new DrakensangException("Error retrieving newest version: "
                + e, e);
        }
    }
}
