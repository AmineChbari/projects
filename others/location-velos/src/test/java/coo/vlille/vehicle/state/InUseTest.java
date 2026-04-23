package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.state.util.InUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the InUse class.
 * This class contains unit tests for the behavior of the InUse state.
 */
public class InUseTest {

    private Vehicle vehicle;

    /**
     * Set up the vehicle for each test.
     * This method runs before each test case to initialize a new vehicle.
     */
    @BeforeEach
    public void setUp() {
        vehicle = new ClassicBike(2); // Initialize a new ClassicBike (this could be adapted depending on the Vehicle class).
        vehicle.setState(new InUse()); // Set the initial state of the vehicle to "InUse".
    }

    /**
     * Test the take method in the InUse state.
     * Verifies that an exception is thrown when trying to take a vehicle that is "InUse".
     */
    @Test
    public void testTakeVehicle() {
        // Verify that an exception is thrown when trying to take a vehicle in the "InUse" state.
        InUseException exception = assertThrows(InUseException.class, () -> {
            vehicle.getState().take(vehicle);  // Call the take method on the InUse state.
        });

        // Verify that the exception message is correct.
        assertEquals("InUseException", exception.getMessage(),
                "The exception message should be correct when trying to take a vehicle that is in use.");
    }


    /**
     * Test the toString method in the InUse state.
     * Verifies that it correctly returns "In Use".
     */
    @Test
    public void testToString() {
        // Verify that the toString method returns "In Use".
        assertEquals("In Use", vehicle.getState().toString(), "The toString method should return 'In Use'.");
    }
}
