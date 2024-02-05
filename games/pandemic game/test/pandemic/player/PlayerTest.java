package pandemic.player;

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

public class PlayerTest {
	private Player p;
	private List<PlayerCard> cards;
	
	@Before 
	public void init() {
		List<PlayerCard> cards = new ArrayList<PlayerCard>();
		PlayerCard c1 = new PlayerCard(new City("city1", Disease.JAUNE), Disease.JAUNE);
		PlayerCard c2 = new PlayerCard(new City("city2", Disease.BLEU), Disease.BLEU);
		PlayerCard c3 = new PlayerCard(new City("city3", Disease.ROUGE), Disease.ROUGE);
		PlayerCard c4 = new PlayerCard(new City("city4", Disease.NOIR), Disease.NOIR);
		Collections.addAll(cards, c1, c2, c3, c4);
		this.cards = cards;
		this.p = new Medecine(new City("city", Disease.NOIR), "name", new Board("Maps/MapInit.json")) ;
	}
	
	@Test
	public void initPlayerTest() {
		assertEquals("city", this.p.getCity().getName());
		assertEquals("name", this.p.getName());
	}
	
	@Test
	public void checkSetCards() {
		this.p.setCards(this.cards);
		assertEquals(this.cards, this.p.getHandDeck());

	}
	
	@Test
	public void checkGetHandDeck() {
		this.p.setCards(this.cards);
		assertEquals(this.cards, this.p.getHandDeck());
	}
	
	@Test
	public void checkToStringExtra() {
		this.p.setCards(cards);
		String str = "\n";
		for (PlayerCard cd : this.p.getHandDeck()){
			str += "	" + cd.toString() + '\n';
		}
		assertEquals(str, this.p.toStringExtra());
	}

	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.player.PlayerTest.class);
	}
}
