package coo.vlille.vehicle.state.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AvailableExceptionTest {

    @Test
    public void testExceptionMessage() {
        AvailableException exception = new AvailableException();
        assertEquals("AvailableException", exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        AvailableException exception = new AvailableException();
        assertTrue(exception instanceof VehicleException);
        assertTrue(exception instanceof Exception);
    }
}