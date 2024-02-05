package pandemic.game.listchooser;

import java.util.List;

/**
 * interface for ListChooser - the way of choosinf from a list -
 */
public interface ListChooser<T> {

	/**
	 * Allows one to choose an item from a list of items of type T. 
	 * 
	 * @param msg The asked question.
	 * @param list The list of items of type T from which one must choose one.
	 * @return The chosen item.
	 */
	public T choose(String msg, List<? extends T> list);
} 
