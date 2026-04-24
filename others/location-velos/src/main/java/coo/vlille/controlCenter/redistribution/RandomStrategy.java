package coo.vlille.controlCenter.redistribution;

import coo.vlille.station.Station;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.VehicleException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The RandomStrategy class implements a redistribution strategy for vehicles
 * among a list of stations using a random approach. It ensures that vehicles
 * are redistributed randomly
 */
public class RandomStrategy implements RedistributionStrategy {
    private Random random = new Random();

    /**
     * Collects all vehicles from the provided list of stations.
     * 
     * @param stations the list of stations to collect vehicles from
     * @return a list of all vehicles gathered from the stations
     */
    private List<Vehicle> collectAllVehicles(List<Station> stations) {
        List<Vehicle> allVehicles = new ArrayList<>();
        for (Station station : stations) {
            allVehicles.addAll(station.getAllVehicles());
            station.clearVehicles(); // Clear vehicles after collection
        }
        return allVehicles;
    }

    /**
     * Shuffles the list of vehicles randomly.
     * 
     * @param vehicles the list of vehicles to shuffle
     */
    private void shuffleVehicles(List<Vehicle> vehicles) {
        Collections.shuffle(vehicles, random);
    }

    /**
     * Distributes the shuffled vehicles randomly across the stations.
     * 
     * @param vehicles the list of shuffled vehicles
     * @param stations the list of stations to distribute vehicles to
     * @throws VehicleException 
     */
    private void distributeVehicles(List<Vehicle> vehicles, List<Station> stations) throws VehicleException {
        int numStations = stations.size();
        for (Vehicle vehicle : vehicles) {
            // Try each station starting from a random one until one accepts the vehicle.
            int start = random.nextInt(numStations);
            for (int i = 0; i < numStations; i++) {
                Station target = stations.get((start + i) % numStations);
                if (target.putForRedistribution(vehicle)) break;
            }
        }
    }

    /**
     * Redistributes vehicles among stations by collecting, shuffling, and
     * redistributing them in a random order.
     *
     * @param stations the list of stations to redistribute vehicles to
     */
    @Override
    public void redistribute(List<Station> stations) throws VehicleException {
        List<Vehicle> allVehicles = collectAllVehicles(stations); // Step 1: Collect
        shuffleVehicles(allVehicles);                              // Step 2: Shuffle
        distributeVehicles(allVehicles, stations);                 // Step 3: Distribute
    }
}
