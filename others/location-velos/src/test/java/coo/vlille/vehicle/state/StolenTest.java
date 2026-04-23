package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.state.util.StolenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Stolen class.
 * This class contains unit tests for the behavior of the Stolen state.
 */
public class StolenTest {

    private Vehicle vehicle;

    /**
     * Set up the vehicle for each test.
     * This method runs before each test case to initialize a new vehicle.
     */
    @BeforeEach
    public void setUp() {
        vehicle = new ClassicBike(2); // Initialize a new ClassicBike (this could be adapted depending on the Vehicle class).
        vehicle.setState(new Stolen()); // Set the initial state of the vehicle to "Stolen".
    }

    /**
     * Test the take method in the Stolen state.
     * Verifies that an exception is thrown when trying to take a stolen vehicle.
     */
    @Test
    public void testTakeVehicle() {
        // Verify that an exception is thrown when trying to take a vehicle in the "Stolen" state.
        StolenException exception = assertThrows(StolenException.class, () -> {
            vehicle.getState().take(vehicle);  // Call the take method on the Stolen state.
        });

        // Verify that the exception message is correct.
        assertEquals("StolenException", exception.getMessage());
    }

    /**
     * Test the put method in the Stolen state.
     * Verifies that an exception is thrown when trying to put a stolen vehicle back into the station.
     */
    @Test
    public void testPutVehicleInStation() {
        // Verify that an exception is thrown when trying to put a stolen vehicle in the station.
        StolenException exception = assertThrows(StolenException.class, () -> {
            vehicle.getState().put(vehicle);  // Call the put method on the Stolen state.
        });

        // Verify that the exception message is correct.
        assertEquals("StolenException", exception.getMessage());
    }

    /**
     * Test the toString method in the Stolen state.
     * Verifies that it correctly returns "Stolen".
     */
    @Test
    public void testToString() {
        // Verify that the toString method returns "Stolen".
        assertEquals("Stolen", vehicle.getState().toString(), "The toString method should return 'Stolen'.");
    }
}
