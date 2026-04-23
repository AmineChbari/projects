package coo.vlille.vehicle.bike.electricBike;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.bike.BikeTest;

public class ElectricBikeTest extends BikeTest {

    @Override
    protected Vehicle createVehicle() {
        return new ElectricBike(1); // Pass a valid ID
    }

    // Additional tests specific to ElectricBike can be added here
}
