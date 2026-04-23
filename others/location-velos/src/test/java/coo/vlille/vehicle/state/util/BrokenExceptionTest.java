package coo.vlille.vehicle.state.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BrokenExceptionTest {

    @Test
    public void testExceptionMessage() {
        BrokenException exception = new BrokenException();
        assertEquals("BrokenException", exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        BrokenException exception = new BrokenException();
        assertTrue(exception instanceof VehicleException);
        assertTrue(exception instanceof Exception);
    }
}