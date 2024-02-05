package pandemic.player.Action;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.disease.Disease;
import pandemic.player.*;
import pandemic.util.GameWinException;


public class TreatMedicTest {
	private Board board;
	private Action treat;
	private City city1;
	private Player p;
	
	@Before 
	public void init() {
		this.board = new Board("Maps/MapInit.json");
		List<Player> players = new ArrayList<Player>();
		this.city1 = new City("city1", Disease.NOIR);
		this.p = new Medecine(this.city1, "medic", this.board);
		players.add(this.p);
		//this.board.init(players);
		this.treat = new TreatMedic(this.board);
	}
	
	@Test
	public void initTreatTest() {
		
	}
	
	@Test
	public void doSomethingTest() {
		Disease.NOIR.setNbOfCubes(10);
		Disease.ROUGE.setNbOfCubes(10);
		Disease.BLEU.setNbOfCubes(10);
		Disease.JAUNE.setNbOfCubes(10);
		Disease.NOIR.setCured(false);
		Disease.ROUGE.setCured(false);
		Disease.BLEU.setCured(false);
		Disease.JAUNE.setCured(false);
		Disease.NOIR.setErradicated(false);
		Disease.ROUGE.setErradicated(false);
		Disease.BLEU.setErradicated(false);
		Disease.JAUNE.setErradicated(false);
		this.board.setCurrentPlayer(this.p);
		this.city1.setOneDisease(Disease.NOIR, 3);
		this.city1.setOneDisease(Disease.JAUNE, 3);
		this.city1.setOneDisease(Disease.ROUGE, 3);
		this.city1.setOneDisease(Disease.BLEU, 3);
		int total = 0;
		int cout = 0;
		total += this.city1.getDiseases().get(Disease.NOIR);
		total += this.city1.getDiseases().get(Disease.JAUNE);
		total += this.city1.getDiseases().get(Disease.ROUGE);
		total += this.city1.getDiseases().get(Disease.BLEU);
		assertTrue(total == 12);
		try {
			cout = this.treat.doSomething();
		} catch (GameWinException e) {
			fail("throws exception");
		}
		assertEquals(1, cout);
		total = 0;
		total += this.city1.getDiseases().get(Disease.NOIR);
		total += this.city1.getDiseases().get(Disease.JAUNE);
		total += this.city1.getDiseases().get(Disease.ROUGE);
		total += this.city1.getDiseases().get(Disease.BLEU);
		assertTrue(total == 9);
		Disease.NOIR.cure();
		Disease.JAUNE.cure();
		Disease.ROUGE.cure();
		Disease.BLEU.cure();
		this.board.setCurrentPlayer(this.p);
		this.city1.setOneDisease(Disease.NOIR, 3);
		this.city1.setOneDisease(Disease.JAUNE, 3);
		this.city1.setOneDisease(Disease.ROUGE, 3);
		this.city1.setOneDisease(Disease.BLEU, 3);
		try {
			cout = this.treat.doSomething();
		} catch (GameWinException e) {
			fail("throws exception");
		}
		assertEquals(0, cout);
		total = 0;
		total += this.city1.getDiseases().get(Disease.NOIR);
		total += this.city1.getDiseases().get(Disease.JAUNE);
		total += this.city1.getDiseases().get(Disease.ROUGE);
		total += this.city1.getDiseases().get(Disease.BLEU);
		System.out.println("\n\n\n"+total+" "+this.p + " "+this.p.getCity());
		System.out.println(Disease.NOIR.getStatus());
		System.out.println(Disease.ROUGE.getStatus());
		System.out.println(Disease.JAUNE.getStatus());
		System.out.println(Disease.BLEU.getStatus());
		assertTrue(total == 9);
		this.city1.setOneDisease(Disease.NOIR, 3);
		this.city1.setOneDisease(Disease.JAUNE, 3);
		this.city1.setOneDisease(Disease.ROUGE, 3);
		this.city1.setOneDisease(Disease.BLEU, 3);
		try {
			this.treat.doSomething();
		} catch (GameWinException e) {
			fail("throws exception");
		}
		total = 0;
		total += this.city1.getDiseases().get(Disease.NOIR);
		total += this.city1.getDiseases().get(Disease.JAUNE);
		total += this.city1.getDiseases().get(Disease.ROUGE);
		total += this.city1.getDiseases().get(Disease.BLEU);
		assertTrue(total == 9);
	}
	
	@Test
	public void toStringTest() {
		assertEquals("TreatDisease", this.treat.toString());
	}

	
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.player.Action.TreatMedicTest.class);
	}
}

