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


public class MoveTest {
	private Board board;
	private Action move;
	private City city1;
	private City city2;
	private Player p;
	
	@Before 
	public void init() {
		this.board = new Board("Maps/MapInit.json");
		List<Player> players = new ArrayList<Player>();
		this.city1 = new City("city1", Disease.NOIR);
		this.city2 = new City("city2", Disease.NOIR);
		this.p = new Expert(this.city1, "expert", this.board);
		
		players.add(p);
		//this.board.init(players);
		this.move = new Move(this.board);
	}
	
	@Test
	public void initTreatTest() {
		
	}
	
	@Test
	public void doSomethingTest() {
		this.board.setCurrentPlayer(this.p);
		this.city1.setOneDisease(Disease.NOIR, 1);
		this.city1.setOneDisease(Disease.JAUNE, 1);
		this.city1.setOneDisease(Disease.ROUGE, 1);
		this.city1.setOneDisease(Disease.BLEU, 1);
		this.city1.setLaboratory(true);
		
		assertEquals(this.city1, this.p.getCity());
		List<City> n = new ArrayList<>();
		n.add(this.city2);
		this.city1.setNeighbors(n);
		try {
			this.move.doSomething();
		} catch (GameWinException e) {
			fail("throws exception");
		}
		assertEquals(this.city2, this.p.getCity());
	}
	
	@Test
	public void toStringTest() {
		assertEquals("Move", this.move.toString());
	}

	
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.player.Action.MoveTest.class);
	}
}

