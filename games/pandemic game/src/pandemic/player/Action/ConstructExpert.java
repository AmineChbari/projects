package pandemic.player.Action;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.city.Card.PlayerCard;
import pandemic.player.Player;

/**
 * Class implements the Action interface to construct a laboratory.
 * 
 * @author pandéquatre
 */
public class ConstructExpert implements Action{
	/** the board of the game */
	private Board board;
	
	/**
     * Constructor for the Construct class.
     * 
     * @param board the current game board
     */
	public ConstructExpert(Board board) {
		this.board = board;
	}

	/**
     * Performs the construction of a laboratory in the current player's city if the player has the appropriate card.
     * 
     * @return an integer indicating the status of the action (always returns 1)
     */
	@Override
	public int doSomething() {
		Player p = this.board.getCurrentPlayer();
		this.board.addResearchStation(p.getCity());
		return 1;
	}

	/**
     * Returns a string representation of the Construct action.
	 * @return a string 
     */
	public String toString() {
		return "Construct";
	}
}
