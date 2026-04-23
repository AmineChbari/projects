package coo.vlille.vehicle;

import coo.vlille.vehicle.decorator.BasketDecorator;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasketTest extends VehicleTest {

    @Override
    protected Vehicle createVehicle() {
        return new BasketDecorator(new ClassicBike(1)); // Décoration d'un vélo classique avec panier
    }

    @Test
    public void testBasketToString() {
        assertTrue(vehicle.toString().contains("Basket"), "Vehicle should contain Basket in its toString");
    }

    @Test
    public void testBasketUsage() {
        vehicle.increaseNbUse();
        assertEquals(1, vehicle.getNbUse(), "Usage count should be 1 after one use.");
    }
}
