package coo.vlille.controlCenter;

import coo.vlille.station.Station;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.VehicleException;

/**
 * Interface representing the mediator for the vlille network.
 * indeed
 */
public interface Observer {
    /**
     * Registers a station in the system.
     * 
     * @param station the station to register
     */
    public void registerStation(Station station);

    /**
     * Finds a station by its ID.
     * 
     * @param id the ID of the station
     * @return the station if found, or null otherwise
     */
    public Station findStationById(int id);

    /**
     * Notifies that a vehicle has been taken.
     * 
     * @param vehicle the vehicle that was taken
     */
    public void notifyVehicleTaken(Vehicle vehicle);

    /**
     * Notifies that a vehicle has been returned.
     * 
     * @param vehicle the vehicle that was returned
     */
    public void notifyVehicleReturned(Vehicle vehicle);

    public void redistribute() throws VehicleException;

    public void checkAllRedistribute() throws VehicleException;

}
