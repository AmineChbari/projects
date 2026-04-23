package coo.vlille.vehicle.state.util;

/**
 * this Exception is thrown when 
 * the vehicule have been already put in a station and someone tries to do it again
 * 
 * It extends VehiculeException
 */
public class AvailableException extends VehicleException {
    
    /**
     * The constructor of the FreeException
     * calls the superclasse constructor
     */
    public AvailableException(){
        super("AvailableException");
    }
}
