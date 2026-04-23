package coo.vlille.staff.repairer;

import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.bike.electricBike.ElectricBike;
import coo.vlille.vehicle.decorator.BasketDecorator;
import coo.vlille.vehicle.decorator.LuggageRackDecorator;
import coo.vlille.vehicle.scooter.Scooter;


/** Abstract class representing a repairer 
 * that fixes all types of vehicles in the network 
*/
public abstract class Repairer {

    /**
     * abstract method to repair a vehicle of type classic bike
     * 
     * @param classicBike vehicle to repair
    */
    public abstract void repair(ClassicBike classicBike);

    /**
     * abstract method to repair a vehicle of type electric bike
     * 
     * @param electricBike vehicle to repair
    */
    public abstract void repair(ElectricBike electricBike);

    /**
     * abstract method to repair a vehicle of type scooter
     * 
     * @param scooter vehicle to repair
    */
    public abstract void repair(Scooter scooter);

    /**
     * abstract method to repair a vehicle of type scooter
     * 
     * @param scooter vehicle to repair
    */
    public abstract void repair(LuggageRackDecorator vc);

    /**
     * abstract method to repair a vehicle of type scooter
     * 
     * @param scooter vehicle to repair
    */
    public abstract void repair(BasketDecorator vc);
}