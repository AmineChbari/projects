package coo.vlille.vehicle.state.util;

/**
 * this Exception is thrown when 
 * the vehicule have been put for more than 3 times 
 * or not been used in a while
 * 
 * It extends VehiculeException
 */
public class BrokenException extends VehicleException{
    
    /**
     * The constructor of the BrokenException
     * calls the superclasse constructor
     */
    public BrokenException() {
        super("BrokenException");
    }

}