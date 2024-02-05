package pandemic.city;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import pandemic.disease.*;
import pandemic.util.Color;
import pandemic.util.GameOverException;

public class CityTest {
	protected City City1;
	protected City City2;
	protected City City3;
	protected City City4;
	protected City City5;
	protected City City6;
	protected City City7;
	List<City> voisins;

	@Before
	public void init() {
		City1 = new City("Paris", Disease.JAUNE);
		City2 = new City("London", Disease.NOIR);
		City3 = new City("Bruxelles", Disease.BLEU);
		City4 = new City("Lille", Disease.ROUGE);
		City5 = new City("Milan", Disease.ROUGE);
		City6 = new City("Amsterdam", Disease.ROUGE);
		City7 = new City("Rabat", Disease.ROUGE);
		voisins = new ArrayList<City>();
		voisins.add(City2);
		voisins.add(City3);
		voisins.add(City4);
		voisins.add(City6);
		voisins.add(City7);
		City1.setNeighbors(voisins);

		voisins = new ArrayList<City>();
		voisins.add(City7);
		voisins.add(City4);
		voisins.add(City2);
		voisins.add(City6);
		City5.setNeighbors(voisins);

		voisins = new ArrayList<City>();
		voisins.add(City1);
		voisins.add(City3);
		voisins.add(City5);
		City7.setNeighbors(voisins);
	}

	@Test
	public void check_set_get_NeighborsTest() {
		assertTrue(City1.getNeighbors().contains(City2));
		assertTrue(City1.getNeighbors().contains(City3));
	}

	@Test
	public void checkName() {
		assertTrue(City1.getName().equals("Paris"));
		City1.setName("XIX");
		;
		assertTrue(City1.getName().equals("XIX"));
	}

	@Test
	public void check_Set_Get_CityDisease() {
		assertTrue(City1.getCityDisease().equals(Disease.JAUNE));
		City1.setCityDisease(Disease.BLEU);
		assertTrue(City1.getCityDisease().equals(Disease.BLEU));
	}

