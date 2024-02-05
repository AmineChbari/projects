package pandemic.player.Action;

import pandemic.board.Board;

/**
 * Class representing Not doing any Action
 */
public class NoneAction implements Action {

	/** The board that will modified */
	private Board board;

	/**
	 * Constructor of special Moving Action
	 * 
	 * @param board the board
	 */
	public NoneAction(Board board) {
		this.board = board;
	}

	/**
	 * The action of doing nothing (player can skip an action)
	 */
	@Override
	public int doSomething() {return 1;}
	
	/**
	 * represents the Action of doing nothing
	 * @return String representation of doing nothing 
	 */
	public String toString() {
		return "NoneAction";
	}
}

	
