package coo.vlille.vehicle.state.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class StolenExceptionTest {

    @Test
    public void testExceptionMessage() {
        StolenException exception = new StolenException();
        assertEquals("StolenException", exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        StolenException exception = new StolenException();
        assertTrue(exception instanceof VehicleException);
        assertTrue(exception instanceof Exception);
    }
}