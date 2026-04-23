package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.state.util.VehicleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Broken class.
 * This class contains unit tests for the behavior of the Broken state.
 */
public class BrokenTest {

    private Vehicle vehicle;

    /**
     * Set up the vehicle for each test.
     * This method runs before each test case to initialize a new vehicle.
     */
    @BeforeEach
    public void setUp() {
        vehicle = new ClassicBike(2); // Initialize a new ClassicBike (this could be adapted depending on the Vehicle class).
        vehicle.setState(new Broken()); // Set the initial state of the vehicle to "Broken".
    }

    /**
     * Test the take method in the Broken state.
     * Verifies that an exception is thrown when trying to take a vehicle that is "Broken".
     */
    @Test
    public void testTakeVehicle() {
        // Verify that an exception is thrown when trying to take a "Broken" vehicle.
        VehicleException exception = assertThrows(VehicleException.class, () -> {
            vehicle.getState().take(vehicle);  // Call the take method on the Broken state.
        });

        // Verify that the exception message is correct.
        assertEquals("BrokenException", exception.getMessage(),
                "The exception message should be correct when trying to take a broken vehicle.");
    }

    /**
     * Test the put method in the Broken state.
     * Verifies that an exception is thrown when trying to put the vehicle back into the station while it's broken.
     */
    @Test
    public void testPutVehicleInStation() {
        // Verify that an exception is thrown when trying to put the broken vehicle back in the station.
        VehicleException exception = assertThrows(VehicleException.class, () -> {
            vehicle.getState().put(vehicle);
        });

        // Verify that the exception message is correct.
        assertEquals("BrokenException", exception.getMessage(),
                "The exception message should be correct when trying to put a broken vehicle in the station.");
    }

    /**
     * Test the toString method in the Broken state.
     * Verifies that it correctly returns "Broken".
     */
    @Test
    public void testToString() {
        // Verify that the toString method returns "Broken".
        assertEquals("Broken", vehicle.getState().toString(), "The toString method should return 'Broken'.");
    }
}
