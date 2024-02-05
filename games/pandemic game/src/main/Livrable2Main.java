package main;
import pandemic.disease.Disease;
import pandemic.board.*;
import pandemic.player.*;
import pandemic.city.*;
import pandemic.city.Card.*;

import java.util.*;

/**
 * the second main class
 *
 * @author pandéquatre
 */
public class Livrable2Main {
	
	/**
	 * default constructor
	 */
	public Livrable2Main(){
	}

    /**
    * the main methods
    * @param args the link of the JSON
    */
    public static void main(String[] args) {

    	//STEP 1
		String mapFilename="";
		if (args.length == 0) mapFilename= "Maps/carte1.json"; // Load the map from the default mini Map json file
		else if (args.length > 0) mapFilename = args[0];  // Load the map from the JSON file argument
        Board myboard =new Board(mapFilename);
        System.out.println("step1: creating the board:");

        //STEP 2
        // Create PLAYERS
        List<Player> players = new ArrayList<Player>();
        players.add(new Globetrotter(null,"P1"));
        players.add(new Medecine(null,"P2"));
        players.add(new Scientist(null,"P3"));
        players.add(new Expert(null,"P3"));
        myboard.init(players);
        System.out.println("step2: creation of decks of cards and 4 players:");
        System.out.println(myboard);
        
        //STEP 3
        System.out.println("step3: draw 2 cards from the infection and infect card pile:");
        System.out.println("drawn cards :\n");
        InfectionCard c = myboard.getInfectionCardDeck().Pop();
        c.ability(myboard, players.get(0), c.getDiseaseType());
        System.out.println(c);
        System.out.println(myboard);
        c = myboard.getInfectionCardDeck().Pop();
        c.ability(myboard, players.get(0), c.getDiseaseType());
        System.out.println(c);
        System.out.println(myboard);
        
        //STEP 4
        System.out.println("step4: each player draws 2 player cards and adds them or triggers an epidemic:");
        Card card = null;
        for(Player p : players) {
        	System.out.println("drawn cards :\n");
        	for (int i=0; i<2; i++) {
        		card = myboard.getPlayerCardDeck().Pop();
        		System.out.println(card);
        		card.ability(myboard, p, Disease.NOIR);// ici ca n'as pas de sens d'infecter une ville avec disesaas noir! 
        	}
        	System.out.println(myboard);
        }
    }
}

