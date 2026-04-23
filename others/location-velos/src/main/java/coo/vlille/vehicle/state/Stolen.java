package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.StolenException;

public class Stolen implements VehicleState{
    
    /**
     * This methode is used to take the vehicule from the station 
     * 
     * @param vehicle the vehicle to handle
     * @throws StolenException
     */
    @Override
    public void take(Vehicle vehicle) throws StolenException {
        // error : Vehicle is stolen
        throw new StolenException();
    }

    /**
     * This methode is used to put the vehicule in the station 
     * 
     * @param vehicle the vehicle to handle 
     * @throws StolenException
     */
    @Override
    public void put(Vehicle vehicle) throws StolenException{
        // error : Vehicle is stolen
        throw new StolenException();
    }

    /**
     * Return a String of the state
     * @return the new state
     */
    @Override
    public String toString() {
        return "Stolen";
    } 
}
