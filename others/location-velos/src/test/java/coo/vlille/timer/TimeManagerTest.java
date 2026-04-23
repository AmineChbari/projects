package coo.vlille.timer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;





public class TimeManagerTest {

    private Runnable task;
    private CountDownLatch latch;

    @BeforeEach
    public void setUp() {
        task = () -> System.out.println("Task executed");
        latch = new CountDownLatch(1);
    }

    @Test
    public void testConstructorWithValidArguments() {
        TimeManager timeManager = new TimeManager(1000, task);
        assertNotNull(timeManager);
    }

    @Test
    public void testConstructorWithInvalidInterval() {
        assertThrows(IllegalArgumentException.class, () -> new TimeManager(0, task));
    }

    @Test
    public void testConstructorWithNullTask() {
        assertThrows(NullPointerException.class, () -> new TimeManager(1000, null));
    }

    @Test
    @Timeout(5)
    public void testStartAndStop() throws InterruptedException {
        TimeManager timeManager = new TimeManager(1000, task);
        timeManager.start(2000, latch);
        assertTrue(latch.await(3, TimeUnit.SECONDS), "Task did not complete in the expected time");
        timeManager.stop();
    }

    @Test
    public void testStopWithoutStart() {
        TimeManager timeManager = new TimeManager(1000, task);
        timeManager.stop();
        assertTrue(true, "Stop without start should not throw any exception");
    }
}