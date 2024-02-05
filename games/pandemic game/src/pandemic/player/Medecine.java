package pandemic.player;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.player.Action.*;

/**
 * class representing a medecine role player
 * 
 * @author pandéquatre
 */
public class Medecine extends Player {
	
	/**
	 * Constructor for the Medecine class.
	 * 
	 * @param city the city where the Medic player starts
	 * @param name the name of the Medic player
	 * @param board the board
	 */
	public Medecine(City city, String name, Board board) {
		super(city, name, board);
		this.ActionsArray.add(new Move(this.board));
		this.ActionsArray.add(new Construct(this.board));
		this.ActionsArray.add(new Cure(this.board));
		this.ActionsArray.add(new TreatMedic(this.board));
		this.ActionsArray.add(new NoneAction(this.board));
	}

	/**
	 * Returns a string representation of the Medic player.
	 * 
	 * @return a string representation of the Medic player
	 */
	public String toString() {
		return "Medecine";
	}

}
