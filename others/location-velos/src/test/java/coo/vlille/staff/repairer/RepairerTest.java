package coo.vlille.staff.repairer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.bike.electricBike.ElectricBike;
import coo.vlille.vehicle.decorator.BasketDecorator;
import coo.vlille.vehicle.decorator.LuggageRackDecorator;
import coo.vlille.vehicle.scooter.Scooter;

public class RepairerTest {

    @Test
    void testRepairerImplementation() {
        Repairer repairer = new ConcreteRepairer();
        ClassicBike classicBike = new ClassicBike(5);
        repairer.repair(classicBike);
        assertNotNull(classicBike.getState(), "ClassicBike should have a state after repair.");

        ElectricBike electricBike = new ElectricBike(1);
        repairer.repair(electricBike);
        assertNotNull(electricBike.getState(), "ElectricBike should have a state after repair.");

        Scooter scooter = new Scooter(2);
        repairer.repair(scooter);
        assertNotNull(scooter.getState(), "Scooter should have a state after repair.");

        LuggageRackDecorator luggageBike = new LuggageRackDecorator(new ClassicBike(3));
        repairer.repair(luggageBike);
        assertNotNull(luggageBike.getState(), "LuggageRackDecorator should have a state after repair.");

        BasketDecorator basketBike = new BasketDecorator(new ElectricBike(4));
        repairer.repair(basketBike);
        assertNotNull(basketBike.getState(), "BasketDecorator should have a state after repair.");
    }
}
