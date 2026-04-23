package coo.vlille.staff.repairer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.bike.electricBike.ElectricBike;
import coo.vlille.vehicle.decorator.BasketDecorator;
import coo.vlille.vehicle.decorator.LuggageRackDecorator;
import coo.vlille.vehicle.scooter.Scooter;
import coo.vlille.vehicle.state.Available;

public class ConcreteRepairerTest {

    @Test
    void testRepairClassicBike() {
        ConcreteRepairer repairer = new ConcreteRepairer();
        ClassicBike bike = new ClassicBike(1);
        repairer.repair(bike);
        assertEquals(Available.class, bike.getState().getClass(), "ClassicBike should be repaired and set to Available state.");
    }

    @Test
    void testRepairElectricBike() {
        ConcreteRepairer repairer = new ConcreteRepairer();
        ElectricBike bike = new ElectricBike(2);
        repairer.repair(bike);
        assertEquals(Available.class, bike.getState().getClass(), "ElectricBike should be repaired and set to Available state.");
    }

    @Test
    void testRepairScooter() {
        ConcreteRepairer repairer = new ConcreteRepairer();
        Scooter scooter = new Scooter(3);
        repairer.repair(scooter);
        assertEquals(Available.class, scooter.getState().getClass(), "Scooter should be repaired and set to Available state.");
        assertEquals(100, scooter.getBattery(), "Scooter battery should be fully charged after repair.");
    }

    @Test
    void testRepairLuggageRackDecorator() {
        ConcreteRepairer repairer = new ConcreteRepairer();
        LuggageRackDecorator bike = new LuggageRackDecorator(new ClassicBike(4));
        repairer.repair(bike);
        assertEquals(Available.class, bike.getState().getClass(), "LuggageRackDecorator bike should be repaired and set to Available state.");
    }

    @Test
    void testRepairBasketDecorator() {
        ConcreteRepairer repairer = new ConcreteRepairer();
        BasketDecorator bike = new BasketDecorator(new ElectricBike(5));
        repairer.repair(bike);
        assertEquals(Available.class, bike.getState().getClass(), "BasketDecorator bike should be repaired and set to Available state.");
    }
}
