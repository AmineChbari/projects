package pandemic.player;

import pandemic.city.City;
import pandemic.player.Action.*;
import pandemic.board.Board;

/**
 * class representing a globetrotter role player
 * 
 * @author pandéquatre
 */
public class Globetrotter extends Player{
	
	/**
	 * Constructor for the Globetrotter class.
	 * 
	 * @param city the city where the globtrotter player starts
	 * @param name the name of the globetrotter player
	 * @param board the board
	 */
	public Globetrotter(City city, String name, Board board) {
		super(city, name, board);
		this.ActionsArray.add(new MoveGlobetrotter(this.board));
		this.ActionsArray.add(new Construct(this.board));
		this.ActionsArray.add(new Cure(this.board));
		this.ActionsArray.add(new Treat(this.board));
		this.ActionsArray.add(new NoneAction(this.board));
	}

	/**
	 * Returns a string representation of the globetrotter player.
	 * 
	 * @return a string representation of the globetrotter player
	 */
	public String toString() {
		return "Globetrotter";
	}

}
