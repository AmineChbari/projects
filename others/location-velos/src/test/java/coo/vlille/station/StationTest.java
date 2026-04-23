package coo.vlille.station;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import coo.vlille.controlCenter.ControlCenter;
import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.state.Broken;
import coo.vlille.vehicle.state.util.VehicleException;

/**
     * Test class for the Station class.
     * This class tests the functionalities of the Station class
     * by verifying the addition, withdrawal, and management of vehicles.
     */
    public class StationTest {

        private Station station;
        private Vehicle myVehicle;
        private ControlCenter controlCenter;

        // Stream to capture the output of System.out.println
        private ByteArrayOutputStream outputStream;

        /**
         * Initializes a station, a vehicle, and a control center before each test.
         * This is executed before each test method.
         */
        @BeforeEach
        public void setUp() {
            // Initialize the control center
            controlCenter = new ControlCenter();

            // Initialize a station with a capacity of 2 and link it to the control center
            station = new Station(1, 2, controlCenter);

            // Initialize a ClassicBike for testing
            myVehicle = new ClassicBike(1);

            // Set up the output stream to capture print statements
            outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));
        }

        /**
         * Tests adding a vehicle to the station.
         * Verifies that the total number of vehicles increases after adding and the correct log message is printed.
         */
        @Test
        public void testAddVehicle() throws VehicleException {
            station.put(myVehicle);

            // Check the log message printed
            String expectedMessage = "Vehicle " + myVehicle.getId() + " added to Station " + station.getId();
            assertTrue(outputStream.toString().contains(expectedMessage), "The correct log message should be printed when adding a vehicle");

            assertEquals(1, station.totalBikes(), "Station should have 1 vehicle after adding a vehicle");
        }

        /**
         * Tests adding a vehicle when the station is full.
         * Verifies that the station does not allow adding more vehicles than its capacity.
         */
        @Test
        public void testAddVehicleWhenFull() throws VehicleException {
            station.put(myVehicle);
            station.put(new ClassicBike(2));

            // Station is now full
            assertTrue(station.isFull(), "Station should be full");

            // Try adding an additional vehicle
            station.put(new ClassicBike(3));

            // Check that the station's capacity is not exceeded and the correct log message is printed
            String expectedMessage = "Station " + station.getId() + " is full. Cannot add vehicle " + 3;
            assertTrue(outputStream.toString().contains(expectedMessage), "The correct log message should be printed when trying to add a vehicle to a full station");

            assertEquals(2, station.totalBikes(), "Station should not allow adding more vehicles than its capacity");
        }

        /**
         * Tests removing a vehicle from the station.
         * Verifies that the correct log message is printed and the station is empty after removing the vehicle.
         */
        @Test
        public void testRemoveVehicle() throws VehicleException {
            station.put(myVehicle);
            station.take(myVehicle);

            // Check the log message printed
            String expectedMessage = "Vehicle " + myVehicle.getId() + " removed from Station " + station.getId();
            assertTrue(outputStream.toString().contains(expectedMessage), "The correct log message should be printed when removing a vehicle");

            assertTrue(station.isEmpty(), "Station should be empty after removing the vehicle");
        }

        /**
         * Tests attempting to remove a vehicle that is not found.
         * Verifies that the correct log message is printed and the station retains existing vehicles.
         */
        @Test
        public void testRemoveVehicleNotFound() throws VehicleException {
            Vehicle otherVehicle = new ClassicBike(2);
            station.put(myVehicle);
            station.take(otherVehicle); // Trying to remove a vehicle that isn't in the station

            // Check the log message printed
            String expectedMessage = "Vehicle " + otherVehicle.getId() + " not found in Station " + station.getId();
            assertTrue(outputStream.toString().contains(expectedMessage), "The correct log message should be printed when trying to remove a non-existent vehicle");

            assertEquals(1, station.totalBikes(), "Station should still have the original vehicle when trying to remove a non-existent vehicle");
        }

        /**
         * Tests if the station is empty.
         * Verifies that the isEmpty() method returns true initially.
         */
        @Test
        public void testIsEmpty() throws VehicleException {
            assertTrue(station.isEmpty(), "Station should be empty initially");
            station.put(myVehicle);
            assertFalse(station.isEmpty(), "Station should not be empty after adding a vehicle");
        }

        /**
         * Tests if the station is full.
         * Verifies that the isFull() method returns true after reaching maximum capacity.
         */
        @Test
        public void testIsFull() throws VehicleException {
            station.put(myVehicle);
            station.put(new ClassicBike(2));

            assertTrue(station.isFull(), "Station should be full after reaching maximum capacity");
        }

        /**
         * Tests retrieving a vehicle by its ID.
         * Verifies that the vehicle is found and matches the one added.
         */
        @Test
        public void testGetVehicleById() throws VehicleException {
            station.put(myVehicle);
            Vehicle foundVehicle = station.getVehicleById(1);
            assertNotNull(foundVehicle, "Vehicle should be found by ID");
            assertEquals(myVehicle.getId(), foundVehicle.getId(), "Found vehicle should match the added vehicle");
        }

        /**
         * Tests retrieving a vehicle by ID when no vehicle is found.
         * Verifies that the method returns null when the vehicle isn't in the station.
         */
        @Test
        public void testGetVehicleByIdWhenNotFound() {
            Vehicle foundVehicle = station.getVehicleById(999);
            assertNull(foundVehicle, "Searching for a vehicle that does not exist should return null");
        }
        
        /**
         * Tests clearing all vehicles from the station.
         * Verifies that after clearing, the station is empty and no vehicles remain.
         */
        @Test
        public void testClearVehicles() throws VehicleException {
            station.put(myVehicle);
            station.clearVehicles();

            // Verify that the station is empty after clearing the vehicles
            assertTrue(station.isEmpty(), "Station should be empty after clearing all vehicles");

            // Verify the log message
            String expectedMessage = "All vehicles have been cleared from Station ";
            assertTrue(outputStream.toString().contains(expectedMessage), "The correct log message should be printed when vehicles are cleared");
        }
        
        /**
         * Tests getting the total number of vehicles in the station.
         * Verifies that the totalBikes() method returns the correct count.
         */
        @Test
        public void testTotalBikes() throws VehicleException {
            assertEquals(0, station.totalBikes(), "Station should have 0 vehicles initially");

            station.put(myVehicle);
            assertEquals(1, station.totalBikes(), "Station should have 1 vehicle after adding a vehicle");

            station.put(new ClassicBike(2));
            assertEquals(2, station.totalBikes(), "Station should have 2 vehicles after adding another vehicle");

            station.take(myVehicle);
            assertEquals(1, station.totalBikes(), "Station should have 1 vehicle after removing a vehicle");
        }

        /**
         * Tests the checkReparation method.
         * Verifies that broken vehicles are repaired.
         */
        @Test
        public void testCheckReparation() throws VehicleException {
            ConcreteRepairer repairer = new ConcreteRepairer();
            Vehicle brokenVehicle = new ClassicBike(2);
            brokenVehicle.setState(new Broken());

            station.put(brokenVehicle);
            station.checkReparation(repairer);

            assertFalse(brokenVehicle.getState() instanceof Broken, "Broken vehicle should be repaired");
        }
}
