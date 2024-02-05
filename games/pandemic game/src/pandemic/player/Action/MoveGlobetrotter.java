package pandemic.player.Action;

import pandemic.game.listchooser.*;
import pandemic.board.Board;
import pandemic.player.Player;
import pandemic.city.City;

/**
 * Class implements the Action interface to Move a city.
 * 
 * @author pandéquatre
 */
public class MoveGlobetrotter implements Action {

	/** The board that will modified */
	private Board board;

	/**
	 * Constructor of special Moving Action
	 * @param board the board
	 */
	public MoveGlobetrotter(Board board) {
		this.board = board;
	}

	/**
	 * Move to a City of the board
	 * 
     * @return an integer indicating the status of the action (always returns 1)
     */
	@Override
	public int doSomething() {
		Player p = this.board.getCurrentPlayer();
		ListChooser<City> CityToChoose = new RandomListChooser<>();
		City destinationCity = CityToChoose.choose("CHOOSE CITY TO MOVE TO: ",this.board.getMapCities());
		System.out.println("Choice : " + destinationCity);
		p.setCity(destinationCity);
		return 1;
	}
	
	/**
	 * represents the action of moving
	 * @return string representing the action
	 */
	public String toString() {
		return "Globetrotter Moving";
	}
}