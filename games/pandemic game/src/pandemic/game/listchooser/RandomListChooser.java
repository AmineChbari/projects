package pandemic.game.listchooser;

import java.util.List;
import java.util.Random;

/**
 * class responsible for choosing randomly from a list
 * @param <T> the type of items in the list
 */
public class RandomListChooser<T> implements ListChooser<T> {
	/** the random method to apply */
	private Random random = new Random();	
	
	/** default constructor
	 * 
	 */
	public RandomListChooser() {
	}
	
	/**
	 * Allows one to choose a random item from a list of items of type T.
	 * If the list of items is empty, null is returned.
	 * The list of items is presented as numbers followed by the 
	 * string representation of the item. 
	 * The possibility not to make a choice is automatically added (choice number 0), 
	 * in this case, null is returned.
	 * 
	 * @param msg The asked question.
	 * @param list The list of items of type T from which one must choose one.
	 * @return The chosen item. null if the list of items is empty or if no choice has been made
	 */
	public T choose(String msg, List<? extends T> list) {
		if (list.isEmpty()) {
			return null;
		}
		System.out.println(msg);
		System.out.print("[");
		int index = 0;
		String separator = " ";
		for (T element : list) {
			System.out.print(separator + (index++) + " - " + element);
			separator = " , ";
		}
		System.out.println(" ]");
		return list.get(random.nextInt(list.size()));
	}

}
