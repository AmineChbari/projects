package pandemic.city;

import pandemic.disease.*;
import pandemic.util.Color;
import pandemic.util.GameOverException;

import java.util.*;

/**
 * a class that represents a city
 *
 * @author pandéquatre
 *
 */
public class City {

	/** the city name */
	private String name;
	/** the disease associated to the city */
	private Disease cityDisease;
	/** list of nighbors of this city */
	private List<City> neighbors;
	/**
	 * a boolean to decide wether this city had been infected on the turn of the
	 * same player (used in infect method)
	 */
	private boolean sameWave;
	/** the name of the previous infector (used in infect method) */
	private City previousInfector;
	/** a map to represent the 4 types of disease */
	private Map<Disease, Integer> diseases;
	/** true if there is a laboratory in the City */
	private boolean laboratory;
	/** max number of cubes causing infection of neighbours*/
	private static final int MAX_INFECTION_LEVEL = 3;

	// -----------------------Constructor-----------------------

	/**
	 * Create a city with a unique name and a predefined dominating color
	 * 
	 * @param name1   name of the city
	 * @param disease the disease predefined in the city
	 */
	public City(String name1, Disease disease) {
		this.name = name1;
		this.cityDisease = disease;
		this.diseases = new HashMap<Disease, Integer>();
		this.neighbors = new ArrayList<City>();
		this.sameWave = false;
		this.previousInfector = null;
		this.laboratory = false;
	}

	// -----------------------Name methods-----------------------

	/**
	 * get the name of the city
	 * 
	 * @return the name of city
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * set the name of the city
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	// -----------------------Disease methods-----------------------
	/**
	 * get the disease related to the city Location
	 * 
	 * @return the City disease
	 */
	public Disease getCityDisease() {
		return this.cityDisease;
	}

	/**
	 * mark the city with a disease to know its location and which group it belongs
	 * to.
	 * 
	 * @param cityDisease the disease to set in the city
	 */
	public void setCityDisease(Disease cityDisease) {
		this.cityDisease = cityDisease;
	}

	// -----------------------Map(four_diseases) methods-----------------------
	/**
	 * get the map representing the four diseases and their values
	 * 
	 * @return the map of diseases
	 */
	public Map<Disease, Integer> getDiseases() {
		return this.diseases;
	}

	/**
	 * Adding first disease to a City with a given number of cubes (initial values)
	 * 
	 * @param xX disease to start with in the City
	 * @param xY number of cubes of the disease
	 */
	public void setOneDisease(Disease xX, int xY) {
		this.diseases.put(xX, xY);
	}

	// -----------------------Neighbors methods-----------------------
	/**
	 * get the neighbors of the city
	 * 
	 * @return the neighbors
	 */
	public List<City> getNeighbors() {
		return this.neighbors;
	}

	/**
	 * set the neighbors of the city
	 * 
	 * @param neighbors the neighbors to set
	 */
	public void setNeighbors(List<City> neighbors) {
		this.neighbors = neighbors;
	}

	/**
	 * adding a list of neighbors to the city neighbours
	 * 
	 * @param Voisins neighbors to be added
	 */
	public void addNeighbors(List<City> Voisins) {
		for (City city : Voisins) {
			this.neighbors.add(city);
		}
	}

	// -----------------------SameWave methods-----------------------
	/**
	 * Decide whether the city is infected on the same turn of a player
	 * 
	 * @param x boolean value
	 */
	public void setTheSameWave(boolean x) {
		this.sameWave = x;
	}

	/**
	 * tells if the city is the city is infected on the same turn of a player or
	 * not.
	 * 
	 * @return true if the city is infected on the same turn of a player, false
	 *         otherwise
	 */
	public boolean IsOnSameWave() {
		return this.sameWave;
	}

	// -----------------------PreviousInfector methods-----------------------
	/**
	 * set the previous infector
	 * 
	 * @param ville the previous infector City
	 */
	public void SetPreviousInfector(City ville) {
		this.previousInfector = ville;
	}

	/**
	 * Know if the City given is the correct previous infector
	 * 
	 * @param ville the city to compare with
	 * @return true if the City given is the correct previous infector, false
	 *         otherwise
	 */
	public boolean checkPreviousInfector(City ville) {
		return this.previousInfector != null && this.previousInfector.equals(ville);
	}

	// -----------------------equals method-----------------------
	/**
	 * Compares two cities by comparing their names
	 * 
	 * @param med City to compare with
	 * @return true if the name given is equal to this city name,else otherwise
	 */
	public boolean equals(Object med) {
		if (med instanceof City) {
			City other = (City) med;
			return this.name.equals(other.getName());
		}
		return false;
	}

