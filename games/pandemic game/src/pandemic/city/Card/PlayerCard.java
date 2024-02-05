package pandemic.city.Card;

import pandemic.disease.*;
import pandemic.player.Player;

import java.util.Random;

import pandemic.board.Board;
import pandemic.city.*;

/**
 * class representing an Playing Card extending from CityCard
 * 
 * @author pandéquatre
 */
public class PlayerCard extends CityCard {

	/**
	 * Create a Playing Card with the given name and the given disease
	 * @param nameOfCity a city
	 * @param x a disease
	 */
	public PlayerCard(City nameOfCity, Disease x) {
		super(nameOfCity,x);
	}

	/**
	 * compare two playing cards based on their names and diseases
	 * 
	 * @param x the object to compare with
	 * @return true if they are equal, false otherwise
	 */
	public boolean equals(Object x){
		if (x instanceof PlayerCard) {
			PlayerCard y = (PlayerCard) x;
			return y.getCity().equals(this.city);
		}
		else return false;
	}

	/**
	 * String representation of the Player card
	 * @return the info on the card
	 */
	public String toString(){
		return ("["+ super.toString()+ ']');
	}

	@Override
	public void ability(Board board, Player player, Disease disease) {
		player.getHandDeck().add(this);
	}

}
