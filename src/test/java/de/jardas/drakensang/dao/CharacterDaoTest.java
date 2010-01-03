package de.jardas.drakensang.dao;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.savegame.Savegame;

public class CharacterDaoTest {
	private Savegame save;

	@Before
	public void setUp() throws Exception {
		Class.forName("org.sqlite.JDBC");

		final URL resource = getClass().getResource("savegame");
		final File directory = new File(resource.getFile());

		save = Savegame.load(directory);
	}

	@After
	public void tearDown() {
		SavegameDao.close();
	}

	@Test
	public void test() {
		SavegameDao.open(save);
		Character ch = loadCharacter();
		final int muBefore = ch.getAttribute().get("MU");
		System.out.println("MU before: " + muBefore);
		ch.getAttribute().set("MU", muBefore + 1);
		CharacterDao.saveAll();

		SavegameDao.open(save);
		ch = loadCharacter();
		final int muAfter = ch.getAttribute().get("MU");
		System.out.println("MU after: " + muAfter);
		assertEquals("MU after saving", muBefore + 1, muAfter);
	}

	private Character loadCharacter() {
		final Set<Character> characters = CharacterDao.getCharacters();
		for (Character ch : characters) {
			if ("Hero".equals(ch.getName())) {
				return ch;
			}
		}
		throw new RuntimeException("Main character not found!");
	}
}
