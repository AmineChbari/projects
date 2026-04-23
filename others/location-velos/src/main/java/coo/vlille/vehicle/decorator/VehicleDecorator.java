package coo.vlille.vehicle.decorator;

import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.VehicleException;

/**
 * Abstract class representing a Vehicle Decorator.
 * Decorators can extend the functionality of a Vehicle.
 */
public abstract class VehicleDecorator extends Vehicle {
    protected Vehicle decoratedVehicle;

    /**
     * Constructor for VehicleDecorator.
     * @param vehicle The vehicle to be decorated.
     */
    protected VehicleDecorator(Vehicle vehicle) {
        super(vehicle.getId());
        this.decoratedVehicle = vehicle;
    }

    /**
     * representing a decorated vehicle with a string
     * @return string representation
     */
    @Override
    public String toString() {
        return decoratedVehicle.toString();
    }

    /**
     * getter for number of use of decorated vehicle
     * @return number of the time being used
     */
    @Override
    public int getNbUse() {
        return decoratedVehicle.getNbUse();
    }

    @Override
    public void put() throws VehicleException {
        super.put();
    }

    @Override
    public void take() throws VehicleException {
        super.take();
    }

    /**
     * increase number of use of decorated vehicle
     */
    @Override
    public void increaseNbUse() {
        decoratedVehicle.increaseNbUse();
    }

    /**
     * repair the vehicle 
     * 
     * @param repairer the repairer that will repair the vehicle
     */
    @Override
    public abstract void repair(ConcreteRepairer repairer);
}
