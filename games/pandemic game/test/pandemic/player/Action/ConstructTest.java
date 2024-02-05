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
import pandemic.player.*;
import pandemic.util.GameWinException;


public class ConstructTest {
	private Board board;
	private Action construct;
	private City city1;
	private City city2;
	
	@Before 
	public void init() {
		this.board = new Board("Maps/MapInit.json");
		List<Player> players = new ArrayList<Player>();
		City startingCity = new City("city1", Disease.NOIR);
		this.city1 = startingCity;
		this.city2 = new City("city2", Disease.BLEU);
		Player p = new Expert(startingCity, "expert", this.board);
		
		List<PlayerCard> cards = new ArrayList<PlayerCard>();
		PlayerCard c3 = new PlayerCard(new City("city3", Disease.JAUNE), Disease.JAUNE);
		PlayerCard c1 = new PlayerCard(startingCity, Disease.NOIR);
		Collections.addAll(cards, c1, c3);
		p.setCards(cards);
		players.add(p);
		
		this.board.init(players);
		this.construct = new Construct(board);
	}
	
	@Test
	public void initConstructTest() {
		
	}
	
	@Test
	public void doSomethingTest() {
		Player p = this.board.getCurrentPlayer();
		assertTrue(p.getCity().getLaboratory()); // a déjà la carte et commence sur la ville de la carte
		try {
			this.construct.doSomething();
		} catch (GameWinException e) {
			fail("throws exception");
		}
		assertTrue(p.getCity().getLaboratory());
		p.setCity(this.city2);
		assertFalse(p.getCity().getLaboratory());
		try {
			this.construct.doSomething();
		} catch (GameWinException e) {
			fail("throws exception");
		}
		assertFalse(p.getCity().getLaboratory());
		PlayerCard c2 = new PlayerCard(this.city2, Disease.NOIR);
		List<PlayerCard> liste = new ArrayList<>();
		liste.add(c2);
		p.setCards(liste);
		try {
			this.construct.doSomething();
		} catch (GameWinException e) {
			fail("throws exception");
		}
		assertTrue(p.getCity().getLaboratory());
	}
	
	@Test
	public void toStringTest() {
		assertEquals("Construct", this.construct.toString());
	}

	
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.player.Action.ConstructTest.class);
	}
}

