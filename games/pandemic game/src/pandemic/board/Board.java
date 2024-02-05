package pandemic.board;

import java.io.*;
import java.util.*;

import org.json.*;

import pandemic.disease.*;
import pandemic.city.*;
import pandemic.city.Card.*;
import pandemic.player.*;
import pandemic.util.GameOverException;

/**
 * a class for pandemic game board
 * @author pandéquatre
 */

public class Board {
    /** variable json citymap*/
    protected JSONObject cityMap;
    /** list of cities*/
    protected List<City> mapCities;
    /** numbers of eclosion */
    protected int nbEclosion;
    /** variable for value of infection rate*/
    protected int infectionRate;
    /**variable for numbers of research Station   */
    protected int nbResearchStation;
    /** It is used to decide wether to increse infection rate or not*/
    protected int NbOfEpidemicCardFound;
    /** a map for representation of disease*/
    protected Map<Disease, Integer> diseases;
    /** int numbers of cities in board */
    protected int nbCities ;
    /** list of the players */
    protected List<Player> players;
    /** deck of player cards */
    protected Deck<Card> playerCardsDeck;
    /** deck of infection cards */
    protected Deck<InfectionCard> infectionCardDeck;
    /** maximum number of research station*/
    protected static final int maxNbResearchStation = 6;
    /** total number of cubes of a disease */
    protected static final int maxNbOfCubes = 24;
    /** maximum number of outbreak */
    protected static final int maxNbEclosion = 8;
    /** associates a disease with an int */
    protected static final Disease[] DISEASECOLOROFCITY= new Disease[] {Disease.JAUNE, Disease.ROUGE, Disease.BLEU, Disease.NOIR};
    /** the current player */
    protected Player currentPlayer;
    /** the cities with a research station */
    protected List<City> citiesWithResearchStation;


