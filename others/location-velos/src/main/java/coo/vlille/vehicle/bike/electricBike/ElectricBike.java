package coo.vlille.vehicle.bike.electricBike;

import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.bike.Bike;

/**
 * Represents an Electric Bike, which is a type of bike with an additional battery attribute.
 * Inherits from the generic Bike class.
 */
public class ElectricBike extends Bike {

    // The current battery level of the electric bike (0 to BATTERY_MAX).
    private int battery;

    // The maximum battery level for an electric bike.
    private static final int BATTERY_MAX = 100;

    /**
     * Constructor to initialize an ElectricBike with a given ID.
     * The battery is set to its maximum level upon creation.
     *
     * @param id The unique identifier of the electric bike.
     */
    public ElectricBike(int id) {
        super(id);
        this.battery = BATTERY_MAX;
    }

    /**
     * Gets the current battery level of the electric bike.
     *
     * @return The current battery level as an integer.
     */
    public int getBattery() {
        return this.battery;
    }

    /**
     * Repairs the electric bike.
     */
    @Override
    public void repair(ConcreteRepairer repairer) {
        repairer.repair(this);
    }

    /**
     * Returns a string representation of the electric bike, including its ID and type.
     *
     * @return A formatted string describing the electric bike.
     */
    @Override
    public String toString() {
        return " ⚡🚲 Electric Bike [" + super.toString() + " ]";
    }
}