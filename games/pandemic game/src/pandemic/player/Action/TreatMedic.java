package pandemic.player.Action;

import pandemic.game.listchooser.*;
import pandemic.board.Board;
import pandemic.player.Player;
import pandemic.city.City;
import pandemic.disease.Disease;

import java.util.*;



/**
 * A class Of Treating a disease by Medic
 * 
 * @author pandéquatre
 */
public class TreatMedic implements Action {
	/**  the board of the game*/
	private Board board;

	/**
	 * Create the treating Action
	 * @param board the board where we apply the action
	 */
	public TreatMedic(Board board) {
		this.board = board;
	}

	/**
	 * get rid of all the cubes of a disease, if it is cured then this doesn't count as an action
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
        int count = playerCity.getDiseases().get(d);
        System.out.println(count);
        for(int i = 0; i < count; i++) this.board.treatCity(playerCity,d);
        // think about how to not make this count as an action
        if (d.isCured()) return 0;
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