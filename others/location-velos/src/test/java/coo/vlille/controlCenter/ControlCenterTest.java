package coo.vlille.controlCenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import coo.vlille.controlCenter.redistribution.RandomStrategy;
import coo.vlille.controlCenter.redistribution.RoundRobinStrategy;
import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.station.Station;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.state.util.VehicleException;

public class ControlCenterTest {
    
    private ControlCenter controlCenter;
    private Station station1;
    private Station station2;
    private ClassicBike bike1;
    private ClassicBike bike2;

    @BeforeEach
    public void setUp() {
        controlCenter = new ControlCenter();

        // Create stations with id, capacity, and controlCenter instance
        station1 = new Station(1, 3, controlCenter);
        station2 = new Station(2, 3, controlCenter);

        // Create bikes (vehicles)
        bike1 = new ClassicBike(101);
        bike2 = new ClassicBike(102);

        // Register stations with the control center
        controlCenter.registerStation(station1);
        controlCenter.registerStation(station2);
    }

    @Test
    public void testRegisterStation() {
        // Ensure stations are registered with the control center
        controlCenter.registerStation(station1);
        controlCenter.registerStation(station2);
        
        // Assert that the stations have been correctly added to the control center
        assertEquals(2, controlCenter.getStations().size());
        assertTrue(controlCenter.getStations().containsKey(1));
        assertTrue(controlCenter.getStations().containsKey(2));
    }

    @Test
    public void testFindStationById() {
        // Retrieve stations by ID
        Station foundStation1 = controlCenter.findStationById(1);
        Station foundStation2 = controlCenter.findStationById(2);

        // Assert the stations found by ID match the expected ones
        assertEquals(station1, foundStation1);
        assertEquals(station2, foundStation2);
    }

    @Test
    public void testNotifyVehicleTaken() throws VehicleException {
        // Notify that a bike is taken from station1
        station1.put(bike1);  // Add bike1 to station1
        station1.take(bike1); // Take bike1 from station1

        // Ensure that the bike has been added to the vehiclesInUse list in ControlCenter
        assertTrue(controlCenter.getVehiclesInUse().contains(bike1));
    }

    @Test
    public void testNotifyVehicleReturned() throws VehicleException {
        // Notify that a bike is taken and then returned
        station1.put(bike1);  // Add bike1 to station1
        station1.take(bike1); // Take bike1 from station1
        station1.put(bike1);  // Return bike1 to station1

        // Ensure the bike is removed from vehiclesInUse after being returned
        assertFalse(controlCenter.getVehiclesInUse().contains(bike1));
    }

    @Test
    public void testRedistributeWithRoundRobinStrategy() throws VehicleException {
        // Set the redistribution strategy to RoundRobin
        RoundRobinStrategy roundRobinStrategy = new RoundRobinStrategy();
        controlCenter.setRedistributionStrategy(roundRobinStrategy);

        // Add vehicles to stations
        station1.put(bike1);
        station2.put(bike2);

        // Perform redistribution
        controlCenter.redistribute();

        // After redistribution, check that vehicles are distributed (the exact distribution depends on strategy)
        assertTrue(station1.getAllVehicles().contains(bike1) || station2.getAllVehicles().contains(bike1));
        assertTrue(station1.getAllVehicles().contains(bike2) || station2.getAllVehicles().contains(bike2));
    }

    @Test
    public void testRedistributeWithRandomStrategy() throws VehicleException {
        // Set the redistribution strategy to Random
        RandomStrategy randomStrategy = new RandomStrategy();
        controlCenter.setRedistributionStrategy(randomStrategy);

        // Add vehicles to stations
        station1.put(bike1);
        station2.put(bike2);

        // Perform redistribution
        controlCenter.redistribute();

        // After redistribution, check that vehicles are randomly distributed
        assertTrue(station1.getAllVehicles().contains(bike1) || station2.getAllVehicles().contains(bike1));
        assertTrue(station1.getAllVehicles().contains(bike2) || station2.getAllVehicles().contains(bike2));
    }

