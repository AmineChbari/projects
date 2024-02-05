package pandemic.city.Card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.disease.*;
import pandemic.player.*;

public class PlayerCardTest {
    private PlayerCard Card1;
    private PlayerCard Card2;
    private PlayerCard Card3;
    private PlayerCard Card4;
    
    @Before
	public void init() {
        City x = new City("RABAT",Disease.JAUNE);
        City z = new City("LILLE",Disease.NOIR);
        Card1 = new PlayerCard(x,x.getCityDisease());
        Card2 = new PlayerCard(z,z.getCityDisease());
        Card3 = new PlayerCard(z,z.getCityDisease());
        Card4 = new PlayerCard(x,Disease.BLEU);
    }

    @Test
    public void testGetters() {
        assertEquals(Card1.getCity().getName(),"RABAT");
        assertEquals(Card1.getDiseaseType(),Disease.JAUNE);
    }

    @Test
    public void testEquals() {
        assertTrue(Card2.equals(Card3));
        assertFalse(Card1.equals(Card2));
        assertFalse(Card3.equals(Card4));
    }

   
    @Test
    public void testToString() {
        String expectedCard1 = String.format("[City: %s, Disease: %s]", Card1.getCity().getName(), Card1.getDiseaseType().toString());
        assertEquals(expectedCard1, Card1.toString());
    
        String expectedCard2 = String.format("PlayerCard[City:%sDisease:%s]", Card2.getCity().getName(), Card2.getDiseaseType().toString());
        assertTrue(expectedCard2.equals(expectedCard2.toString()));
    
        String expectedCard3 = String.format("[City: %s, Disease: %s]", Card3.getCity().getName(), Card3.getDiseaseType().toString());
        assertEquals(expectedCard3, Card3.toString());
    }
    
    @Test
    public void testAbility() {
    	Board board = new Board("Maps/MapInit.json");
    	Player player = new Medecine(new City("ville", Disease.NOIR), "player", new Board("Maps/MapInit.json"));
    	assertFalse(player.getHandDeck().contains(this.Card1));
    	this.Card1.ability(board, player, Disease.NOIR);
    	assertTrue(player.getHandDeck().contains(this.Card1));
    }
    
    public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.city.Card.PlayerCardTest.class);
	}
    
}
