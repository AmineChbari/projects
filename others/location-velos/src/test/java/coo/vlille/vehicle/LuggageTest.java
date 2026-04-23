package coo.vlille.vehicle;

import coo.vlille.vehicle.bike.electricBike.ElectricBike;
import coo.vlille.vehicle.decorator.LuggageRackDecorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LuggageTest extends VehicleTest {

    @Override
    protected Vehicle createVehicle() {
        return new LuggageRackDecorator(new ElectricBike(2)); // Décoration d'un vélo électrique avec porte-bagages
    }

    @Test
    public void testLuggageToString() {
        // Vérifier que le porte-bagages est bien ajouté à la chaîne de caractères du vélo
        assertTrue(vehicle.toString().contains("Luggage"), "Vehicle should contain Luggage in its toString");
    }

    @Test
    public void testLuggageUsage() {
        // Augmenter l'usage et vérifier qu'il fonctionne correctement
        vehicle.increaseNbUse();
        assertEquals(1, vehicle.getNbUse(), "Usage count should be 1 after one use.");
    }
}
