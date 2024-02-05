package pandemic.player.Action;

import pandemic.game.listchooser.*;
import pandemic.board.Board;
import pandemic.player.Player;
import pandemic.city.City;
import pandemic.disease.Disease;

import java.util.*;



/**
 * A class of Treating a disease
 * 
 * @author pandéquatre
 */
public class Treat implements Action {
	/**  the board of the game*/
	private Board board;

	/**
	 * Create the treating Action
	 * @param board the board where we apply the action
	 */
	public Treat(Board board) {
		this.board = board;
	}

	/**
	 * get rid of one cube of a disease if not cured, otherwise get rid of all the cubes of that disease
	 * @return 1 for consuming one action
	 */
	@Override
	public int doSomething() {
		Player p = this.board.getCurrentPlayer();
		City playerCity = p.getCity();
		ListChooser<Disease> DiseaseToChoose = new RandomListChooser<>();
		List<Disease> mList = new ArrayList<>();
		mList.addAll(playerCity.getDiseases().keySet());
		Disease d = DiseaseToChoose.choose("CHOSE DISEASE TO TREAT: ",mList);
		System.out.println("Choice : " + d);
		if(d.isCured()){
			int count = playerCity.getDiseases().get(d);
			for(int i = 0; i < count; i++) {
				this.board.treatCity(playerCity,d);
			}
		}	
		else {
			this.board.treatCity(playerCity,d);
		}
		
		return 1;
	}

	/**
	 * represents the Action of Treating
	 * @return String representation of Treating 
	 */
	public String toString() {
		return "TreatDisease";
	}
}