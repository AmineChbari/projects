package pandemic.player.Action;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.city.Card.PlayerCard;
import pandemic.player.Player;

/**
 * The Construct class implements the Action interface 
 * to construct a laboratory in the current player's city.
 * 
 * @author pandéquatre
 */
public class Construct implements Action {
	
	/** the game board */
	private Board board;
	

	/**
     * Constructor for the Construct class.
     * 
     * @param board the current game board
     */
	public Construct(Board board) {
		this.board = board;
	}

	/**
     * Performs the construction of a laboratory in the current player's city if the player has the appropriate card.
     * @return the cost in action points
     */
	@Override
	public int doSomething() {
		Player p = this.board.getCurrentPlayer();
		City playerCity = p.getCity();
		PlayerCard card = p.getCardByCity(playerCity);
		if (card != null) {
			this.board.addResearchStation(playerCity);
			p.removeCard(card);
		}
		return 1;
	}

	/**
     * text representation of the Construct action.
	 * @return a string representation of the Construct action
     */
	public String toString() {
		return "Construct";
	}

}
