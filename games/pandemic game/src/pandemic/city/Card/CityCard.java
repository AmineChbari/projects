package pandemic.city.Card;
import pandemic.disease.*;
import pandemic.city.*;

/**
 * Abstract Class representing a Card of a City 
 * The card contains the name and the disease of the city
 * 
 * @author pandéquatre
 */
public abstract class CityCard extends Card {
	/** the name of The city in the card */
	protected City city;
	/** the disease in the city of the card */
    protected Disease theDisease;

	/** 
	 * Create a Card with the given city and the given disease
	 * @param city a city
	 * @param x a disease
	*/
	public CityCard(City city, Disease x) {
		this.city = city;
		this.theDisease = x;
	}

	
	/** 
	 * get the name of the city in the card
	 * @return city name
	 */
	public City getCity(){
		return this.city;
	}

	/** 
	 * get the disease of the city in the card
	 * @return city disease
	 */
	public Disease getDiseaseType(){
		return this.theDisease;
	}

	/** 
	 * represents the card with a string
	 * @return the card info
	 */
	public String toString(){
		return "City: " + city.getName() + ", Disease: " + theDisease.toString();
	}

}
