package pandemic.player.Action;

import java.util.*;

import pandemic.game.listchooser.*;
import pandemic.board.Board;
import pandemic.city.City;
import pandemic.disease.Disease;
import pandemic.city.Card.PlayerCard;
import pandemic.player.Player;
import pandemic.util.GameWinException;


/**
 * Class implements the Action interface to special Cure a disease
 * 
 * @author pandéquatre
 */
public class CureScientist implements Action {
	
	/** The board that will modified */
	private Board board;
	
	/**
     * Constructor for the Cure Action of a scientist.
     * 
     * @param board the game board
     */
	public CureScientist(Board board) {
		//super(board);
		this.board = board;
	}
	
	/**
     * the method that is responsible of applying the Curing action of a scientist
	 * @throws GameWinException game is win
     * 
     * @return an integer indicating the status of the action (always returns 1)
     */
	@Override
	public int doSomething() throws GameWinException {
		Player p = this.board.getCurrentPlayer();
		ListChooser<Disease> DiseaseToChoose = new RandomListChooser<>();
		List<Disease> myList = new ArrayList<>();
		myList.addAll(p.getCity().getDiseases().keySet());
		Disease diseaseToCure = DiseaseToChoose.choose("CHOOSE DISEASE TO CURE: ",myList);
		System.out.println("Choice : " + diseaseToCure);
		int count  = 0;
		for(PlayerCard playerCard : p.getHandDeck()) {
			if(playerCard.getDiseaseType() == diseaseToCure) count++;
		}
		if(count > 3) {
			diseaseToCure.cure();
			Cure.isGameWin();
		}
		return 1;
	}
	/**
	 * A static method that checks if the cure scientist of all diseases results in a game win.
	 * 
	 * @throws GameWinException if the cure of all diseases results in a game win
	 */
	
	public static void isGameWin() throws GameWinException {
		Disease[] diseases = Disease.values();
		int compteur = 0;
		for (Disease d : diseases) {
			if (d.isCured()) {
				compteur++;
			}
		}
		if (compteur >= diseases.length) {
			throw new GameWinException(compteur + " remedies found");
		}
	}

    /** 
     * represents the curing action of scientist
     * 
     * @return string representation of the action
     */
	public String toString() {
		return "Scientist Curing";
	}

} 
