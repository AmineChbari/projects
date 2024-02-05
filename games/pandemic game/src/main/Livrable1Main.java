package main;
import pandemic.disease.Disease;
import pandemic.util.GameOverException;
import pandemic.board.*;


/**
 * the first main class
 *
 * @author pandéquatre
 */
public class Livrable1Main {
	
	/**
	 * default constructor
	 */
	public Livrable1Main(){
	}

    /**
    * the main methods
    * @param args the link of the JSON
    */
    public static void main(String[] args) {

		String mapFilename="";
		if (args.length == 0) mapFilename= "Maps/miniMap.json"; // Load the map from the default mini Map json file
		else if (args.length > 0) mapFilename = args[0];  // Load the map from the JSON file argument
        Board myboard =new Board(mapFilename);

//todo

        //Print the initial state of the cities
        System.out.println("step1: Initial state of the cities:");
        myboard.setMapCities();
        myboard.setNeighborsCities();
        myboard.InitCityDiseases();
        myboard.setDisease();
        System.out.println(myboard);

        // Infect some cities
         System.out.println("\n\n step 2 :Infecting cities...\n");

        // InfectCity(City,Disease) pas de String, a la place de ville-i on fait this.mapCities.get(i);
        try {
			myboard.InfectCity(myboard.getMapCities().get(1),Disease.JAUNE);
			myboard.InfectCity(myboard.getMapCities().get(3),Disease.NOIR);
	        myboard.InfectCity(myboard.getMapCities().get(6),Disease.BLEU);
	        myboard.InfectCity(myboard.getMapCities().get(7),Disease.JAUNE);
	        myboard.InfectCity(myboard.getMapCities().get(4),Disease.BLEU);
	        myboard.InfectCity(myboard.getMapCities().get(5),Disease.NOIR);
		} catch (GameOverException e) {
			e.printStackTrace();
		}
        


        // // Print the state of the cities after the infections
         System.out.println("\n Step 3: State of the cities after the infections:\n\n");
         System.out.println(myboard);

        // // Trigger an outbreak
         System.out.println("\nStep4: Triggering outbreak in ville-7 infect+2 \n");
         try {
			myboard.InfectCity(myboard.getMapCities().get(6),Disease.BLEU);
		} catch (GameOverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         System.out.println(myboard);
         try {
			myboard.InfectCity(myboard.getMapCities().get(6),Disease.BLEU);
		} catch (GameOverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         System.out.println(myboard);
         System.out.println("Step4 :outbreak\n");    //ville 7=3cube
         try {
			myboard.InfectCity(myboard.getMapCities().get(6),Disease.BLEU);
		} catch (GameOverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         // Print the state of the cities after the outbreak
         System.out.println("Step5 :State of the cities after the outbreak:\n\n");
         System.out.println(myboard);
         System.out.println("Step6 :State of the cities after the 2nd outbreak by infect 3 more time ville-7:\n\n");
        
         try {
        	 myboard.InfectCity(myboard.getMapCities().get(6),Disease.BLEU);
             myboard.InfectCity(myboard.getMapCities().get(6),Disease.BLEU);
			myboard.InfectCity(myboard.getMapCities().get(6),Disease.BLEU);
		} catch (GameOverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         System.out.println(myboard);





        /*
         * les voisins de "ville-6":
         *  "ville-6": ["ville-12", "ville-5", "ville-1", "ville-47"]"
         */
        //myboard.getNeighborsForACity(myboard.getMapCities().get(6)).forEach(city -> System.out.println(myboard));}
    }
}
