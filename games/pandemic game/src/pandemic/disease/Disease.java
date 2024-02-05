package pandemic.disease;

import pandemic.util.Color;

/**
 * Represents the diseases in the game
 *
 * @author pandéquatre
 */
public enum Disease {
	/** the fist Disease*/
	JAUNE(0),
	/** the second Disease*/
	ROUGE(1),
	/** the third Disease*/
	BLEU(2),
	/** the fourth Disease*/
	NOIR(3);

	/*
	 * value of disease color
	 */
	private final Integer value;

	/**
	 * true if there is a remedy for the Disease, false otherwise
	 */
	private boolean cured;
	/**
	 * true if the Disease is erradicated, false otherwise
	 */
	private boolean erradicated;
	
	private int nbOfCubes;

	/*
	public final String Blue = "\033[0;34m";
	public final String Grey = "\033[0;90m";
	public final String Red = "\033[0;31m";
	public final String Yellow = "\033[0;33m";
	public final String Black = "\033[0m";
	public final String Green = "\033[0;32m";
	*/

	/**
	 * creates a Disease
	 */
	private Disease(Integer value) {
		this.value = value;
		this.cured = false;
		this.erradicated = false;
		this.nbOfCubes = 0;
	}

	/**
	 * returns a disease value
	 * @return a disease value
	 */
	public Integer getValue() {
		return this.value;
	}

	/**
	 * returns true if the Disease is cured, false otherwise
	 * @return true if the Disease is cured, false otherwise
	 */
	public boolean isCured() {
		return this.cured;
	}

	/**
	 * returns true if the Disease is erradicated, false otherwise
	 * @return true if the Disease is erradicated, false otherwise
	 */
	public boolean isErradicated() {
		return this.erradicated;
	}

	/**
	 * puts cured to true
	 */
	public void cure() {
		this.cured = true;
	}
	
	/**
	 * change cured value
	 * 
	 * @param value the new value
	 */
	public void setCured(boolean value) {
		this.cured = value;
	}
	
	/** change the value of erradicated
	 * @param value the value
	 */
	public void setErradicated(boolean value) {
		this.erradicated = value;
	}

	/**
	 * puts erradicated to true
	 */
	public void erradicate() {
		this.erradicated = true;
	}

	/**
	 * returns a long description of the current state of the Disease
	 * @return a long description of the current state of the Disease
	 */
	public String getStatus() {
		String cured = "not ";
		String erradicated = "not ";
		if (this.cured) {
			if (this.erradicated) {
				erradicated = "";
			}
			cured = "";
		}
		return this.toString() + " is " + cured + "cured and " + erradicated + "erradicated";
	}
	
	/** returns the nb of cubes
	 * @return the nb of cubes
	 */
	public int getNbOfCubes() {
		return this.nbOfCubes;
	}
	
	/** change the nb of cubes
	 * @param n the new nb of cubes
	 */
	public void setNbOfCubes(int n) {
		this.nbOfCubes = n;
	}
	
	/** add cubes when they are removes from the board with treat and remove cubes when there is an infection

	 * @param n the nb of cubes
	 */
	public void AddRemoveFromNbOfCubes(int n) {
		this.nbOfCubes += n;
	}

	/**
	 * returns a short description of the current state of the Disease
	 * @return a short description of the current state of the Disease
	 */
	public String toString() {
		String color = Color.BLACK.getColor();
		if (this.name().equals("JAUNE")) color = Color.YELLOW.getColor();
		else if (this.name().equals("ROUGE")) color = Color.RED.getColor();
		else if (this.name().equals("NOIR")) color =  Color.GREY.getColor();
		else color = Color.BLUE.getColor();
		return color + this.name() + Color.BLACK.getColor() + " [cubes : " + this.nbOfCubes + "]";
	}
}
