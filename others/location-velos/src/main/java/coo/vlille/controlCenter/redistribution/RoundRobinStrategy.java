package coo.vlille.controlCenter.redistribution;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import coo.vlille.station.Station;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.VehicleException;

/**
 * The RoundRobinStrategy class implements a redistribution strategy for vehicles
 * among a list of stations using a round-robin approach. It ensures that vehicles
 * are redistributed while avoiding overfilling stations and keeping some stations 
 * from being empty.
 */
public class RoundRobinStrategy implements RedistributionStrategy {
    private Queue<Vehicle> elementsToHandle;

    /**
     * Redistributes vehicles among the given stations using the round-robin strategy.
     * This method collects all vehicles from the stations, clears them, and then 
     * redistributes them based on their current capacities.
     *
     * @param stations the list of stations to redistribute vehicles among
     */
    @Override
    public void redistribute(List<Station> stations) throws VehicleException {
        collectAllElements(stations);
        clearAllStations(stations);
        distributeVehicles(stations);
        balanceStationLevels(stations, 0.3, 0.8); // Minimum and maximum occupancy thresholds
    }

    /**
     * Collects all vehicles from the specified stations into a queue for redistribution.
     *
     * @param stations the list of stations to collect vehicles from
     */
    private void collectAllElements(List<Station> stations) {
        elementsToHandle = new LinkedList<>();
        for (Station st : stations) {
            elementsToHandle.addAll(st.getAllVehicles());
        }
    }

    /**
     * Clears all vehicles from each station in the provided list.
     *
     * @param stations the list of stations to clear
     */
    private void clearAllStations(List<Station> stations) {
        for (Station st : stations) {
            st.clearVehicles(); // Assume this method clears the vehicles from a station
        }
    }

    /**
     * Distributes vehicles among the stations in a round-robin manner.
     * Vehicles are added only if the station is not full.
     *
     * @param stations the list of stations to distribute vehicles to
     * @throws VehicleException 
     */
    private void distributeVehicles(List<Station> stations) throws VehicleException {
        int stationIndex = 0;
        int numStations = stations.size();
        // First pass: respect the 80% soft cap, but stop when no station accepted during a full round.
        int sinceLastPlace = 0;
        while (!elementsToHandle.isEmpty() && sinceLastPlace < numStations) {
            Station currentStation = stations.get(stationIndex);
            if (canAddVehicle(currentStation) && currentStation.putForRedistribution(elementsToHandle.peek())) {
                elementsToHandle.poll();
                sinceLastPlace = 0;
            } else {
                sinceLastPlace++;
            }
            stationIndex = (stationIndex + 1) % numStations;
        }
        // Second pass: if vehicles remain, ignore the 80% cap and just fill any non-full station.
        sinceLastPlace = 0;
        while (!elementsToHandle.isEmpty() && sinceLastPlace < numStations) {
            Station currentStation = stations.get(stationIndex);
            if (currentStation.putForRedistribution(elementsToHandle.peek())) {
                elementsToHandle.poll();
                sinceLastPlace = 0;
            } else {
                sinceLastPlace++;
            }
            stationIndex = (stationIndex + 1) % numStations;
        }
    }

    /**
     * Checks if a vehicle can be added to the given station without exceeding its maximum capacity.
     * 
     * @param station the station to check
     * @return true if a vehicle can be added, false otherwise
     */
    private boolean canAddVehicle(Station station) {
        int maxCapacity = station.getCapacity();
        int currentVehicleCount = station.totalBikes();
        double currentOccupancy = (double) currentVehicleCount / maxCapacity;

        return currentOccupancy < 0.8; // Less than 80% of maximum capacity
    }

    /**
     * Balances the levels of vehicles in the stations by adding vehicles to stations
     * that are below the minimum occupancy threshold.
     *
     * @param stations the list of stations to balance
     * @param minThreshold the minimum occupancy threshold
     * @param maxThreshold the maximum occupancy threshold
     */
    private void balanceStationLevels(List<Station> stations, double minThreshold, double maxThreshold) throws VehicleException {
        for (Station st : stations) {
            int maxCapacity = st.getCapacity();
            int currentVehicleCount = st.totalBikes();
            double currentOccupancy = (double) currentVehicleCount / maxCapacity;

            // Add vehicles if the station is below the minimum threshold
            while (currentOccupancy < minThreshold && !elementsToHandle.isEmpty()) {
                addVehicleToStation(st);
                currentVehicleCount = st.totalBikes();
                currentOccupancy = (double) currentVehicleCount / maxCapacity;
            }
        }
    }

    /**
     * Adds a vehicle to the specified station and logs the current occupancy.
     *
     * @param station the station to add a vehicle to
          * @throws VehicleException 
          */
         private void addVehicleToStation(Station station) throws VehicleException {
        Vehicle vehicle = elementsToHandle.peek();
        if (vehicle != null && station.putForRedistribution(vehicle)) {
            elementsToHandle.poll();
            System.out.println("Added a vehicle to the station. Current occupancy: " +
                ((double) station.totalBikes() / station.getCapacity()) * 100 + "%");
        }
    }
}
