package pandemic.player;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.player.Action.*;

/**
 * class representing a Scientist role player
 * 
 * @author pandéquatre
 */
public class Scientist extends Player{
	
	/**
	 * Constructor for the Scientist class.
	 * 
	 * @param city the city where the Scientist player starts
	 * @param name the name of the Scientist player
	 * @param board the board
	 */
	public Scientist(City city, String name, Board board) {
		super(city, name, board);
		this.ActionsArray.add(new Move(this.board));
		this.ActionsArray.add(new Construct(this.board));
		this.ActionsArray.add(new CureScientist(this.board));
		this.ActionsArray.add(new Treat(this.board));
		this.ActionsArray.add(new NoneAction(this.board));
	}
	
	
	/**
	 * Returns a string representation of the Scientist player.
	 * 
	 * @return a string representation of the Scientist player
	 */
	public String toString() {
		return "Scientist";
	}

}








