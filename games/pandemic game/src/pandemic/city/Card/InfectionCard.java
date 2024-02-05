package pandemic.city.Card;

import pandemic.disease.*;
import pandemic.player.Player;
import pandemic.util.GameOverException;
import pandemic.board.Board;
import pandemic.city.*;

/**
 * class representing an Infection Card extending from CityCard
 * 
 * @author pandéquatre
 */
public class InfectionCard extends CityCard {

	/**
	 * Create a Infection Card with the given name and the given disease
	 * @param nameOfCity a city
	 * @param x a disease
	 */
	public InfectionCard(City nameOfCity, Disease x) {
		super(nameOfCity,x);
	}

	/**
	 * compare two Infection cards based on their names and diseases
	 * 
	 * @param x the object to compare with
	 * @return true if they are equal, false otherwise
	 */
	public boolean equals(Object x){
		if (x instanceof InfectionCard) {
			InfectionCard y = (InfectionCard) x;
			return y.getCity().equals(this.city);
		}
		else return false;
	}

	/**
	 * String representation of the Infection card
	 * @return the info on the card
	 */
	public String toString(){
		return ("InfectionCard ["+ super.toString()+ ']');
	}


	public void ability(Board board, Player player, Disease disease) throws GameOverException {
		board.InfectCity(this.city, disease);
		board.getInfectionCardDeck().discard(this);
	}
}
