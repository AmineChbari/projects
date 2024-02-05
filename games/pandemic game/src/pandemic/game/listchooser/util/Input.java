package pandemic.game.listchooser.util;


import java.util.Scanner;

/**
 * Une classe utilitaire pour la saisie de cha�nes ou d'entiers sur l'entr�e
 * standard.
 */

public class Input {
	private static Scanner scanner = new Scanner(System.in);

	
	/** default constructor
	 * 
	 */
	public Input() {
	}
	
	/**
	 * permet la saisie d'une chaîne sur l'entrée standard
	 * 
	 * @return la chaîne saisie
	 */
	public static String readString() {
		return Input.scanner.next();
	}

	/**
	 * permet la saisie d'un entier sur l'entrée standard
	 * 
	 * @throws java.io.IOException if an error occurs while reading the input
	 * @return l'entier saisi
	 */
	public static int readInt() throws java.io.IOException {
		try {
			return Input.scanner.nextInt();
		} catch (Exception e) {
			Input.scanner.skip(".*");
			throw new java.io.IOException();
		}
	}	
	
	// pour le test
	//public static void main(String[] args) {
		//try {
		//	System.out.print(" chaine : ? ");
		//	String chaineLue = Input.readString();
		//	System.out.println("lue  => " + chaineLue);
		//	System.out.print(" int : ? ");
		//	int intLu = Input.readInt();
		//	System.out.println("lue  => " + intLu);
		//} catch (java.io.IOException e) {
		//}
	//} pas besoin du main pour la suite.
} // Input
