/**
 * 
 */
package pandemic.board;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pandemic.city.City;
import pandemic.city.Card.*;
import pandemic.disease.Disease;
import pandemic.util.GameOverException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pandéquatre
 *
 */

public class DeckTest {
	private Deck<Card> deck;
	private List<Card> cards;
	
    @Before
    public void init() {
        this.deck = new Deck<Card>();
        List<Card> cards = new ArrayList<Card>();
        City City1 = new City("Paris", Disease.JAUNE); 
        City City2 = new City("lille", Disease.NOIR);
        Card card1 = new InfectionCard(City1,City1.getCityDisease());
        Card card2 = new PlayerCard(City2,City2.getCityDisease());
        Card card3 = new EpidemicCard();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        this.cards = cards;
    }
    
    @Test
    public void testSetPile() {
    	this.deck.setPile(this.cards);
        assertEquals(3, this.deck.getDeckSize());
    }
    
    @Test
    public void testPop() {
    	this.deck.setPile(this.cards);
    	try {
			assertTrue(this.cards.contains(this.deck.Pop()));
			assertTrue(this.cards.contains(this.deck.Pop()));
			assertTrue(this.cards.contains(this.deck.Pop()));
		} catch (GameOverException e) {
			fail("throws exception");
		}
    	try {
			assertNull(this.deck.Pop());
			fail("Didn't throws exception");
		} catch (GameOverException e) {
		}
    }
    
    @Test
   	public void testDiscard() {
    	this.deck.setPile(this.cards);
    	Card card = null;
		try {
			card = this.deck.Pop();
		} catch (GameOverException e) {
			fail("throws exception");
		}
    	this.deck.discard(card);
        assertFalse(deck.getPile().contains(card));
    }

    @Test
    public void testAddDiscardToPile() {
    	this.deck.setPile(this.cards);
    	Card card1 = null;
    	Card card2 = null;
		try {
			card1 = this.deck.Pop();
			card2 = this.deck.Pop();
		} catch (GameOverException e) {
			fail("throws exception");
		}
    	
    	assertEquals(1, this.deck.getDeckSize());
    	this.deck.discard(card1);
    	this.deck.discard(card2);
    	this.deck.addDiscardToPile();
        assertEquals(3, this.deck.getDeckSize());
    }

    @Test
    public void testGetDeckSize() {
    	this.deck.setPile(this.cards);
        assertEquals(3, this.deck.getDeckSize());
    }

    @Test
    public void testSetPlayerDeckPile() {
    	this.deck.setPlayerDeckPile(this.cards);
        // we already have 3 cards in the deck so we must have 7 after adding 4 cards in the pile
        assertEquals(this.deck.getDeckSize(),7);

        int count = 0;
        for (Card myCard : this.deck.getPile()) {
            if (myCard instanceof EpidemicCard) count ++;
        }
        assertTrue(count == 5);
    }
    
    @Test
    public void PopThrowsGameOverException() {
    	int i = 0;
    	this.deck.setPile(this.cards);
    	int size = this.deck.getDeckSize();
    	while (i < size) {
    		try {
				this.deck.Pop();
			} catch (GameOverException e) {
				fail("Throws exception");
			}
    		i++;
    	}
    	
    	try {
			this.deck.Pop();
			fail("Didn't throws exception");
		} catch (GameOverException e) {
		}
    }

    public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.board.DeckTest.class);
	}
}
