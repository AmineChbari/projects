package pandemic.city.Card;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.disease.*;
import pandemic.player.Medecine;
import pandemic.player.Player;
import pandemic.util.GameOverException;

public class EpidemicCardTest {
    private EpidemicCard Card1;
    private EpidemicCard Card2;
    
    @Test
    public void oneTest() {
        Card1 = new EpidemicCard();
        Card2 = new EpidemicCard();
        City x = new City("California",Disease.JAUNE);
        PlayerCard Card3 = new PlayerCard(x,Disease.JAUNE);
        assertTrue(Card1.equals(Card2));
        assertFalse(Card1.equals(Card3));
        assertTrue( ("Epidemic Card[Steps: Increase -> Infect -> Intensify]").equals(Card1.toString()));
        assertFalse(("EpidemicCard").equals(Card2.toString()));
    }
    
    @Test
    public void testequals() {
    	EpidemicCard card1 = new EpidemicCard();
    	EpidemicCard card2 = new EpidemicCard();
    	boolean result = card1.equals(card2); 
    	assertTrue(result == true); 
    	String stringObject = "this is a string"; 
    	boolean resultr = card1.equals(stringObject); 
    	assertTrue(resultr == false);
    }
    
    @Test
    public void testAbility() {
    	Card Card1 = new EpidemicCard();
    	Board board = new Board("Maps/MapInit.json");
    	Player player = new Medecine(new City("ville", Disease.NOIR), "player", new Board("Maps/MapInit.json"));
    	List<Player> liste = new ArrayList<Player>();
    	liste.add(player);
    	board.init(liste);
    	assertEquals(2, board.getInfectionRate());
    	board.updateInfectionRate();
    	board.updateInfectionRate();
    	assertEquals(2, board.getInfectionRate());
    	try {
			Card1.ability(board, player, Disease.NOIR);
		} catch (GameOverException e) {
			fail("throws exception");
		}
    	assertEquals(3, board.getInfectionRate());
    }


    public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.city.Card.EpidemicCardTest.class);
	}
    
}
