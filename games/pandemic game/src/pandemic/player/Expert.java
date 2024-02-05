package pandemic.player;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.player.Action.*;

/**
 * class representing an expert role player
 * 
 * @author pandéquatre
 */
public class Expert extends Player{
	
	/**
	 * Constructor for the Expert class.
	 * 
	 * @param city the city where the Expert player starts
	 * @param name the name of the Expert player
	 * @param board the board
	 */
	public Expert(City city, String name, Board board) {
		super(city, name, board);
		this.ActionsArray.add(new Move(this.board));
		this.ActionsArray.add(new ConstructExpert(this.board));
		this.ActionsArray.add(new Cure(this.board));
		this.ActionsArray.add(new Treat(this.board));
		this.ActionsArray.add(new NoneAction(this.board));
	}

	/**
	 * Returns a string representation of the Expert player.
	 * 
	 * @return a string representation of the Expert player
	 */
	public String toString() {
		return "Expert";
	}
}
