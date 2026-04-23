package coo.vlille.vehicle.state.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class VehicleExceptionTest {

    @Test
    public void testExceptionMessage() {
        VehicleException exception = new VehicleException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        VehicleException exception = new VehicleException("Test message");
        assertTrue(exception instanceof Exception);
    }
}