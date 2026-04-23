package coo.vlille.vehicle.bike.classicBike;

import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.bike.Bike;

public class ClassicBike extends Bike{
    /**
     * Constructor for ClassicBike class.
     * @param id The id of the classic bike.
     */
    public ClassicBike(int id) {
        super(id);
    }
    
    /**
     * Repairs the classic bike.
     */
    @Override
    public void repair(ConcreteRepairer repairer) {
        repairer.repair(this);
    }

    /**
     * Returns a string representation of the ClassicBike object.
     * This method overrides the toString method of the superclass.
     * 
     * @return a string that represents the ClassicBike object, 
     *         including a bicycle emoji and the result of the superclass's toString method.
     */
    @Override
    public String toString() {
        return "  🚲 Classic Bike [" + super.toString() + " ]";
    }
}