    /**
     * A Board constructor take file.json for argument
     * @param Filemane name of map's File.json
     */
    public Board(String Filemane) {
        try {
            FileReader reader = new FileReader(Filemane);
            this.cityMap = new JSONObject(new JSONTokener(reader));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.mapCities = new ArrayList<>();
        this.nbResearchStation = 0;
        this.infectionRate = 2;
        this.NbOfEpidemicCardFound = 1;
        this.nbEclosion = 0;
        this.diseases = new HashMap<>();
        this.nbCities = 0;
        this.currentPlayer = null;
        this.citiesWithResearchStation = new ArrayList<>();
       
    }

    // ---------------------- Json Data extraction -------------------------------//
    /**
     * parser for json object,extract map of cities
     * @return citiesNames a map of cities with associeted disease
     */
    public Map<String, Integer> cities() {
        Map<String, Integer> citiesName = new HashMap<>();
        JSONObject citiesJSON = this.cityMap.getJSONObject("cities");
        Iterator<String> entries = citiesJSON.keys();
        while (entries.hasNext()) {
            String cityName = entries.next();
            Integer disease = (Integer) citiesJSON.get(cityName);

            citiesName.put(cityName, disease);
        }
        return citiesName;
    }
    /**
     * parser for json object,extract map of neighbors
     * @return neighborsCities a map of cities with associeted neighbors
     */
    public Map<String, Set<String>> neighbors() {
        Map<String, Set<String>> neighborsCities = new HashMap<>();
        JSONObject neighborsJSON = this.cityMap.getJSONObject("neighbors");
        Iterator<String> entries = neighborsJSON.keys();
        while (entries.hasNext()) {
            String entryKey = entries.next();
            JSONArray neighborsArray = neighborsJSON.getJSONArray(entryKey);
            Set<String> neighborName = new HashSet<>();
            for (Object neighborsCity : neighborsArray) {
                String nName = (String) neighborsCity;
                neighborName.add(nName);
            }
            neighborsCities.put(entryKey, neighborName);
        }
        return neighborsCities;
    }

    // --------------------------------------------------------------------------------------//

    /**
     * get cities on this map
     *
     * @return a list of cities
     */
    public  List<City> getMapCities() {
        return this.mapCities;
    }
    /**
     * get nb of cities on this map
     * @return int nb of cities
     */
    public int getNumOfCities() {
        return this.nbCities;
    }

    /**
     * add cities from the map to a list mapCities
     */
    public void setMapCities() {
        Set<String> cities = this.cities().keySet();
        for (String city : cities) {
            Integer diseaseCityValue = this.cities().get(city);
            Disease diseaseOfCity = this.getDiseaseColorOfCity(diseaseCityValue);
            City c = new City(city, diseaseOfCity);
            this.mapCities.add(c);
            this.nbCities++;
        }
    }

    /**
     * set all cities and their neighbours
    */
     public void setNeighborsCities(){
   		Set<String> allNeighbors = this.neighbors().keySet(); // neighbors du JSON
   		for (String cityName : allNeighbors){ // pour une ville de neighbors du JSON
   			Set<String> neighbors = this.neighbors().get(cityName);
   			List<City> neighborsOfTheCity = new ArrayList<>();
   			for (String neighbor : neighbors){ // pour une des voisines de cette ville
   				for (City city : this.mapCities){
   					if (neighbor.equals(city.getName())){ // on cherche l'object City correspondant au nom de la ville voisine
   						neighborsOfTheCity.add(city);
   					}
   				}
   			}
   			for (City city : this.mapCities){ // on ajoute la liste des voisins au
   				if (cityName.equals(city.getName())){
   					city.setNeighbors(neighborsOfTheCity);
   				}
   			}
   		}
   	}

    /**
     * initializes all the cities with 0 for each disease
     */
    public void InitCityDiseases() {
        for (City city : this.mapCities) {
            city.setOneDisease(Disease.JAUNE, 0);
            city.setOneDisease(Disease.ROUGE, 0);
            city.setOneDisease(Disease.BLEU, 0);
            city.setOneDisease(Disease.NOIR, 0);
        }
        Disease.JAUNE.setNbOfCubes(maxNbOfCubes);
        Disease.ROUGE.setNbOfCubes(maxNbOfCubes);
        Disease.BLEU.setNbOfCubes(maxNbOfCubes);
        Disease.NOIR.setNbOfCubes(maxNbOfCubes);
    }

    /**
     * method to initialize the board with many conditions
     * @param players list of players
     */
    public void init(List<Player> players){
    	// initialize the City's Diseases, Board diseases, mapCities and city' neighbors
    	this.setMapCities();
    	this.setNeighborsCities();
    	this.InitCityDiseases();
    	this.setDisease();
    	// initialize the Decks
    	this.infectionCardDeck = new Deck<InfectionCard>();
    	this.playerCardsDeck = new Deck<Card>();
        List<InfectionCard> infectionCards = new ArrayList<InfectionCard>();
        List<Card> playerCards = new ArrayList<Card>();
        for(City city : this.mapCities){
        	infectionCards.add(new InfectionCard(city, city.getCityDisease()));
        	playerCards.add(new PlayerCard(city, city.getCityDisease()));
        }
        Collections.shuffle(playerCards);
        // give cards to Players
        this.players = players;
        int nbOfPlayers = players.size();
        Random rand = new Random();
        City startingCity = this.mapCities.get(rand.nextInt(this.mapCities.size()-1));
        for(Player p : players) {
        	List<PlayerCard> cards = new ArrayList<PlayerCard>();
        	p.setCity(startingCity);
        	for (int i=0; i<6-nbOfPlayers; i++) {  // 2 players : 4 cards | 3 players : 3 cards | 4 players : 2 cards
        		PlayerCard c = (PlayerCard) playerCards.get(i);
            	playerCards.remove(i);
            	cards.add(c);
            }
        	p.setCards(cards);
        }
        // build a laboratory on the starting city
        this.addResearchStation(startingCity);
        this.infectionCardDeck.setPile(infectionCards);
        this.playerCardsDeck.setPlayerDeckPile(playerCards);
        
        this.currentPlayer = players.get(0);
    }


    /**
     * get all the neighbors of a city
     *
     * @param city, the city we want to get its neighbors
     * @return a List of cities
     */
    public List<City> getNeighborsForACity(City city) {
        return city.getNeighbors();
    }

    /**
     * get all the diseases
     * @return diseases
     */
    public Map<Disease, Integer> getDiseases() {
        return this.diseases;
    }

    /**
    * get a disease color of city
    * @param diseaseCityValue, value of city in citiesJSON
    * @return diseaseColor
    */
    public Disease getDiseaseColorOfCity(Integer diseaseCityValue){
    	return DISEASECOLOROFCITY[diseaseCityValue];
    }

    /**
     * Set disease in the Map
     */
    public void setDisease() {
        for (City city : this.mapCities) {
            Disease disease = city.getCityDisease();
            this.diseases.put(disease, city.getDiseases().get(disease));
        }
    }

    /**
     * get numbers of eclosion
     *
     * @return int numbers of eclosion
     */
    public int getNbEclosion() {
        return this.nbEclosion;
    }

    /**
     * get the number of remaining infections cubes which stand for infection rate
     *
     * @return an integer, the infection rate
     */
    public int getInfectionRate() {
        return this.infectionRate;
    }

    /**
     * get the number of current research station
     *
     * @return an integer
     */
    public int getNbReseachStation() {
        return this.nbResearchStation;
    }
    
    /** if the max nb of research station isn't reached add a city add a research station, otherwise remove another before
     * @param city the city
     */
    public void addResearchStation(City city) {
    	if (!city.getLaboratory()) {
    		if (this.nbResearchStation >= maxNbResearchStation) {
        		Random rand = new Random();
        		City randomCity = this.citiesWithResearchStation.get(rand.nextInt(this.citiesWithResearchStation.size()));
        		this.citiesWithResearchStation.remove(randomCity);
        		this.nbResearchStation -= 1;
        		randomCity.setLaboratory(false);
        	}
    		this.nbResearchStation += 1;
    		city.setLaboratory(true);
    		this.citiesWithResearchStation.add(city);
    	}
    }


    /**
     * Increase the infection rate for the game if it should be
     */
    public void updateInfectionRate() {
        if (this.NbOfEpidemicCardFound==3 || this.NbOfEpidemicCardFound==5) this.infectionRate++;
        this.NbOfEpidemicCardFound++;
    }

    /**
     * adding a new city in the map
     *
     * @param city the city to be added
     */
    public void addCity(City city) {
        this.mapCities.add(city);
    }

    /**
     * add a number of cube to a diseases from the stock of cubes if the number of
     * cubes for this diseases is under maximal cubes
     *
     * @param city    a city which the number of cubes is going to be added for a
     *                disease
     * @param disease disease which we infect with
     * @throws GameOverException game is over
     */
    public void InfectCity(City city, Disease disease) throws GameOverException {
    	int newEclosions = 0;
    	
    	if (! disease.isErradicated()) {
    		newEclosions = city.infect(disease);
            this.Reset_PreviousInfector(city);
    	}
    	this.nbEclosion += newEclosions;
    	if (this.nbEclosion >= maxNbEclosion) {
    		throw new GameOverException(this.nbEclosion + " outbreaks");
    	}
    }
    
    /** changes the nb of eclosions
     * @param n the nb of eclosion
     */
    public void setNbEclosion(int n) {
    	this.nbEclosion = n;
    }

    /**
     * delete a one cube for a diseases
     *
     * @param city  the city which the cube is going to be deleted for a disease
     * @param disease variable who represented a disease
     */
    public void treatCity(City city, Disease disease) {
        city.treat(disease);
        if (disease.getNbOfCubes() >= maxNbOfCubes && disease.isCured()) {
    		disease.erradicate();
    	}
    }

    /**
     * reset the wave so all the infection focus areas can be affected again
     * this action is called after we switch the turn of play to another player
     */
    public void Reset_SameWave() {
        for (City ville : this.mapCities) {
            ville.setTheSameWave(false);
        }
    }

    /**
     * Reset the previous infector of each city and make it null
     * this action is called after switching from an action to another
     * @param city represente the city
     */
    public void Reset_PreviousInfector(City city) {
        city.SetPreviousInfector(null);
    }

    /**
     * get a city by its name
     * 
     * @param name the name of the city
     * @return the City  with the matching name
     */
    public City getCityByName(String name) {
        for (City city : this.mapCities) {
            if (city.getName().equals(name)) {
                return city;
            }
        }
        return null;
    }


    
    /**
     * have the InfectionDeck
     * @return Deck of InfectionCard
     */
    public Deck<InfectionCard> getInfectionCardDeck(){
    	return this.infectionCardDeck;
    }

    /**
     * have the PlayerDeck
     * @return Deck of playerCard
     */
    public Deck<Card> getPlayerCardDeck(){
    	return this.playerCardsDeck;
    }

    /**
     * getter for the current player
     * @return current player 
     */
    public Player getCurrentPlayer() {
    	return this.currentPlayer;
    }
    
    /**
     * setter for the current player
     * @param p the current player
     */
    public void setCurrentPlayer(Player p) {
    	this.currentPlayer = p;
    }

    /**
     * String representation of the board
     * @return String representation of the board
     */
    public String toString() {
        String dis1 = ("Map with ["+this.getNumOfCities()+" City,Research Stations: " + this.nbResearchStation + ",Infection Rate: "
                + this.infectionRate + " ]\n");

        String disP = "\n MENU OF THE PLAYERS :\n";
        for (Player pl : this.players) 
            disP += pl.toString()+ "\n" + pl.toStringExtra() + "\n";
        
        String dis4 = "\n MENU OF THE NEIGHBORS :\n";
        for (City ville : this.mapCities)
            dis4 += ville.toStringNeighbors();

        String dis2 = "\n STATUT OF ALL THE CITIES:\n";
        for (City ville : this.mapCities)
            dis2 += ville.toStringExtra();

        String dis3 = "\n STATUT OF DISEASES:\n";
        for (Disease ml : this.diseases.keySet())
            dis3 += ml.getStatus() + "\n";

        return "\n" + "//-----------------PLAYING BOARD-----------------\\\\" + "\n"
                + dis1 + disP + dis4 + dis2 + dis3 +
                "\n\\\\" + "-----------------------------------------------//" + "\n";
        }
    }
