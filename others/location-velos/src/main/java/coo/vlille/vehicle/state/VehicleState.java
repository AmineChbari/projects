package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.*;

/**
 * VehiculeState is an interface that represents the state of a vehicle.
 */
public interface VehicleState {
    
    /**
     * take the vehicle
     * 
     * @param vehicle the vehicle to handle
     * @throws VehiculeException If the vehicle cannot be taken.
     */
    void take(Vehicle vehicle) throws VehicleException;
    
    /**
     * put back the vehicle
     * 
     * @param vehicle the vehicle to handle
     * @throws VehiculeException If the vehicle cannot be put.
     */
    void put(Vehicle vehicle) throws VehicleException;

    /**
     * Returns a string representation of the vehicle state.
     * @return A string representation of the vehicle state.
     */
    String toString();
}