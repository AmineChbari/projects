package pandemic.player.Action;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.city.Card.PlayerCard;
import pandemic.disease.Disease;
import pandemic.game.Game;
import pandemic.player.*;
import pandemic.util.GameWinException;


public class CureTest {
	private Board board;
	private Action cure;
	private City city1;
	private Player p;
	private List<Disease> dList;
	
	@Before 
	public void init() {
		this.board = new Board("Maps/MapInit.json");
		List<Player> players = new ArrayList<Player>();
		this.city1 = new City("city1", Disease.NOIR);
		this.p = new Expert(this.city1, "expert", this.board);
		
		players.add(p);
		//this.board.init(players);
		this.cure = new Cure(this.board);
		this.dList = new ArrayList<>();
		this.dList.add(Disease.NOIR);
		this.dList.add(Disease.ROUGE);
		this.dList.add(Disease.BLEU);
		this.dList.add(Disease.JAUNE);
	}
	
	@Test
	public void initTreatTest() {
		
	}
	
	@Test
	public void isGameWinTest() {
		int nbCuredDiseases = 0;
		for (Disease d : this.dList) {		// reset for next tests
			d.setCured(false);  
			if (d.isCured()) {
				nbCuredDiseases++;
			}
		}
		assertEquals(0, nbCuredDiseases);
		try {
			Cure.isGameWin();
		} catch (GameWinException e) {
			fail("throw exception");
		}
		for (Disease d : this.dList) {
			d.cure();
		}
		nbCuredDiseases = 0;
		for (Disease d : this.dList) {
			if (d.isCured()) {
				nbCuredDiseases++;
			}
		}
		assertEquals(4, nbCuredDiseases);
		try {
			Cure.isGameWin();
			fail("didn't throw exception");
		} catch (GameWinException e) {
		}
		for (Disease d : this.dList) {		// reset for next tests
			d.setCured(false);  
		}
	}
	
	@Test
	public void doSomethingTest() {
		this.board.setCurrentPlayer(this.p);
		this.city1.setOneDisease(Disease.NOIR, 1);
		this.city1.setOneDisease(Disease.JAUNE, 1);
		this.city1.setOneDisease(Disease.ROUGE, 1);
		this.city1.setOneDisease(Disease.BLEU, 1);
		assertFalse(Disease.NOIR.isCured());
		try {
			this.cure.doSomething();
		} catch (GameWinException e1) {
			fail("throws exception");
		}
		int nbCuredDiseases = 0;
		for (Disease d : this.dList) {
			if (d.isCured()) {
				nbCuredDiseases++;
			}
		}
		assertEquals(0, nbCuredDiseases);
		
		List<PlayerCard> cards = new ArrayList<PlayerCard>();
		PlayerCard c1 = new PlayerCard(this.city1, Disease.NOIR);
		PlayerCard c2 = new PlayerCard(this.city1, Disease.NOIR);
		PlayerCard c3 = new PlayerCard(this.city1, Disease.NOIR);
		PlayerCard c4 = new PlayerCard(this.city1, Disease.NOIR);
		PlayerCard c5 = new PlayerCard(this.city1, Disease.NOIR);
		PlayerCard c21 = new PlayerCard(this.city1, Disease.ROUGE);
		PlayerCard c22 = new PlayerCard(this.city1, Disease.ROUGE);
		PlayerCard c23 = new PlayerCard(this.city1, Disease.ROUGE);
		PlayerCard c24 = new PlayerCard(this.city1, Disease.ROUGE);
		PlayerCard c25 = new PlayerCard(this.city1, Disease.ROUGE);
		PlayerCard c31 = new PlayerCard(this.city1, Disease.BLEU);
		PlayerCard c32 = new PlayerCard(this.city1, Disease.BLEU);
		PlayerCard c33 = new PlayerCard(this.city1, Disease.BLEU);
		PlayerCard c34 = new PlayerCard(this.city1, Disease.BLEU);
		PlayerCard c35 = new PlayerCard(this.city1, Disease.BLEU);
		PlayerCard c41 = new PlayerCard(this.city1, Disease.JAUNE);
		PlayerCard c42 = new PlayerCard(this.city1, Disease.JAUNE);
		PlayerCard c43 = new PlayerCard(this.city1, Disease.JAUNE);
		PlayerCard c44 = new PlayerCard(this.city1, Disease.JAUNE);
		PlayerCard c45 = new PlayerCard(this.city1, Disease.JAUNE);
		Collections.addAll(cards, c1, c2, c3, c4, c5, c21, c22, c23, c24, c25, c31, c32, c33, c34, c35, c41, c42, c43, c44, c45);
		p.setCards(cards);
		this.city1.setLaboratory(true);
		int cout = 0;
		try {
			cout = this.cure.doSomething();
		} catch (GameWinException e1) {
			fail("throws exception");
		}
		assertEquals(1, cout);
		nbCuredDiseases = 0;
		for (Disease d : this.dList) {
			if (d.isCured()) {
				nbCuredDiseases++;
			}
		}
		assertEquals(1, nbCuredDiseases);
		try {
			this.cure.doSomething();
		} catch (GameWinException e) {
			fail("throws exception");
		}
		nbCuredDiseases = 0;
		for (Disease d : this.dList) {
			d.cure();
			nbCuredDiseases++;
		}
		assertEquals(4, nbCuredDiseases);
		try {
			this.cure.doSomething();
			fail("didn't throws exception");
		} catch (GameWinException e) {
		}
	}
	
	@Test
	public void toStringTest() {
		assertEquals("Cure", this.cure.toString());
	}

	
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.player.Action.CureTest.class);
	}
}

