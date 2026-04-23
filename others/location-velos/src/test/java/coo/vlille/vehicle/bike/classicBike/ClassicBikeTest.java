package coo.vlille.vehicle.bike.classicBike;

import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.bike.BikeTest;

public class ClassicBikeTest extends BikeTest {

    @Override
    protected Vehicle createVehicle() {
        return new ClassicBike(1); // Pass a valid ID
    }

    // Additional tests specific to ClassicBike can be added here
}
