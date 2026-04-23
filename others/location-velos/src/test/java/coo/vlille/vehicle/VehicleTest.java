package coo.vlille.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import coo.vlille.vehicle.state.*;
import coo.vlille.vehicle.util.Color;


public abstract class VehicleTest {
	
    protected abstract Vehicle createVehicle(); // Redéfini en sous-classes
    protected Vehicle vehicle;

    @BeforeEach
    public void init() {
        this.vehicle = this.createVehicle(); // Factory method
    }

    @Test
    public void testSetColor() {
        Color newColor = Color.BLUE;
        vehicle.setColor(newColor);
        assertEquals(newColor, vehicle.getColor());
    }

    @Test
    public void testGetSetId() {
        int newId = 42; // New ID to be set
        vehicle.setId(newId); // Set the new ID
        assertEquals(newId, vehicle.getId()); // Verify that the ID has been updated correctly
    }

    @Test
    public void testSetState() {
        VehicleState newState = new Available(); // Create a new state (use an appropriate state from your project)
        vehicle.setState(newState); // Set the new state
        assertEquals(newState, vehicle.state); // Verify that the state has been updated correctly
    }

    @Test
    public void testGetNbUse() {
        assertEquals(0, vehicle.getNbUse()); // Initially, the number of uses should be 0
    }

    @Test
    public void testIncreaseNbUse() {
        vehicle.increaseNbUse(); // Increase the usage count
        assertEquals(1, vehicle.getNbUse()); // Verify that the count has increased to 1
        vehicle.increaseNbUse(); // Increase again
        assertEquals(2, vehicle.getNbUse()); // Verify that the count has increased to 2
    }

    @Test
    public void testEqualsWithSameId() {
        Vehicle anotherVehicle = this.createVehicle(); // Create another vehicle instance
        vehicle.setId(1); // Set the same ID
        anotherVehicle.setId(1); // Set the same ID
        assertTrue(vehicle.equals(anotherVehicle), "Vehicles with the same ID should be equal");
    }

    @Test
    public void testEqualsWithDifferentId() {
        Vehicle anotherVehicle = this.createVehicle(); // Create another vehicle instance
        vehicle.setId(1); // Set ID for this vehicle
        anotherVehicle.setId(2); // Set a different ID for the other vehicle
        assertFalse(vehicle.equals(anotherVehicle), "Vehicles with different IDs should not be equal");
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(vehicle.equals(null), "Vehicle should not be equal to null");
    }

    @Test
    public void testEqualsWithDifferentType() {
        String differentType = "I am not a Vehicle";
        assertFalse(vehicle.equals(differentType), "Vehicle should not be equal to an object of a different type");
    }
}

