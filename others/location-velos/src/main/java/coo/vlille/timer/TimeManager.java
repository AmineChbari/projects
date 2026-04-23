package coo.vlille.timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The TimeManager class manages periodic execution of a task at a specified interval.
 * It uses a ScheduledExecutorService to handle timed task execution and allows for 
 * an initial delay before starting the periodic executions.
 * It also stops after a given duration.
 */
public class TimeManager {
    private final long interval; // The interval in milliseconds between successive task executions.
    private final Runnable task; // The task to be executed periodically.
    private ScheduledExecutorService scheduler; // Scheduler for managing task execution.

    /**
     * Constructs a TimeManager instance with the specified interval and task.
     *
     * @param interval The time interval in milliseconds between successive executions of the task.
     * @param task     The task to execute periodically. This cannot be null.
     * @throws IllegalArgumentException If the interval is non-positive.
     * @throws NullPointerException     If the task is null.
     */
    public TimeManager(long interval, Runnable task) {
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval must be positive.");
        }
        if (task == null) {
            throw new NullPointerException("Task cannot be null.");
        }

        this.interval = interval;
        this.task = task;
        //this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts the TimeManager, scheduling the task to run periodically after an initial delay
     * equal to the interval. The task will run at fixed intervals thereafter and stop after 20 seconds.
     */
    public void start(long durationMillis, CountDownLatch latch) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        long initialDelay = interval; // Initial delay before the first execution.
        scheduler.scheduleAtFixedRate(task, initialDelay, interval, TimeUnit.MILLISECONDS);

        // Stop the task after the given duration (20 seconds in this case)
        scheduler.schedule(() -> {
            stop();
            latch.countDown(); // Signal that the task is done
        }, durationMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the TimeManager, shutting down the scheduler.
     * Any pending or future executions of the task are canceled.
     */
    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown(); // Properly shut down the scheduler
        }
    }
}
