package pandemic.util;
/**
 * An exception thrown when the game is over.
 * This exception can be used to signal the end of the game to the calling code.
 */
public class GameOverException extends Exception {
	 /**
     * Constructs a new GameOverException with the specified detail message.
     * @param message the detail message
     */
	public GameOverException(String message) {
		super(message);
	}
}
