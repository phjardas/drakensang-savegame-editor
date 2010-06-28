package de.jardas.drakensang;

import org.apache.commons.lang.builder.ToStringBuilder;


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

	protected static VersionInformation load(String content) {
		String[] tokens = content.split("\n+");
        String ver = tokens[0];

        String[] changes = new String[tokens.length - 1];

        for (int i = 1; i < tokens.length; i++) {
            changes[i - 1] = tokens[i];
        }

        return new VersionInformation(ver, changes);
	}
}
