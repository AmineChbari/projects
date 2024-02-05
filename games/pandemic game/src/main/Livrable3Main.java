package main;
import pandemic.disease.Disease;
import pandemic.board.*;
import pandemic.player.*;
import pandemic.city.*;
import pandemic.city.Card.*;
import pandemic.game.Game;

import java.util.*;

/**
 * the third main class
 *
 * @author pandéquatre
 */
public class Livrable3Main {
	
	/**
	 * default constructor
	 */
	public Livrable3Main(){
	}

    /**
    * the main methods
    * @param args the link of the JSON
    */
    public static void main(String[] args) {

    	//STEP 1
		String mapFilename="";
		if (args.length == 0) mapFilename= "Maps/MapInit.json"; // Load the map from the default mini Map json file
		else if (args.length > 0) mapFilename = args[0];  // Load the map from the JSON file argument
        Board myboard =new Board(mapFilename);
        System.out.println("step1: creating the board:");

        //STEP 2
        // Create PLAYERS
        List<Player> players = new ArrayList<Player>();
        players.add(new Globetrotter(null,"P1",myboard));
        players.add(new Medecine(null,"P2",myboard));
        players.add(new Scientist(null,"P3",myboard));
        players.add(new Expert(null,"P3",myboard));
        System.out.println("step2: creation of decks of cards and 4 players:");

        //STEP 3 PLAY
        Game G = new Game(myboard);
        G.play(players);
    }
}

