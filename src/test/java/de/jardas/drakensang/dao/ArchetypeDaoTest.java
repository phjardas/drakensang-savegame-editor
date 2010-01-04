package de.jardas.drakensang.dao;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

public class ArchetypeDaoTest {
	@BeforeClass
	public static void setUpClass() throws Exception {
        Class.forName("org.sqlite.JDBC");
	}

	@Test
	public void testLoadArchetypes() {
		System.out.println("Hairs: "+ Arrays.toString(ArchetypeDao.getHairs()));
		assertEquals("Number of hairs", 55, ArchetypeDao.getHairs().length);
		
		System.out.println("Faces: "+ Arrays.toString(ArchetypeDao.getFaces()));
		assertEquals("Number of faces", 30, ArchetypeDao.getFaces().length);
		
		System.out.println("Bodies: "+ Arrays.toString(ArchetypeDao.getBodies()));
		assertEquals("Number of bodies", 30, ArchetypeDao.getBodies().length);
	}
}
