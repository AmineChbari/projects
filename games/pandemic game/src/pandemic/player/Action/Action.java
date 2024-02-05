package pandemic.player.Action;

import pandemic.util.GameWinException;

/**
 * Interface representing an Action
 * 
 * @author pandéquatre
 */
public interface Action {
	
	/** 
	 * responsible for applying the action 
	 * @throws GameWinException game is win
	 * 
	 * @return the cost of the action
	 */
	public int doSomething() throws GameWinException;

	/**
	 * string representation of the action
	 * @return string representation of the action
	 */
	public String toString();
	
}