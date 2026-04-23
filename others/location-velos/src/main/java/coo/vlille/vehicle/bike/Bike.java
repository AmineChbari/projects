package coo.vlille.vehicle.bike;

import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.Vehicle;

/**
 * Abstract Class representing a Bike
 *
 */
public abstract class Bike extends Vehicle{
    
    /**
     * Constructor for Bike class
     * @param id The id of the bike
     */
    protected Bike(int id) {
        super(id);
    }

    /**
     * repair the vehicle 
     * 
     * @param repairer the repairer that will repair the vehicle
     */
    @Override
    public abstract void repair(ConcreteRepairer repairer);
    
}
