package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.InUseException;

/**
 * Class that represents "In Use" state so the vehicle can not be used
 * It implements the VehiculeState interface
 */
public class InUse implements VehicleState  {
    
    /**
     * Remove the vehicule from the station (not supposed to happen in this case)
     * 
     * @param vehicle the vehicle to handle
     * @throws InUseException the vehicule is in use and can't be removed
     */
    @Override
    public void take(Vehicle vehicle) throws InUseException {
        // error : the vehicle is already in use
        throw new InUseException();
    }

    /**
     * Put the vehicule in the station
     * 
     * @param vehicle the vehicle to handle
     */
    @Override
    public void put(Vehicle vehicle) {
        vehicle.setState(new Available());  // State transition to Available
    }

    /**
     * Return a String of the state
     * @return the new state
     */
    @Override
    public String toString() {
        return "In Use";
    }
}
