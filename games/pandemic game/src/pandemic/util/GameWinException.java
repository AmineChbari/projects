package pandemic.util;
/**
 * An exception thrown when the game is won.
 * This exception can be used to signal a victory condition to the calling code.
 */
public class GameWinException extends Exception {
	/**
     * Constructs a new GameWinException with the specified detail message.
     * @param message the detail message
     */
	public GameWinException(String message) {
		super(message);
	}
}
