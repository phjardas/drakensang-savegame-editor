package de.jardas.drakensang;

import org.junit.Test;
import static org.junit.Assert.*;

public class VersionInformationTest {
	@Test
	public void testLoad() {
		StringBuffer content = new StringBuffer();
		content.append("1.2").append("\n").append("\n");
		content.append("Added a nifty new feature.").append("\n");
		content.append("Added another great feature.").append("\n");
		
		VersionInformation version = VersionInformation.load(content.toString());
		
		assertEquals("Version", "1.2", version.getVersion());
		assertEquals("Number of changelog entries", 2, version.getChangelog().length);
		assertEquals("Changelog #1", "Added a nifty new feature.", version.getChangelog()[0]);
		assertEquals("Changelog #2", "Added another great feature.", version.getChangelog()[1]);
	}
}
