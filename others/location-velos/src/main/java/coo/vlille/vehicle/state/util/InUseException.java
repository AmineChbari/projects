package coo.vlille.vehicle.state.util;

/**
 * A specific type of VehiculeException thrown when the vehicule is in use and someone tries to take it from a station
 * It extends VehiculeException
 */
public class InUseException extends VehicleException {
    /**
     * The constructor of InUseException
     * calls the superclasse constructor
     */
    public InUseException() {
        super("InUseException");
    }
}