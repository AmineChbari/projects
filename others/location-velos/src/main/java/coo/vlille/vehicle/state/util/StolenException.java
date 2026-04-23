package coo.vlille.vehicle.state.util;

/**
 * A specific type of VehiculeException thrown when the vehicule was being stolen
 * It extends VehiculeException
 */
public class StolenException extends VehicleException {
    /**
     * The constructor of theStolenException
     * calls the superclasse constructor
     */
    public StolenException() {
        super("StolenException");
    }
}
