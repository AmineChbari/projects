package pandemic.player;

import java.util.*;

import pandemic.city.City;
import pandemic.city.Card.PlayerCard;
import pandemic.player.Action.*;
import pandemic.util.GameWinException;
import pandemic.board.Board;
import pandemic.game.Game;

/**
 * an abstract class representing a player
 * 
 * @author pandéquatre
 */
public abstract class Player {
	/**
	 * this city
	 */
	private City city;
	/**
	 * the player's name
	 */
	private String name;
	/**
	 * the hand of the player
	 */
	private List<PlayerCard> cards;
	/**
	 * the abilities of the player
	 */
	protected List<Action> ActionsArray;
	/**
	 * the board
	 */
	protected Board board;

	
	/**
	 * Constructor for the Player class.
	 * 
	 * @param city the city where the player starts
	 * @param name the name of the player
	 * @param board the board
	 */
	public Player(City city, String name, Board board) {
		this.city = city;
		this.name = name;
		this.board = board;
		this.cards = new ArrayList<PlayerCard>();
		this.ActionsArray = new ArrayList<>();
	}

	/**
	 * Returns the current city where the player is located.
	 * 
	 * @return the city where the player is located
	 */
	public City getCity() {
		return this.city;
	}

	/**
	 * Returns the name of the player.
	 * 
	 * @return the name of the player
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the player's hand of cards to the given list of player cards.
	 * 
	 * @param cards the list of player cards to set the player's hand to
	 */
	public void setCards(List<PlayerCard> cards) {
		this.cards.addAll(cards);
	}
	
	/** remove the card
	 * @param card the card
	 */
	public void removeCard(PlayerCard card) {
		this.cards.remove(card);
	}

	/**
	 * Returns the player's hand of cards.
	 * 
	 * @return the player's hand of cards
	 */
	public List<PlayerCard> getHandDeck(){
		return this.cards;
	}
	
	/** returns the corresponding player card
	 * @param city the city
	 * @return the corresponding player card
	 */
	public PlayerCard getCardByCity(City city) {
		for(PlayerCard card : this.cards) {
			if(card.getCity().equals(city)) {
				return card;
			}
		}
		return null;
	}

	/**
	 * set the player's location city
	 * @param city the location
	 */
	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * choose from Actions
	 * @return 1 if the action is consumed , otherwise 0
	 * @throws GameWinException game is win
	 */
	public int play() throws GameWinException {
		Action a = Game.lc.choose("Choisissez parmi", this.ActionsArray);
		System.out.println("Choice : "+a+"\n");
		return a.doSomething();
	}

	/**
	 * abstract method that returns a string representation of the player, including their name and their
	 * hand of cards.
	 * 
	 * @return a string representation of the player, including their name and their
	 * hand of cards
	 */
	public abstract String toString();
	

	/**
	 * Returns a string representation of the player's hand of cards.
	 * 
	 * @return a string representation of the player's hand of cards
	 */
	public String toStringExtra(){
		String str = "\n";
		for (PlayerCard cd : this.cards){
			str += "	" + cd.toString() + '\n';
		}
		return str;
	}

}
