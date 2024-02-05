package pandemic.board;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;
import pandemic.city.*;
import pandemic.disease.*;
import pandemic.player.*;
import pandemic.util.GameOverException;


public class BoardTest {
	private Board board;

	@Before
	public void init() {
		this.board = new Board("Maps/MapInit.json");
		List<Player> players = new ArrayList<Player>();
		City startingCity = new City("test", Disease.NOIR);
		players.add(new Expert(startingCity, "expert", this.board));
		players.add(new Globetrotter(startingCity, "expert", this.board));
		players.add(new Medecine(startingCity, "expert", this.board));
		players.add(new Scientist(startingCity, "expert", this.board));
		this.board.init(players);
	}

	@Test
	public void initboardTest() {
		assertNotNull(this.board.getMapCities());
		for (City city : this.board.getMapCities()) {
			assertNotNull(this.board.getNeighborsForACity(city));
		}
		assertNotNull(this.board.diseases);
		assertEquals(2, this.board.getInfectionRate());
		assertEquals(0,this.board.getNbEclosion());
		assertEquals(4, this.board.getDiseases().size());
		assertTrue(this.board.players.get(0).getCity().getLaboratory());
		assertEquals(1, this.board.getNbReseachStation()); //cette ligne ne fonctionne pas 
		assertNotNull(this.board.players.get(0).getHandDeck().get(1));
		assertEquals(24, Disease.JAUNE.getNbOfCubes());
		assertEquals(24, Disease.ROUGE.getNbOfCubes());
		assertEquals(24, Disease.BLEU.getNbOfCubes());
		assertEquals(24, Disease.NOIR.getNbOfCubes());
	}

	@Test
	public void mapDataTestCheckGetMapCitiesNeighborsAndDisease() {
		List<City> cities = this.board.getMapCities();
		this.board.setDisease();
		for (City city : cities) {
			assertEquals(city.getNeighbors(), this.board.getNeighborsForACity(city));
			Set<Disease> diseases = city.getDiseases().keySet();
			for (Disease disease : diseases) {
				assertTrue(this.board.getDiseases().containsKey(disease));
			}
		}
	}

	@Test
	public void checkTheDiseaseColor() {
		assertEquals(Disease.JAUNE, this.board.getDiseaseColorOfCity(0));
		assertEquals(Disease.ROUGE, this.board.getDiseaseColorOfCity(1));
		assertEquals(Disease.BLEU, this.board.getDiseaseColorOfCity(2));
		assertEquals(Disease.NOIR, this.board.getDiseaseColorOfCity(3));
		assertEquals(Disease.ROUGE, this.board.getDiseaseColorOfCity(1));
	}


	@Test
	public void checkInitCityDisesas() {
		List<City> cities = this.board.getMapCities();
		for (City city :cities ) {
			assertSame(0, city.getDiseases().get(Disease.NOIR));
			assertSame(0, city.getDiseases().get(Disease.JAUNE));
			assertSame(0, city.getDiseases().get(Disease.BLEU));
			assertSame(0, city.getDiseases().get(Disease.ROUGE));
		}
	}

	@Test
	public void checkJsonExtractCities() {
		assertEquals(this.board.cities().size(), 48);
		assertTrue(this.board.cities().containsKey("ville-1"));
    	assertSame(this.board.cities().get("ville-1"), 2);
    	assertTrue(this.board.cities().containsKey("ville-2"));
    	assertSame(this.board.cities().get("ville-2"), 1);
	}

	@Test
	public void checkJsonExtractNeighbours(){
		assertSame(this.board.neighbors().size(), 48);
		assertTrue(this.board.neighbors().containsKey("ville-1"));
    	assertTrue(this.board.neighbors().get("ville-1").contains("ville-2"));
    	assertTrue(this.board.neighbors().get("ville-1").contains("ville-7"));
    	assertTrue(this.board.neighbors().containsKey("ville-2"));
    	assertTrue(this.board.neighbors().get("ville-2").contains("ville-1"));
    	assertTrue(this.board.neighbors().get("ville-2").contains("ville-3"));
	}
	@Test
	public void checkJsonDataSetMapAndNeighbourInBoard() {
		assertSame(this.board.getMapCities().size(), 48);
		assertTrue(this.board.getMapCities().get(0) instanceof City);
	}

	@Test
	public void CheckUpdateInfectionRate() {
		int infectionRate = this.board.getInfectionRate();
		this.board.updateInfectionRate();
		assertEquals(infectionRate, this.board.getInfectionRate());
		this.board.updateInfectionRate();
		this.board.updateInfectionRate();
		this.board.updateInfectionRate();
		assertEquals(infectionRate+1, this.board.getInfectionRate());
	}

	@Test
	public void checkAddCityToMapCities() {
		City city1 = new City("Paris", Disease.JAUNE);
		City city2 = new City("Lyon", Disease.BLEU);
		this.board.addCity(city1);
		this.board.addCity(city2);
		assertTrue(this.board.getMapCities().contains(city1));
		assertTrue(this.board.getMapCities().contains(city2));
	}

