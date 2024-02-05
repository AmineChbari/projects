package pandemic.player.Action;

import pandemic.game.listchooser.*;
import pandemic.board.Board;
import pandemic.player.Player;
import pandemic.city.City;

/**
 * Class implements the Action interface to Move a Neighboring city.
 * 
 * @author pandéquatre
 */
public class Move implements Action {

	/** The board that will modified */
	private Board board;

	/**
	 * Constructor of the Move Action
	 * @param board the board
	 */
	public Move(Board board) {
		this.board = board;
	}

	/**
	 * Move a player from a city to a neighboring city
	 * 
     * @return an integer indicating the status of the action (always returns 1)
     */
	@Override
	public int doSomething() {
		Player p = this.board.getCurrentPlayer();
		ListChooser<City> CityToChoose = new RandomListChooser<>();
		City destinationCity = CityToChoose.choose("CHOOSE A NEIGHBOUR TO MOVE TO: ",p.getCity().getNeighbors());
		System.out.println("Choice : " + destinationCity);
		p.setCity(destinationCity);
		return 1;
	}

	/**
	 * represents the Move action
	 * @return string representation of the Move
	 */
	public String toString() {
		return "Move";
	}
}