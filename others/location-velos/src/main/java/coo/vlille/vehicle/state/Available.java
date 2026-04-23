package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.AvailableException;


public class Available implements VehicleState {
    
    /**
     * take the vehicule from the station
     * 
     * @param vehicle the vehicle to handle
     */
    @Override
    public void take(Vehicle vehicle)  {
        vehicle.setState(new InUse());  // State transition to InUse
    }

    /**
     * put the vehicule in the station
     * 
     * @param vehicle the vehicle to handle
     * @throws AvailableException the vehicule is in already in sation
     */
    @Override
    public void put(Vehicle vehicle) throws AvailableException {
        // error : Already in Station
        throw new AvailableException();
    }

    /**
     * Return a String representation of the state
     * @return the new state
     */
    @Override
    public String toString() {
        return "Available";
    }   
}
