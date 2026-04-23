package coo.vlille.vehicle.state.util;

/**
 * Representing a specefic type of Exception
 * It extends VehiculeException
 */
public class VehicleException extends Exception {
    /**
     * The constructor of VehiculeException
     * calls the superclasse constructor
     * @param message the message to be printed if the exception was thrown
     */
    public VehicleException(String message) {
        super(message);
    }
}