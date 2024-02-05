package pandemic.game;

import pandemic.board.Board;
import pandemic.player.Player;
import pandemic.player.Action.*;
import pandemic.util.GameOverException;
import pandemic.util.GameWinException;
import pandemic.game.listchooser.*;
import pandemic.city.Card.*;

import java.util.*;

/**
 * a class that represents the game that controls the playing process
 *
 * @author pandéquatre
 *
 */
public class Game {

	/** The board of the game*/
	private Board board;
	/** responsible for Random choosing*/
	public static ListChooser<Action> lc = new RandomListChooser<>();
	/** Number of actions per player (4 actions)*/
	private static final int NB_TOUR = 4;
	/** max number of cars in the player hand */
	private static final int maxNbCardsInHand = 7;
	
	/**
	 * Construct a new game for the board
	 * @param myboard the board of the game
	 */
	public Game(Board myboard) {
		this.board = myboard;
	}

	/**
	 * Allows players to start playing
	 * 
	 * @param pls players of the game
	 * @throws GameOverException game over alert
	 * @throws GameWinException game won alert
	 * 
	 */
	public void play(List<Player> pls) throws GameOverException, GameWinException {
		
		for(Player player : pls) {
			this.board.setCurrentPlayer(player);
			// 4 ACTIONS
			System.out.println("\nstep3: play 4 actions for each player:");
			int NbTour = NB_TOUR;
			while(NbTour > 0){
				System.out.println("\nPlayer : "+player);
				int x =player.play();
				NbTour-=x;
			}

			// 2 PLAYER CARDS
			System.out.println("\nstep4: each player draws 2 player cards and adds them or triggers an epidemic:");
			System.out.println("drawn cards :\n");
        	for (int i=0; i<2; i++) {
        		Card card = this.board.getPlayerCardDeck().Pop();
        		System.out.println(card);
				int size = player.getHandDeck().size();
        		if (size >= maxNbCardsInHand) {
        			Random rand = new Random();
        			Card card2 = player.getHandDeck().get(rand.nextInt(size));
        			System.out.println("Too many cards, removing card : " + card2);
        			player.getHandDeck().remove(card2);
        			this.board.getPlayerCardDeck().discard(card2);
        		}
        		card.ability(this.board, player, null);
        	}
        	
        	// 2 INFECTION CARDS
        	System.out.println("\nstep5: draw 2 cards from the infection and infect card pile:");
        	System.out.println("drawn cards :\n");
        	for (int i = 0; i < 2; i++) {
        		InfectionCard c = this.board.getInfectionCardDeck().Pop();
        		c.ability(this.board, pls.get(0), c.getDiseaseType());
        		System.out.println(c);
        	}
			this.board.Reset_SameWave();
			
		}
		System.out.println(this.board);
	}
	
	/**
	 * getter for the board
	 * @return the board
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * representation of the game
	 * @return String representation
	 */
	@Override
	public String toString() {
		return this.board.toString();
	}

}
