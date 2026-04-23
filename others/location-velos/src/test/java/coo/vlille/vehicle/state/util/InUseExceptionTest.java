package coo.vlille.vehicle.state.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class InUseExceptionTest {

    @Test
    public void testExceptionMessage() {
        InUseException exception = new InUseException();
        assertEquals("InUseException", exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        InUseException exception = new InUseException();
        assertTrue(exception instanceof VehicleException);
        assertTrue(exception instanceof Exception);
    }
}