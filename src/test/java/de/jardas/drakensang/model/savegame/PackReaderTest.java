package de.jardas.drakensang.model.savegame;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.jardas.drakensang.model.savegame.PackReader.PackData;

public class PackReaderTest {
	@Test
	public void testReadVersion2() throws IOException {
		final PackData info = PackReader.read(getClass().getResourceAsStream(
				"savegame_v2.nfo"));
		assertEquals("Pack version", 2, info.getVersion());

		final String[] data = info.getData();
		assertEquals("Pack length", 3, data.length);
		assertEquals("Location", "location04", data[0]);
		assertEquals("Character", "Sleepingsun,Livello: 5", data[1]);
		assertEquals("Name", "Italian save", data[2]);
	}

	@Test
	public void testReadVersion3() throws IOException {
		final PackData info = PackReader.read(getClass().getResourceAsStream(
				"savegame_v3.nfo"));
		assertEquals("Pack version", 3, info.getVersion());

		final String[] data = info.getData();
		assertEquals("Pack length", 3, data.length);
		assertEquals("Location", "locr01_nadoret", data[0]);
		assertEquals("Character", "Peradan von Ghune,Level: 1", data[1]);
		assertEquals("Name", "001", data[2]);
	}
}