	@Test
	public void checkInfectCity() {
		City city = this.board.getMapCities().get(0);
		Disease diseaseNOIR = Disease.NOIR;
		try {
			this.board.InfectCity(city, diseaseNOIR);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		assertSame(1, city.getDiseases().get(diseaseNOIR));
		try {
			this.board.InfectCity(city, diseaseNOIR);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		assertSame(2, city.getDiseases().get(diseaseNOIR));
	}
	
	@Test
	public void checkTreatCity() {
		City city = this.board.getMapCities().get(0);
		Disease diseaseNOIR = Disease.NOIR;
		assertSame(0, city.getDiseases().get(diseaseNOIR));
		try {
			this.board.InfectCity(city, diseaseNOIR);
			this.board.InfectCity(city, diseaseNOIR);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		assertSame(2, city.getDiseases().get(diseaseNOIR));
		this.board.treatCity(city, diseaseNOIR);
		assertSame(1, city.getDiseases().get(diseaseNOIR));
		assertFalse(diseaseNOIR.isErradicated());
		int tempo = diseaseNOIR.getNbOfCubes();
		diseaseNOIR.setNbOfCubes(23);
		diseaseNOIR.cure();
		this.board.treatCity(city, diseaseNOIR);
		assertEquals(24, diseaseNOIR.getNbOfCubes());
		assertTrue(diseaseNOIR.isErradicated());
		diseaseNOIR.setNbOfCubes(tempo);
		assertSame(0, city.getDiseases().get(diseaseNOIR));
	}

	@Test
	public void checkResetSameWave(){
		City city = this.board.getMapCities().get(0);
		city.setTheSameWave(true);
		this.board.Reset_SameWave();
		assertFalse(city.IsOnSameWave());
	}

	@Test
	public void checkResetPreviousInfector() {
		City city = this.board.getMapCities().get(0);
		City previousInfector = new City("thisCity", Disease.BLEU);
		city.SetPreviousInfector(previousInfector);
		this.board.Reset_PreviousInfector(city);
		assertFalse(city.checkPreviousInfector(previousInfector));
	}

	@Test
    public void testGetCityByName() {
        City city1 = new City("city1", Disease.NOIR);
        City city2 = new City("city2",Disease.NOIR);
        board.getMapCities().add(city1);
        board.getMapCities().add(city2);
        City result = board.getCityByName("city1");
        assertEquals(city1, result);
    }
	
	@Test
	public void checkAddResearchStation() {
		assertEquals(1, this.board.getNbReseachStation());
		this.board.addResearchStation(new City("city2", Disease.NOIR));
		this.board.addResearchStation(new City("city3", Disease.NOIR));
		this.board.addResearchStation(new City("city4", Disease.NOIR));
		this.board.addResearchStation(new City("city5", Disease.NOIR));
		this.board.addResearchStation(new City("city6", Disease.NOIR));
		this.board.addResearchStation(new City("city7", Disease.NOIR));
		this.board.addResearchStation(new City("city8", Disease.NOIR));
		assertEquals(6, this.board.getNbReseachStation());
		City c9 = new City("city9", Disease.NOIR);
		this.board.addResearchStation(c9);
		assertEquals(6, this.board.getNbReseachStation());
		assertEquals(6, this.board.citiesWithResearchStation.size());
		assertTrue(this.board.citiesWithResearchStation.contains(c9));
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
	
	@Test
	public void checkInfectCityThrowsExceptionForEclosions() {
		City city = this.board.getMapCities().get(0);
		resetCubes(city);
		this.board.setNbEclosion(7);
		try {
			this.board.InfectCity(city, Disease.BLEU);
			this.board.InfectCity(city, Disease.BLEU);
			this.board.InfectCity(city, Disease.BLEU);
		} catch (GameOverException e) {
			fail("Throws exception");
		}
		try {
			this.board.InfectCity(city, Disease.BLEU);
			fail("Didn't throws exception");
		} catch (GameOverException e) {
		}
	}
	@Test
    public void testGetNumOfCities() {
        assertEquals(48, this.board.getNumOfCities());
    }

	@Test
	public void checkToString() {	
		String expected = "\n" + "//-----------------PLAYING BOARD-----------------\\\\" + "\n"
				+ "Map with [0 City,Research Stations: 0,Infection Rate: 0 ]\n"
				+ "\n MENU OF THE PLAYERS :\n\n"
				+ "\n MENU OF THE NEIGHBORS :\n"
				+ "\n STATUT OF ALL THE CITIES:\n"
				+ "\n STATUT OF DISEASES:\n"
				+ "\n\\\\" + "-----------------------------------------------//" + "\n";
		String result = board.toString();
		assertNotSame(expected, result);
	}
	
	
	
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.board.BoardTest.class);
	}
}