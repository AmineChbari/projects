package pandemic.city.Card;

import pandemic.board.Board;
import pandemic.disease.Disease;
import pandemic.player.Player;
import pandemic.util.GameOverException;

/**
 * Abstract Class representing a simple card
 * 
 * @author pandéquatre
 */
public abstract class Card {
	
	/** default constructor
	 * 
	 */
	public Card() {
	}
	
	/** create a Card
	 * @param board the board
	 * @param player the player
	 * @param disease the disease
	 * @throws GameOverException game is over
	 */
	public abstract void ability(Board board, Player player, Disease disease) throws GameOverException;
    
    /**
     * Abstract method representing the card with a string
     */
    public abstract String toString();

    /** 
	 * abstract method that compare two cards 
	 * @param x the object to compare with
	 * @return true if they are equal, false otherwise
	 */
    public abstract boolean equals(Object x);
}
