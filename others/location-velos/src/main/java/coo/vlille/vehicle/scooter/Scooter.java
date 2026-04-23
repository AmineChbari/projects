package coo.vlille.vehicle.scooter;

import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.state.util.VehicleException;

/**
 * Represents a scooter with a rechargeable battery.
 * Inherits from the {@link Vehicle} class and adds battery functionality.
 */
public class Scooter extends Vehicle {

    // Current battery level of the scooter
    private int battery;

    // Maximum battery capacity
    private static final int BATTERY_MAX = 100;

    /**
     * Creates a new Scooter with the given ID and sets the battery to full.
     *
     * @param id The unique identifier for the scooter.
     */
    public Scooter(int id) {
        super(id);
        this.battery = BATTERY_MAX;
    }

    /**
     * Returns the current battery level of the scooter.
     *
     * @return The battery level as a percentage.
     */
    public int getBattery() {
        return this.battery;
    }

    /**
     * Sets the battery level of the scooter.
     *
     * @param battery The battery level as a percentage.
     */
    public void setBattery(int battery) {
        this.battery = battery;
    }

    /**
     * Uses the scooter and reduces the battery by 10%.
     *
     * @throws VehicleException If the scooter is already in use.
     */
    @Override
    public void take() throws VehicleException {
        super.take();
        this.battery -= 10;
    }

    /**
     * Returns the scooter to the station and recharges the battery to full.
     *
     * @throws VehicleException If the scooter is already available.
     */
    @Override
    public void repair(ConcreteRepairer repairer) {
        repairer.repair(this);
    }

    /**
     * Returns a description of the scooter, including its ID, state, and battery level.
     *
     * @return A string representation of the scooter.
     */
    @Override
    public String toString() {
        return " 🛴 Scooter [" + super.toString() + ", " + this.battery + "% ]";
    }
}
