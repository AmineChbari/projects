package coo.vlille.vehicle.decorator;

import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.Vehicle;

/**
 * Decorator for adding a luggage rack to a Vehicle.
 */
public class LuggageRackDecorator extends VehicleDecorator {

    /**
     * Constructor for LuggageRackDecorator.
     * @param vehicle The vehicle to be decorated with a luggage rack.
     */
    public LuggageRackDecorator(Vehicle vehicle) {
        super(vehicle);
    }

    /**
     * Repairs the decorated bike.
     */
    @Override
    public void repair(ConcreteRepairer repairer) {
        repairer.repair(this);
    }

    /**
     * representing a vehicle that has Luggage Rack
     * @return string representation vehicle with Luggage Rack
     */
    @Override
    public String toString() {
        return (super.toString() + " with Luggage Rack 🧳");
    }
}
