package coo.vlille.vehicle.decorator;

import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.Vehicle;

/**
 * Decorator for adding a basket to a Vehicle.
 */
public class BasketDecorator extends VehicleDecorator {

    /**
     * Constructor for BasketDecorator.
     * @param vehicle The vehicle to be decorated with a basket.
     */
    public BasketDecorator(Vehicle vehicle) {
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
     * representing a vehicle that has Basket
     * @return string representation of vehicle with Basket
     */
    @Override
    public String toString() {
        return super.toString() + " with Basket 🧺";
    }
}
