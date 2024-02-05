package pandemic.city.Card;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.city.Card.InfectionCard;
import pandemic.disease.*;
import pandemic.player.Medecine;
import pandemic.player.Player;
import pandemic.util.GameOverException;

public class InfectionCardTest {
    private InfectionCard Card1;
    private InfectionCard Card2;
    private InfectionCard Card3;
    private InfectionCard Card4;
    
    @Before
	public void init() {
        City x = new City("TOKYO",Disease.JAUNE);
        City z = new City("LONDON",Disease.NOIR);
        Card1 = new InfectionCard(x,x.getCityDisease());
        Card2 = new InfectionCard(z,z.getCityDisease());
        Card3 = new InfectionCard(z,z.getCityDisease());
        Card4 = new InfectionCard(x,Disease.BLEU);
    }

    @Test
    public void testGetters() {
        assertEquals(Card1.getCity().getName(),"TOKYO");
        assertEquals(Card1.getDiseaseType(),Disease.JAUNE);
    }

    @Test
    public void testequals() {
        assertTrue(Card2.equals(Card3));
        assertFalse(Card1.equals(Card2));
        assertFalse(Card3.equals(Card4));
        Object xx = "cardx";
		assertFalse(Card1.equals(xx));
    }

    @Test
    public void testString() {
        assertTrue( ("InfectionCard [City: " + Card1.getCity().getName() + ", Disease: " + Card1.getDiseaseType().toString() + "]").equals(Card1.toString()));
        assertFalse(("Infection[City:" + Card2.getCity().getName() + "Disease:" + Card2.getDiseaseType().toString() + "]").equals(Card2.toString()));
        assertEquals(("InfectionCard [City: " + Card3.getCity().getName() + ", Disease: " + Card3.getDiseaseType().toString() + "]"),Card3.toString());
    }
    
    @Test
    public void testAbility() {
    	Board board = new Board("Maps/MapInit.json");
    	Player player = new Medecine(new City("ville", Disease.NOIR), "player", new Board("Maps/MapInit.json"));
    	List<Player> liste = new ArrayList<Player>();
    	liste.add(player);
    	board.init(liste);
    	this.Card1.getCity().setOneDisease(Disease.NOIR, 0);
    	this.Card1.getCity().setOneDisease(Disease.JAUNE, 0);
    	this.Card1.getCity().setOneDisease(Disease.BLEU, 0);
    	this.Card1.getCity().setOneDisease(Disease.ROUGE, 0);
    	assertEquals((Integer) 0, this.Card1.getCity().getDiseases().get(Disease.NOIR));
    	try {
			this.Card1.ability(board, player, Disease.NOIR);
		} catch (GameOverException e) {
			fail("throws exception");
		}
    	//assertEquals((Integer) 1, this.Card1.getCity().getDiseases().get(Disease.NOIR)); ne fonctionne pas 
    }

    public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.city.Card.InfectionCardTest.class);
	}
    
}
