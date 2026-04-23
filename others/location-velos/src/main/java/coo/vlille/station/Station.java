package coo.vlille.station;

import java.util.ArrayList;
import java.util.List;

import coo.vlille.controlCenter.ControlCenter;
import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.Broken;
import coo.vlille.vehicle.state.Stolen;
import coo.vlille.vehicle.state.util.VehicleException;

/**
 * Class representing a bike station in the bike-sharing system.
 * A station can hold a certain number of vehicles, such as bikes or electric bikes.
 */
public class Station {
    /** Unique identifier of the station */
    private int id;
    /** Maximum capacity of the station (number of vehicle slots) */
    private int capacity;
    /** List of vehicles currently in the station */
    private List<Vehicle> vehicles;
    /** Observer that handles distributing the vehicles when needed */
    private ControlCenter center;
    /** Number of seconds after which a vehicle is considered stolen */
    public static final int NB_SECONDS_STOLEN = 6;

    /**
     * Constructor for a Station.
     * 
     * @param id the unique identifier for the station
     * @param capacity the maximum number of vehicles the station can hold
     * @param center the control center that manages the station
     */
    public Station(int id, int capacity, ControlCenter center) {
        this.id = id;
        this.capacity = capacity;
        this.vehicles = new ArrayList<>();
        this.center = center;
    }

    /**
     * Checks for each vehicle in the station if its state is Broken.
     * If a vehicle is broken, it calls the repair method on that vehicle.
     */
    public void checkReparation(ConcreteRepairer repairer) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getState() instanceof Broken) {
                vehicle.repair(repairer);
            }
        }
    }

    /**
     * Checks for each vehicle in the station if its state is Stolen.
     * If a vehicle is Stolen, it can't be used again.
     */
    public void checkStolen() {
        if (vehicles.size() == 1) {
            Vehicle vehicle = vehicles.get(0);
            vehicle.setState(new Stolen());
            System.out.println("Vehicle " + vehicle.getId() + " is stolen and cannot be used.");
        }
    }

    /**
     * Gets the station id.
     * 
     * @return the station id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the station's capacity.
     * 
     * @return the station capacity
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Gets all the vehicles inside this station.
     * 
     * @return the list of all the vehicles
     */
    public List<Vehicle> getAllVehicles() {
        return this.vehicles;
    }

    /**
     * Finds and returns a vehicle by its ID.
     * 
     * @param id the vehicle's ID
     * @return the vehicle with the specified ID, or null if not found
     */
    public Vehicle getVehicleById(int id) {
        for (Vehicle v : vehicles) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    /**
     * Checks if the station is full.
     * 
     * @return true if the station is full, false otherwise
     */
    public boolean isFull() {
        return vehicles.size() == capacity;
    }

    /**
     * Checks if the station is empty.
     * 
     * @return true if the station is empty, false otherwise
     */
    public boolean isEmpty() {
        return vehicles.isEmpty();
    }

    /**
     * Adds a vehicle to the station if there is enough space.
     * 
     * @param v the vehicle to be deposited
     * @throws VehicleException if the vehicle cannot be put in the station
     */
    public void put(Vehicle v) throws VehicleException {
        if (isFull()) {
            System.out.println("Station " + id + " is full. Cannot add vehicle " + v.getId());
            return;
        }
        v.put();
        this.vehicles.add(v);
        this.center.notifyVehicleReturned(v);
        System.out.println("Vehicle " + v.getId() + " added to Station " + id);
    }

    /**
     * Removes a specific vehicle from the station.
     * 
     * @param v the vehicle to remove
     * @throws VehicleException if the vehicle cannot be taken from the station
     */
    public void take(Vehicle v) throws VehicleException {
        if (this.vehicles.remove(v)) {
            this.center.notifyVehicleTaken(v);
            System.out.println("Vehicle " + v.getId() + " removed from Station " + id);
            v.take();
        } else {
            System.out.println("Vehicle " + v.getId() + " not found in Station " + id);
        }
    }

    /**
     * Clears all vehicles from the station.
     */
    public void clearVehicles() {
        this.vehicles.clear();
        System.out.println("All vehicles have been cleared from Station " + id);
    }

    /**
     * Gets the total number of vehicles in the station.
     * 
     * @return the total number of vehicles
     */
    public int totalBikes() {
        return this.vehicles.size();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("~~~ Station " + this.id + " ~~~\n");
        result.append("Capacity: " + this.capacity + " | Vehicles: ");
        
        if (vehicles.isEmpty()) {
            result.append("No vehicles 🚫");
        } else {
            int i = 0;
            for (Vehicle vehicle : vehicles) {
                result.append("\n - " + vehicle);
                i++;
            }
            if (i < this.capacity) {
                for (int j = i; j < this.capacity; j++) {
                    result.append("\n -   ⛔ EMPTY SLOT");
                } 
            }
        }
        result.append("\n~~~~~~~~~~~~~~~~~~~~~~~\n");
        return result.toString();
    }
}