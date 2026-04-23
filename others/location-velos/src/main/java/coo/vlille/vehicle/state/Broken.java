package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.BrokenException;

public class Broken implements VehicleState{
    
    /**
     * This methode is used to take the vehicule from the station 
     * 
     * @param vehicle the vehicle to handle 
     * @throws BrokenException
     */
    @Override
    public void take(Vehicle vehicle) throws BrokenException {
        // error : Vehicle is broken
        throw new BrokenException();
    }

    /**
     * This methode is used to put the vehicule in the station 
     * 
     * @param vehicle the vehicle to handle
     * @throws BrokenException
     */
    @Override
    public void put(Vehicle vehicle) throws BrokenException{
        // error : Vehicle is broken
        throw new BrokenException();
    }

    /**
     * Return a String of the state
     * @return the new state
     */
    @Override
    public String toString() {
        return "Broken";
    } 
}