package de.jardas.drakensang.dao;

import org.junit.BeforeClass;
import org.junit.Test;

public class ArchetypeDaoTest {
	@BeforeClass
	public static void setUpClass() throws Exception {
        Class.forName("org.sqlite.JDBC");
	}

	@Test
	public void testLoadArchetypes() {
		ArchetypeDao.get("archetype_MI_KR_W");
	}
}
