package coo.vlille;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    public void testScenario1() {
        assertDoesNotThrow(() -> {
            Method method = Main.class.getDeclaredMethod("scenario1");
            method.setAccessible(true);
            method.invoke(null);
        });
    }

    @Test
    public void testScenario2() {
        assertDoesNotThrow(() -> {
            Method method = Main.class.getDeclaredMethod("scenario2");
            method.setAccessible(true);
            method.invoke(null);
        });
    }

    @Test
    public void testScenario3() {
        assertDoesNotThrow(() -> {
            Method method = Main.class.getDeclaredMethod("scenario3");
            method.setAccessible(true);
            method.invoke(null);
        });
    }

    @Test
    public void testScenario4() {
        assertDoesNotThrow(() -> {
            Method method = Main.class.getDeclaredMethod("scenario4");
            method.setAccessible(true);
            method.invoke(null);
        });
    }

    @Test
    public void testScenario5() {
        assertDoesNotThrow(() -> {
            Method method = Main.class.getDeclaredMethod("scenario5");
            method.setAccessible(true);
            method.invoke(null);
        });
    }
}