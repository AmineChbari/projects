package coo.vlille.controlCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coo.vlille.controlCenter.redistribution.RedistributionStrategy;
import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.station.Station;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.VehicleException;

/**
 * A class representing the control center of the vlille network
 * implementing the Mediator pattern.
 */
public class ControlCenter implements Observer {

    /** Map of all the stations in the network, keyed by their IDs */
    private Map<Integer, Station> stations;

    /** List of all vehicles in use in the network
     * These are the vehicles not attached to any station, they are currently in use by users.
     */
    private List<Vehicle> vehiclesInUse;

    /** Redistribution strategy for managing vehicles across stations */
    private RedistributionStrategy redistributionStrategy;

    /**
     * Constructor for the control center
     */
    public ControlCenter() {
        this.stations = new HashMap<>();
        this.vehiclesInUse = new ArrayList<>();
    }

    /**
     * Registers a station with the control center.
     * @param station the station to add
     */
    @Override
    public void registerStation(Station station) {
        if (stations.containsKey(station.getId())) {
            System.out.println("Station with ID " + station.getId() + " is already registered.");
            return;
        }
        stations.put(station.getId(), station);
        System.out.println("Station " + station.getId() + " registered");
    }

    /**
     * Gets the list of vehicles currently in use.
     * @return the list of vehicles in use
     */
    public List<Vehicle> getVehiclesInUse() {
        return vehiclesInUse;
    }

    /**
     * Gets the map of stations registered with the control center.
     * @return the map of stations
     */
    public Map<Integer, Station> getStations() {
        return stations;
    }

    /**
     * Removes a station by its ID.
     * @param stationId the ID of the station to remove
     */
    public void removeStation(int stationId) {
        Station station = stations.get(stationId);
        if (station != null) {
            stations.remove(stationId);
            System.out.println("Station " + stationId + " removed and monitoring stopped.");
        } else {
            System.out.println("Station with ID " + stationId + " not found.");
        }
    }

    /**
     * Finds a station by its ID.
     * @param id the ID of the station
     * @return the station if found, null otherwise
     */
    @Override
    public Station findStationById(int id) {
        return stations.get(id);
    }

    /**
     * Notifies the control center that a vehicle has been taken.
     * @param vehicle the vehicle that was taken
     */
    @Override
    public void notifyVehicleTaken(Vehicle vehicle) {
        this.vehiclesInUse.add(vehicle);
    }

    /**
     * Notifies the control center that a vehicle has been returned.
     * @param vehicle the vehicle that was returned
     */
    @Override
    public void notifyVehicleReturned(Vehicle vehicle) {
        this.vehiclesInUse.remove(vehicle);
    }

    /**
     * Sets the approach to redistribute the vehicles.
     * @param strategy the redistribution strategy
     */
    public void setRedistributionStrategy(RedistributionStrategy strategy) {
        this.redistributionStrategy = strategy;
    }

    /**
     * Executes the redistribution strategy.
     */
    @Override
    public void redistribute() throws VehicleException {
        if (redistributionStrategy == null) {
            System.out.println("Redistribution strategy is not set.");
            return;
        }
        redistributionStrategy.redistribute(new ArrayList<>(stations.values()));
    }

    /**
     * Checks all stations and redistributes vehicles if necessary.
     */
    @Override
    public void checkAllRedistribute() throws VehicleException {
        List<Station> allStations = new ArrayList<>(stations.values());
        for (Station station : allStations) {
            if (station.isFull() || station.isEmpty()) {
                System.out.println("Redistributing vehicles for station: " + station.getId());
                this.redistribute();
            }
        }
    }

    /**
     * Checks all stations and repairs vehicles if necessary.
     * @param repairer the repairer to use for repairs
     */
    public void checkAllRepairs(ConcreteRepairer repairer) {
        List<Station> allStations = new ArrayList<>(stations.values());
        for (Station station : allStations) {
            station.checkReparation(repairer);
        }
    }

    /**
     * Checks all stations and marks vehicles as stolen if necessary.
     */
    public void checkAllStolen() {
        List<Station> allStations = new ArrayList<>(stations.values());
        for (Station station : allStations) {
            station.checkStolen();
        }
    }
    
    /**
     * Returns a string representation of the control center, including all registered stations
     * and vehicles currently in use.
     *
     * @return A string representation of the control center.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\n<<------------------------- vlille ------------------------->>\n");
        for (Map.Entry<Integer, Station> entry : stations.entrySet()) {
            result.append(entry.getValue());
        }
        result.append("VEHICLES OUT OF STATIONS (IN SERVICE):\n");
        if (this.vehiclesInUse.isEmpty()) {
            result.append("NONE ⛔");
        } else {
            for (Vehicle vehicle : this.vehiclesInUse) {
                result.append("\n - " + vehicle);
            }
        }
        result.append("\n<<---------------------------------------------------------->>\n");
        return result.toString();
    }
}