package main;

import pandemic.board.Board;
import pandemic.game.Game;
import pandemic.player.*;
import pandemic.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * the fourth main class
 *
 * @author pandéquatre
 */
public class Livrable4Main {
	
	/**
	 * default constructor
	 */
	public Livrable4Main(){
	}
	
	/**
	* the main methods
	* @param args the link of the JSON
	*/
    public static void main(String[] args) {
    	
    	
        String mapFilename = args.length == 1 ? args[0] : "Maps/MapInit.json";
        Board myboard = new Board(mapFilename);

        int nb_players = 4;
        if (args.length == 2) {
            try {
                nb_players = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number of players specified.");
                System.exit(1);
            }
            if (nb_players > 4 || nb_players < 2) {
                System.err.println("Number of players should be 2, 3, or 4. Default is 4.");
                System.exit(1);
            }
        }

        List<Player> players = new ArrayList<>();
        switch (nb_players) {
            case 2:
                players.add(new Globetrotter(null, "P1", myboard));
                players.add(new Medecine(null, "P2", myboard));
                break;
            case 3:
                players.add(new Globetrotter(null, "P1", myboard));
                players.add(new Medecine(null, "P2", myboard));
                players.add(new Expert(null, "P3", myboard));
                break;
            case 4:
                players.add(new Globetrotter(null, "P1", myboard));
                players.add(new Medecine(null, "P2", myboard));
                players.add(new Scientist(null, "P3", myboard));
                players.add(new Expert(null, "P4", myboard));
                break;
            default:
                break;
        }

        System.out.println("Step 1: Creating the board.");
        System.out.println("Step 2: Creating decks of cards and " + nb_players + " players.");
        System.out.println("Step 3: Play.");
        
        myboard.init(players);
        Game G = new Game(myboard);
        while(true){
            try {
                G.play(players);
            } catch (GameWinException e) {
            	System.out.println("\n\n" + Color.GREEN.getColor() + "YOU WIN : " + Color.BLACK.getColor() + e.getMessage());
                System.exit(1);
            } catch (GameOverException e) {
            	System.out.println("\n\n" + Color.RED.getColor() + "GAME OVER ! " + Color.BLACK.getColor() + e.getMessage());
                System.exit(1);
            }
        }
    }
}
