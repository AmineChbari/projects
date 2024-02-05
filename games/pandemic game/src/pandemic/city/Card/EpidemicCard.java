package pandemic.city.Card;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.disease.Disease;
import pandemic.player.Player;
import pandemic.util.GameOverException;

/**
 * class representing an Epidemic Card extending from Card class
 * 
 * @author pandéquatre
 */
public class EpidemicCard extends Card {

	/** the description on the card */
	protected String description;

	/**
	 * create a Epidemic Card with a steps to follow on it
	 */
	public EpidemicCard() {
		this.description = "Epidemic Card[Steps: Increase -> Infect -> Intensify]";
	}
	
	/** 
	 * compare two epidemic cards based on their nature and description
	 * @param x the object to compare with
	 * @return true if they are equal, false otherwise
	 */
	public boolean equals(Object x){
	if (x instanceof EpidemicCard) {
		EpidemicCard y = (EpidemicCard) x;
		return this.description.equals(y.toString());
	}
		return false;
	}

	
	/** 
	 * String representation of the Epidemic card
	 * @return description
	 */
	public String toString() {
		return this.description;
	}

	@Override
	public void ability(Board board, Player player, Disease disease) throws GameOverException {
		//1--increase
		// Increase the infection rate for the game if it should be
		board.updateInfectionRate();
		//2--Infect
		// Get the bottom card from the infection deck
		InfectionCard UpperCard = board.getInfectionCardDeck().Pop(); //change check by popping
		// searching for the city in the map 
		City cityToInfect = UpperCard.getCity();
		// Infect 3 time the new city with the disease from the UpperCard card
		for (int i = 0; i < 3; i++) {
		 	board.InfectCity(cityToInfect,UpperCard.getDiseaseType());
		}
		//3--Intensify
		board.getInfectionCardDeck().discard(UpperCard);
		board.getInfectionCardDeck().addDiscardToPile();
		board.getPlayerCardDeck().discard(this);
	}
}
