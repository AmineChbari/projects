package coo.vlille.vehicle.state;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.state.util.VehicleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Available class.
 * This class contains unit tests for the behavior of the Available state.
 */
public class AvailableTest{

    private Vehicle vehicle;

    @BeforeEach
    public void setUp() {
        
        vehicle = new ClassicBike(2);
        vehicle.setState(new Available()); 
    }

    @Test
public void testTakeVehicle() {
    try {
        vehicle.getState().take(vehicle); 
    } catch (VehicleException e) {
        fail( e.getMessage());
    }

    assertTrue(vehicle.getState() instanceof InUse, "Le véhicule devrait être en état 'InUse' après avoir été pris.");
}


    @Test
    public void testPutVehicleInStation() {
        VehicleException exception = assertThrows(VehicleException.class, () -> {
            vehicle.getState().put(vehicle);
        });
        assertEquals("AvailableException", exception.getMessage(), "Le message d'exception devrait être correct.");
    }

    @Test
    public void testToString() {
        assertEquals("Available", vehicle.getState().toString(), "La méthode toString devrait renvoyer 'Available'.");
    }
}
