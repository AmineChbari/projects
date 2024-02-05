package pandemic.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import pandemic.city.Card.*;
import pandemic.util.GameOverException;

/**
 * Class representing the Cards Deck
 * 
 * @author pandéquatre
 */  
public class Deck<T extends Card> {
	/** Creating a stack of type T a cards pile*/
    private Stack<T> pile; // 
    /** Creating a list of type T a cards discard*/
    private List<T> discard;  
    
    /**
     * Create a new Deck with an empty pile and discard
     */
    public Deck() {
        this.pile = new Stack<>();
        this.discard = new ArrayList<>();
    }
    
    /**
     * adds the list of cards to the deck's pile
     * @param cards List of cards 
     */
    public void setPile(List<T> cards) {
    	this.pile.addAll(cards);
    	Collections.shuffle(this.pile);
    }

    /**
     * adds the list of cards to the Playerdeck's pile in a specific steps(devide to 4 piles, add Epidemic card to each one,
     * add them back to the main Playerdeck's pile)
     * @param cards List of cards 
     */
    @SuppressWarnings("unchecked")
    public void setPlayerDeckPile(List<T> cards) {
        //create the 4 stacks
        List<T> p1 = new ArrayList<>();
        List<T> p2 = new ArrayList<>();
        List<T> p3 = new ArrayList<>();
        List<T> p4 = new ArrayList<>();
        // add cards to the 4 stacks
        int len = cards.size();
        
        for (int i = 0; i < len; i++) {
            if (i < len/4) p1.add(cards.get(i));
            else if (i >= len/4 && i < len/2) p2.add(cards.get(i));
            else if (i >= len/2 && i < (len/4)*3) p3.add(cards.get(i));
            else p4.add(cards.get(i));
        }
        // add Epidemic card to each pile
        p1.add((T) new EpidemicCard());
        p2.add((T)new EpidemicCard());
        p3.add((T)new EpidemicCard());
        p4.add((T)new EpidemicCard());
        Collections.shuffle(p1);
        Collections.shuffle(p2);
        Collections.shuffle(p3);
        Collections.shuffle(p4);
        //add 4 stacks to the main pile
        this.pile.addAll(p1);
        this.pile.addAll(p2);
        this.pile.addAll(p3);
        this.pile.addAll(p4);
    }
    
    
    /** 
     * get the pile of the Deck
     * @return the PlayerDeck pile
     */
    public Stack<T> getPile() {
        return this.pile;
    }

    /**
     * get the top Card
     * @return the card in the top of the pile
     * @throws GameOverException game is over
     */
    public T Pop() throws GameOverException {
    	if (this.pile.isEmpty()) {
    		throw new GameOverException("cannot draw 2 player cards");
    	}
    	else {
    		return this.pile.pop();
    	}
    }
  
    /**
     * get rid of a Card
     * @param c the card to get rid of
     */
    public void discard(T c) {
    	this.discard.add(c);
    }
    
    /**
     * empty the discard back in the Pile 
     */
    public void addDiscardToPile() {
    	Collections.shuffle(this.discard);
    	this.pile.addAll(this.discard);
    	this.discard.clear();
    }
    
    /**
     * This function returns the size of the player's deck to know the numbers of card left to player.
     * 
     * @return The size of the deck.
     */
    public int getDeckSize() {
        return this.pile.size();
    }
}
