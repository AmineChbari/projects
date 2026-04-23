package coo.vlille.staff.repairer;

import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.bike.electricBike.ElectricBike;
import coo.vlille.vehicle.decorator.BasketDecorator;
import coo.vlille.vehicle.decorator.LuggageRackDecorator;
import coo.vlille.vehicle.scooter.Scooter;
import coo.vlille.vehicle.state.Available;

/** Class representing a repairer 
 * that fixes all types of vehicles in the network 
*/
public class ConcreteRepairer extends Repairer{

    /**
     * repair a vehicle of type classic bike
     * 
     * @param classicBike vehicle to repair
     */
    public void repair(ClassicBike vehicle) {
        try {
            Thread.sleep(3000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        vehicle.setState(new Available());
        System.out.println("🛠️ Repairing the Classic bike:");
        System.out.println("Reparation takes 3 seconds...");
    }

    /**
     * repair a vehicle of type electric bike
     * 
     * @param electricBike vehicle to repair
     */
    public void repair(ElectricBike vehicle) {
        try {
            Thread.sleep(3000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        vehicle.setState(new Available());
        System.out.println("🛠️ Repairing the Electric bike:");
        System.out.println("Reparation takes 3 seconds...");
    }

    /**
     * repair a vehicle of type scooter
     * 
     * @param scooter vehicle to repair
     */
    public void repair(Scooter vehicle) {
        try {
            Thread.sleep(3000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        vehicle.setState(new Available());
        System.out.println("🛠️ Repairing the Scooter:");
        vehicle.setBattery(100);
        System.out.println("Reset Battery to 💯 % charge");
        System.out.println("Reparation takes 3 seconds...");
    }

    /**
     * method to repair a vehicle of type luggage rack decorated bike
     * 
     * @param vehicle vehicle to repair
    */
    public void repair(LuggageRackDecorator vehicle) {
        try {
            Thread.sleep(3000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        vehicle.setState(new Available());
        System.out.println("🛠️ Repairing the decorated bike:");
        System.out.println("Reparation takes 3 seconds...");
    }

    /**
     * method to repair a vehicle of type basket decorated bike
     * 
     * @param vehicle vehicle to repair
    */
    public void repair(BasketDecorator vehicle) {
        try {
            Thread.sleep(3000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        vehicle.setState(new Available());
        System.out.println("🛠️ Repairing the decorated bike:");
        System.out.println("Reparation takes 3 seconds...");
    }
}