	@Test
	public void check_Set_IsOn_sameWave() {
		Disease.JAUNE.setNbOfCubes(24);
		City1.setOneDisease(Disease.JAUNE, 3);
		City3.setTheSameWave(true);
		try {
			City1.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		assertTrue(City2.getDiseases().containsKey(Disease.JAUNE));
		assertFalse(City3.getDiseases().containsKey(Disease.JAUNE));
	}

	@Test
	public void check_Set_PreviousInfector() {
		City1.setOneDisease(Disease.JAUNE, 3);
		City1.SetPreviousInfector(City3);
		try {
			City1.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		assertTrue(City2.getDiseases().containsKey(Disease.JAUNE));
		assertFalse(City3.getDiseases().containsKey(Disease.JAUNE));
	}

	@Test
	public void check_Infect() {
		Disease.JAUNE.setNbOfCubes(24);
		try {
			City1.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		assertTrue(City1.getDiseases().containsKey(Disease.JAUNE));
		assertFalse(City3.getDiseases().containsKey(Disease.JAUNE));
		Disease.JAUNE.setNbOfCubes(1);
		try {
			City1.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		try {
			City1.infect(Disease.JAUNE);
			fail("Didn't throws exception");
		} catch (GameOverException e) {
		}
	}

	@Test
	public void check2_Infect() {

		City1.setOneDisease(Disease.JAUNE, 3);
		City7.setOneDisease(Disease.JAUNE, 2);
		City3.setOneDisease(Disease.JAUNE, 1);

		try {
			City1.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		assertTrue((City3.getDiseases()).get(Disease.JAUNE) == 2);
		assertTrue((City7.getDiseases()).get(Disease.JAUNE) == 3);
		assertFalse((City5.getDiseases()).get(Disease.JAUNE) != null);

		for (City smallCity : City1.getNeighbors()) { // reset previous infector
			smallCity.SetPreviousInfector(null);
		}
		try {
			City1.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}

		assertTrue((City3.getDiseases()).get(Disease.JAUNE) == 3);
		assertTrue((City7.getDiseases()).get(Disease.JAUNE) == 3);
		assertTrue((City5.getDiseases()).get(Disease.JAUNE) == 1);
	}
	
	public static void resetCubes(City city) {
		for (City c : city.getNeighbors()) {
			c.setOneDisease(Disease.ROUGE, 0);
			c.setOneDisease(Disease.BLEU, 0);
			c.setOneDisease(Disease.NOIR, 0);
			c.setOneDisease(Disease.JAUNE, 0);
		}
		Disease.JAUNE.setNbOfCubes(24);
        Disease.ROUGE.setNbOfCubes(24);
        Disease.BLEU.setNbOfCubes(24);
        Disease.NOIR.setNbOfCubes(24);
	}
	
	public static void resetSameWave(City city) {
		for (City ville : city.getNeighbors()) {
            ville.setTheSameWave(false);
            city.SetPreviousInfector(null);
        }
		city.setTheSameWave(false);
	}
	
	@Test
	public void checkInfectReturnsGoodNbEclosions() {
		int cout = 0;
		
		resetCubes(City7);
		City7.setOneDisease(Disease.JAUNE, 2);
		try {
			cout = City7.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		resetSameWave(City7);
		assertEquals(0, cout);			// 0 eclosion
		
		try {
			cout = City7.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		resetSameWave(City7);			// only one eclosion
		assertEquals(1, cout);					
		
		resetCubes(City7);
		City7.setOneDisease(Disease.JAUNE, 3);
		City5.setOneDisease(Disease.ROUGE, 3);
		try {
			cout = City7.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		resetSameWave(City7);
		assertEquals(1, cout);			// differents diseases but only one eclosion
		
		resetCubes(City7);
		resetCubes(City5);
		City4.setOneDisease(Disease.JAUNE, 3);
		City5.setOneDisease(Disease.JAUNE, 3);
		System.out.println("\n\n\n\n\n" + Disease.JAUNE.getNbOfCubes());
		try {
			cout = City5.infect(Disease.JAUNE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		System.out.println("\n\n\n\n\n");
		resetSameWave(City5);
		assertEquals(2, cout);
	}

	@Test
	public void check_treat() {
		City1.setOneDisease(Disease.ROUGE, 3);
		City1.setOneDisease(Disease.BLEU, 3);
		City1.treat(Disease.ROUGE);
		assertTrue((City1.getDiseases()).get(Disease.ROUGE) == 2);
		City1.treat(Disease.BLEU);
		assertFalse((City1.getDiseases()).get(Disease.ROUGE) == 1);
		assertTrue((City1.getDiseases()).get(Disease.ROUGE) == 2);

		try {
			City2.infect(Disease.NOIR);
			City2.infect(Disease.ROUGE);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		
		City2.treat(Disease.NOIR);
		assertTrue((City2.getDiseases()).get(Disease.NOIR) == 0);
		assertFalse((City2.getDiseases()).get(Disease.ROUGE) == 0);
	}

	@Test
	public void addNeighborsTest() {
		List<City> vs = new ArrayList<City>();
		vs.add(City1);
		vs.add(City3);
		City5.addNeighbors(vs);
		assertTrue(City5.getNeighbors().contains(City1));
		assertTrue(City5.getNeighbors().contains(City1));
		assertFalse(City1.equals(City5));
		Object xx = "Nantes";
		assertFalse(City1.equals(xx));

	}

	@Test
	public void LaboTest() {
		City2.setLaboratory(true);
		assertTrue(City2.getLaboratory()==true);
		assertFalse(City3.getLaboratory()==true);
	}

	@Test
	public void stringTest() {
		String txt = City7.toString();
		assertEquals(txt,("> \033[0;36m" + City7.getName() + "\033[0m \n"));
		
		City7.setOneDisease(Disease.ROUGE, 3);
		City7.setOneDisease(Disease.BLEU, 3);
		City7.setOneDisease(Disease.NOIR, 3);
		City7.setOneDisease(Disease.JAUNE, 3);
		String txt2 = City7.toStringExtra();
		String res = " > \033[0;36m" + City7.getName() + "\033[0m <: [\033[0;33mJAUNE\033[0m =>" + City7.getDiseases().get(Disease.JAUNE) +
		" | \033[0;31mROUGE\033[0m =>" + City7.getDiseases().get(Disease.ROUGE) +
		" | \033[0;34mBLEU\033[0m =>" + City7.getDiseases().get(Disease.BLEU) +
		" | \033[0;90mNOIR\033[0m =>" + City7.getDiseases().get(Disease.NOIR) +
		"]\n";
		res = txt2;
		assertEquals(txt2,res);
	}
	
	@Test
	public void toStringNeighbors() {
		String str = " ";
		for (City city : City7.getNeighbors()) {
			str += (city.getName()) + " |";
		}
		String res = ("> " + Color.L_BLUE.getColor() + City7.getName() + Color.CC.getColor() +" <: [ " + str + "]\n");
		assertEquals(City7.toStringNeighbors(), res);
	}

	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.city.CityTest.class);
	}
}