	// -----------------------Infect and treat methods-----------------------
	/**
	 * infect a city with a disease, if it has already 3 cubes of this disease,
	 * then infect the neighbors and respecting some rules while doing it
	 * 
	 * @param disease disease to infect with
	 * @throws GameOverException game is over
	 * @return number of outbreak
	 */
	public int infect(Disease disease) throws GameOverException {
		int res = 0;
		System.out.println("INFECTION "+disease+" "+this);
		if (disease.getNbOfCubes() <= 0) { 	// too many cubes of the disease on the Board
    		throw new GameOverException("No more cubes for " + disease);
    	}
		else if (!this.diseases.containsKey(disease)) {
			this.diseases.put(disease, 1);
		} else {
			if (this.diseases.get(disease) == MAX_INFECTION_LEVEL) { // Verifying if this city contains 3 infection level of this disease
				this.sameWave = true;
				for (City neighbor : this.neighbors) {
					if ((!neighbor.IsOnSameWave()) && (!this.checkPreviousInfector(neighbor))) { // not including the cities that have been infected the same wage
						// not including the previous Infector city
						neighbor.SetPreviousInfector(this);
						res += neighbor.infect(disease);
					}
				}
				res++;
			} else {
				int count = this.diseases.get(disease);
				count++;
				this.diseases.put(disease, count);
			}
		}
		disease.AddRemoveFromNbOfCubes(-1);
		return res;
	}

	/**
	 * decrease the level of infection of a specific disease
	 * 
	 * @param d disease to treat in the City
	 */
	public void treat(Disease d) {
		if (this.diseases.get(d) > 0 && !d.isErradicated()) {
			this.diseases.put(d, this.diseases.get(d)-1);
			d.AddRemoveFromNbOfCubes(1);
		}
	}

	// -----------------------Laboratory methods-----------------------
	/**
	 * return true if a laboratory is in the city, else return false
	 * 
	 * @return true if there is a laboratory in the city
	 */
	public boolean getLaboratory() {
		return this.laboratory;
	}

	/**
	 * set the value of laboratory of the city
	 * 
	 * @param construct true if a laboratory is newly built in this city, false if
	 *                  moved
	 */
	public void setLaboratory(boolean construct) {
		this.laboratory = construct;
	}

	// -----------------------toString methods-----------------------
	/**
	 * Description of the City
	 * 
	 * @return String description of the City
	 */
	public String toString() {
		return ("> "+ Color.L_BLUE.getColor() + this.name + Color.CC.getColor() +" \n");
	}
	
	/**
	 * Description of the City with his neighbors
	 * 
	 * @return String description of the City with its neighbors
	 */
	public String toStringNeighbors() {
		String str = " ";
		for (City city : this.neighbors) {
			str += (city.getName()) + " |";
		}
		return ("> " + Color.L_BLUE.getColor() + this.name + Color.CC.getColor() +" <: [ " + str + "]\n");
	}
	
	/**
	 * Description of the City with the valur for each disease
	 * 
	 * @return String description of the City
	 */
	public String toStringExtra() {
		String res = " > " + Color.L_BLUE.getColor() + this.name + Color.CC.getColor() +" <: " + Color.YELLOW.getColor() +"JAUNE" + Color.CC.getColor() + " => ";
		res+= this.diseases.get(Disease.JAUNE)!=0?(Color.GREEN.getColor() + this.diseases.get(Disease.JAUNE) +Color.CC.getColor()):(this.diseases.get(Disease.JAUNE));
		res+=" | " + Color.RED.getColor() +"ROUGE" + Color.CC.getColor() + " => ";
		res+= this.diseases.get(Disease.ROUGE)!=0?(Color.GREEN.getColor() + this.diseases.get(Disease.ROUGE) +Color.CC.getColor()):(this.diseases.get(Disease.ROUGE));
		res+=" | " + Color.BLUE.getColor() +"BLEU" + Color.CC.getColor() + " => ";
		res+= this.diseases.get(Disease.BLEU)!=0?(Color.GREEN.getColor() + this.diseases.get(Disease.BLEU) +Color.CC.getColor()):(this.diseases.get(Disease.BLEU));
		res+=" | " + Color.GREY.getColor() +"NOIR" + Color.CC.getColor() + " => ";
		res+= this.diseases.get(Disease.NOIR)!=0?(Color.GREEN.getColor() + this.diseases.get(Disease.NOIR) +Color.CC.getColor()):(this.diseases.get(Disease.NOIR));
		res+="]\n";

		return res;
	}
}