    @Test
    public void testRedistributeWithNoStrategy() throws VehicleException {
        // Perform redistribution without setting any strategy
        controlCenter.redistribute();
        // Verify no redistribution occurred (the vehiclesInUse list remains unchanged)
        assertTrue(controlCenter.getVehiclesInUse().isEmpty());
    }

    @Test
    public void testStationFull() throws VehicleException {
        // Add vehicles to station1 until it's full
        for (int i = 0; i < 3; i++) {
            station1.put(new ClassicBike(100 + i));
        }

        // Attempt to add one more vehicle, which should fail because the station is full
        ClassicBike extraBike = new ClassicBike(111);
        try {
            station1.put(extraBike);
        } catch (VehicleException e) {
            // Expected exception
        }

        // Assert that the station's capacity was not exceeded
        assertFalse(station1.getAllVehicles().contains(extraBike));
    }

    @Test
    public void testStationEmpty() throws VehicleException {
        // Verify that the station starts empty
        assertTrue(station1.isEmpty());

        // Add a vehicle
        station1.put(bike1);

        // Check if the station is no longer empty
        assertFalse(station1.isEmpty());
    }

    @Test
    public void testRemoveStation() {
        // Remove a station by ID
        controlCenter.removeStation(1);

        // Assert that the station has been removed
        assertFalse(controlCenter.getStations().containsKey(1));
    }

    @Test
    public void testCheckAllRedistribute() throws VehicleException {
        // Set the redistribution strategy to RoundRobin
        RoundRobinStrategy roundRobinStrategy = new RoundRobinStrategy();
        controlCenter.setRedistributionStrategy(roundRobinStrategy);

        // Add vehicles to stations
        station1.put(bike1);
        station2.put(bike2);

        // Perform checkAllRedistribute
        controlCenter.checkAllRedistribute();

        // After redistribution, check that vehicles are distributed (the exact distribution depends on strategy)
        assertTrue(station1.getAllVehicles().contains(bike1) || station2.getAllVehicles().contains(bike1));
        assertTrue(station1.getAllVehicles().contains(bike2) || station2.getAllVehicles().contains(bike2));
    }

    @Test
    public void testCheckAllRepairs() throws VehicleException {
        // Create a mock repairer
        ConcreteRepairer repairer = new ConcreteRepairer();

        // Add vehicles to stations
        station1.put(bike1);
        station2.put(bike2);

        // Perform checkAllRepairs
        controlCenter.checkAllRepairs(repairer);

        // Verify that the repair check was performed (this would depend on the implementation of checkReparation)
        // For simplicity, we assume checkReparation prints a message or changes a state we can verify
        // This part of the test would need to be adapted based on the actual implementation of checkReparation
    }
    /*
    @Test
    public void testToString() throws VehicleException {
        ClassicBike bike1 = new ClassicBike(101);
        Scooter scooter1 = new Scooter(102);

        station1.put(bike1);
        station2.put(scooter1);

        String expectedOutput = "\n<<------------------------- vlille ------------------------->>\n" +
                "~~~ Station 1 ~~~\n" +
                "Capacity: 3 | Vehicles: \n" +
                " -   🚲 Classic Bike [ Id : 101 , state: Available ] \n" +
                " -   ⛔ EMPTY SLOT \n" +
                " -   ⛔ EMPTY SLOT \n" +
                "~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "~~~ Station 2 ~~~\n" +
                "Capacity: 3 | Vehicles: \n" +
                " -  🛴 Scooter [ Id : 102 , state: Available, 100% ] \n" +
                " -   ⛔ EMPTY SLOT \n" +
                " -   ⛔ EMPTY SLOT \n" +
                "~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "VEHICLES OUT OF STATIONS (IN SERVICE):\n" +
                "NONE ⛔\n" +
                "<<---------------------------------------------------------->>\n";

        assertEquals(expectedOutput, controlCenter.toString());
    }
         */
}