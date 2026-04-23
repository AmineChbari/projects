package coo.vlille;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import coo.vlille.controlCenter.ControlCenter;
import coo.vlille.controlCenter.redistribution.RoundRobinStrategy;
import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.station.Station;
import coo.vlille.timer.TimeManager;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.bike.electricBike.ElectricBike;
import coo.vlille.vehicle.decorator.BasketDecorator;
import coo.vlille.vehicle.decorator.LuggageRackDecorator;
import coo.vlille.vehicle.scooter.Scooter;
import coo.vlille.vehicle.state.util.VehicleException;

public class Main {
    // Codes ANSI pour les couleurs
    private static final String RESET = "\u001B[0m";  // Réinitialise la couleur
    private static final String GREEN = "\u001B[32m";  // Vert
    private static final String RED = "\u001B[31m";  // Rouge
    private static final String YELLOW = "\u001B[33m"; // Jaune
    private static final String CYAN = "\u001B[36m";  // Cyan
    private static final String PURPLE = "\u001B[35m";  // Violet
    private static final String BLUE = "\u001B[34m";  // Bleu

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n\n\n\n\n\n================================================");
                System.out.println("        " + CYAN + "\uD83D\uDEB2  Welcome to V'lille System \uD83D\uDEB2" + RESET);
                System.out.println("================================================\n");

                System.out.println("\n" + YELLOW + "\u25B6 Choose a scenario:" + RESET);
                System.out.println("  1: " + CYAN + "\uD83D\uDD04 Scenario 1 - Initialization and distribution with some withdraws" + RESET);
                System.out.println("  2: " + PURPLE + "\u26A1 Scenario 2 - Trigger Redistribution with a station Full" + RESET);
                System.out.println("  3: " + PURPLE + "\u274C Scenario 3 - Trigger Redistribution with a station Empty" + RESET);
                System.out.println("  4: " + BLUE + "🛠️ Scenario 4 - Vehicle Broken then fix it" + RESET);
                System.out.println("  5: " + BLUE + "🚨 Scenario 5 - Vehicle turning to Stolen" + RESET);
                System.out.println("  6: " + GREEN + "🖥️ Scenario 6 - Interface graphique" + RESET);
                System.out.println("  0: " + RED + "\uD83D\uDDD1  Exit" + RESET + "\n");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        scenario1();
                        break;
                    case 2:
                        scenario2();
                        break;
                    case 3:
                        scenario3();
                        break;
                    case 4:
                        scenario4();
                        break;
                    case 5:
                        scenario5();
                        break;
                    case 6:
                        useMainV2Interface();
                        break;    
                    
                    case 0:
                        System.out.println("\n Exiting the system. Goodbye! 👋\n");
                        System.exit(0);
                    default:
                        System.out.println("\n" + RED + "\u26D4 Invalid choice. Please try again." + RESET + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println("\n" + RED + "\u26A0 Input error: " + e.getMessage() + RESET);
        }
    }

    private static void useMainV2Interface() {
        MainV2 mainV2 = new MainV2();
        mainV2.main(new String[0]);  // Appel de la méthode main avec un tableau vide

    }

    public static void scenario1() throws InterruptedException {
        // Initialize the control center for Scenario 1
        ControlCenter controlCenter = new ControlCenter();
        controlCenter.setRedistributionStrategy(new RoundRobinStrategy());

        // Create the time manager
        TimeManager timeManager = new TimeManager(3000, () -> {
            try {
                controlCenter.checkAllRedistribute();
            } catch (VehicleException e) {
                e.printStackTrace();
            }
            System.out.println(controlCenter);
        });

        System.out.println("\n" + CYAN + "\uD83D\uDD04 Scenario 1: Initialization and Redistribution" + RESET);

        // creating and registring stations
        Station station1 = new Station(1, 3, controlCenter);
        Station station2 = new Station(2, 3, controlCenter);
        Station station3 = new Station(3, 4, controlCenter);
        Station station4 = new Station(4, 5, controlCenter);
        controlCenter.registerStation(station1);
        controlCenter.registerStation(station2);
        controlCenter.registerStation(station3);
        controlCenter.registerStation(station4);

        // creating and registring vehicles for station 1
        ClassicBike bike1 = new ClassicBike(101);
        LuggageRackDecorator bike2 = new LuggageRackDecorator(new ClassicBike(102));
        try {
            station1.put(bike1);
            station1.put(bike2);
        } catch (VehicleException e) {
            e.printStackTrace();
        }
        // creating and registring vehicles for station 2
        ElectricBike bike3 = new ElectricBike(103);
        ClassicBike bike4 = new ClassicBike(104);
        try {
            station2.put(bike3);
            station2.put(bike4);
        } catch (VehicleException e) {
            e.printStackTrace();
        }
        // creating and registring vehicles for station 3
        Scooter scooter1 = new Scooter(105);
        ClassicBike bike6 = new ClassicBike(106);
        ElectricBike bike7 = new ElectricBike(107);
        try {
            station3.put(scooter1);
            station3.put(bike6);
            station3.put(bike7);
        } catch (VehicleException e) {
            e.printStackTrace();
        }
        // creating and registring vehicles for station 4
        Scooter scooter2 = new Scooter(108);
        Scooter scooter3 = new Scooter(109);
        BasketDecorator bike8 = new BasketDecorator(new ElectricBike(110));
        ElectricBike bike9 = new ElectricBike(111);
        try {
            station4.put(scooter2);
            station4.put(scooter3);
            station4.put(bike8);
            station4.put(bike9);
        } catch (VehicleException e) {
            e.printStackTrace();
        }

        // Set up a latch to wait until the task completes
        CountDownLatch latch = new CountDownLatch(1);

        // Start the time manager and wait for 20 seconds
        timeManager.start(12500, latch);  // Run for 10 seconds

        // Create a scheduled executor service
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule step 1 to run immediately
        scheduler.schedule(() -> {
            System.out.println("step1 triggered");
            try {
                station1.take(bike1);
                station2.take(bike3);
                station3.take(scooter1);
                station4.take(scooter3);
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        }, 4, TimeUnit.SECONDS);

        // Schedule step 2 to run at 4 seconds
        scheduler.schedule(() -> {
            System.out.println("step2 triggered after 4 seconds");
            try {
                station1.put(bike3);
                station2.put(bike1);
                station4.take(scooter2);
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        }, 7, TimeUnit.SECONDS);

        // Schedule step 3 to run at 8 seconds
        scheduler.schedule(() -> {
            System.out.println("step3 triggered after 8 seconds");
            try {
                station3.put(scooter3);
                station4.put(scooter1);
                station4.put(scooter2);
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        }, 10, TimeUnit.SECONDS);

        try {
            latch.await();  // Block until the task finishes (10 seconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Shutdown the scheduler
        scheduler.shutdown();

        System.out.println("\nScenario 1 finished. Returning to menu...");
    }

    public static void scenario2() {
        // Initialize the control center for Scenario 2
        ControlCenter controlCenter = new ControlCenter();
        controlCenter.setRedistributionStrategy(new RoundRobinStrategy());

        // Create the time manager
        TimeManager timeManager = new TimeManager(2000, () -> {
            System.out.println(controlCenter);
            try {
                controlCenter.checkAllRedistribute();
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        });

        System.out.println("\n" + PURPLE + "\u26A1 Scenario 2: Trigger Redistribution with a station Full" + RESET);
        Station station1 = new Station(1, 3, controlCenter);
        Station station2 = new Station(2, 3, controlCenter);
        controlCenter.registerStation(station1);
        controlCenter.registerStation(station2);

        ClassicBike bike1 = new ClassicBike(101);
        ElectricBike bike2 = new ElectricBike(102);
        Scooter scooter1 = new Scooter(103);
        Scooter scooter2 = new Scooter(104);


        try {
            station1.put(bike1);
            station1.put(bike2);
            station1.put(scooter1);
            station2.put(scooter2);
        } catch (VehicleException e) {
            e.printStackTrace();
        }

        // Set up a latch to wait until the task completes
        CountDownLatch latch = new CountDownLatch(1);

        // Start the time manager and wait for 20 seconds
        timeManager.start(6000, latch);  // Run for 6 seconds

        try {
            latch.await();  // Block until the task finishes (6 seconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nScenario 2 finished. Returning to menu...");
    }

    public static void scenario3() {
        // Initialize the control center for Scenario 3
        ControlCenter controlCenter = new ControlCenter();
        controlCenter.setRedistributionStrategy(new RoundRobinStrategy());

        // Create the time manager
        TimeManager timeManager = new TimeManager(2000, () -> {
            System.out.println(controlCenter);
            try {
                controlCenter.checkAllRedistribute();
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        });

        System.out.println("\n" + PURPLE + "\u274C Scenario 3: Trigger Redistribution with a station Empty" + RESET);
        Station station1 = new Station(1, 3, controlCenter);
        Station station2 = new Station(2, 3, controlCenter);
        controlCenter.registerStation(station1);
        controlCenter.registerStation(station2);

        ClassicBike bike1 = new ClassicBike(101);
        ElectricBike bike2 = new ElectricBike(102);
        Scooter scooter1 = new Scooter(103);

        try {
            station1.put(bike1);
            station1.put(bike2);
            station1.put(scooter1);
        } catch (VehicleException e) {
            e.printStackTrace();
        }

        // Set up a latch to wait until the task completes
        CountDownLatch latch = new CountDownLatch(1);

        // Start the time manager and wait for 20 seconds
        timeManager.start(6000, latch);  // Run for 12 seconds

        try {
            latch.await();  // Block until the task finishes (12 seconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nScenario 3 finished. Returning to menu...");
    }

    public static void scenario4() {
        // Initialize the control center for Scenario 4
        ControlCenter controlCenter = new ControlCenter();
        ConcreteRepairer repairer = new ConcreteRepairer();
        controlCenter.setRedistributionStrategy(new RoundRobinStrategy());

        // Create the time manager
        TimeManager timeManager = new TimeManager(2000, () -> {
            System.out.println(controlCenter);
            controlCenter.checkAllRepairs(repairer);
            try {
                controlCenter.checkAllRedistribute();
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        });

        System.out.println("\n" + CYAN + "🛠️ Scenario 4: Vehicle Out of Service" + RESET);
        Station station1 = new Station(1, 3, controlCenter);
        Station station2 = new Station(2, 3, controlCenter);
        controlCenter.registerStation(station1);
        controlCenter.registerStation(station2);

        ClassicBike bike1 = new ClassicBike(101);
        ElectricBike bike2 = new ElectricBike(102);
        Scooter scooter1 = new Scooter(103);

        try {
            station1.put(bike1);
            station2.put(bike2);
            station1.put(scooter1);
        } catch (VehicleException e) {
            e.printStackTrace();
        }

        // Set up a latch to wait until the task completes
        CountDownLatch latch = new CountDownLatch(1);

        // Start the time manager and wait for 11 seconds
        timeManager.start(11000, latch);  // Run for 11 seconds

        // Create a scheduled executor service
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule step 1 to run at 3 seconds
        scheduler.schedule(() -> {
            System.out.println("step1 triggered : using scooter for the first time");
            try {
                station1.take(scooter1);
                station2.put(scooter1);
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        }, 3, TimeUnit.SECONDS);

        // Schedule step 2 to run at 5 seconds
        scheduler.schedule(() -> {
            System.out.println("step2 triggered after 4 seconds : using scooter for the second time");
            try {
                station2.take(scooter1);
                station1.put(scooter1);
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);

        // Schedule step 3 to run at 7 seconds
        scheduler.schedule(() -> {
            System.out.println("step3 triggered after 8 seconds : using scooter for the third time");
            try {
                station1.take(scooter1);
                station2.put(scooter1);
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        }, 7, TimeUnit.SECONDS);

        try {
            latch.await();  // Block until the task finishes (11 seconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Shutdown the scheduler
        scheduler.shutdown();

        System.out.println("\nScenario 4 finished. Returning to menu...");
    }


    public static void scenario5() {
        // Initialize the control center for Scenario 5
        ControlCenter controlCenter = new ControlCenter();
        controlCenter.setRedistributionStrategy(new RoundRobinStrategy());
    
        // Create the time manager
        TimeManager timeManager = new TimeManager(5000, () -> {
            System.out.println(controlCenter);
            controlCenter.checkAllStolen();
        });
        System.out.println("\n 🚨 Scenario 5: Stolen Vehicle");
    
        // Create the stations
        Station station1 = new Station(1, 3, controlCenter);
        controlCenter.registerStation(station1);
    
        // Add vehicles to the stations
        ClassicBike bike1 = new ClassicBike(101);
        ElectricBike bike2 = new ElectricBike(102);
    
        try {
            station1.put(bike1);
            station1.put(bike2);
        } catch (VehicleException e) {
            e.printStackTrace();
        }
    
        // Create a scheduled executor service
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
        // Schedule taking one vehicle
        scheduler.schedule(() -> {
            System.out.println("step1 triggered : using bike1 for the first time");
            try {
                station1.take(bike1);
            } catch (VehicleException e) {
                e.printStackTrace();
            }
        }, Station.NB_SECONDS_STOLEN, TimeUnit.SECONDS);
    
        // Set up a latch to wait until the task completes
        CountDownLatch latch = new CountDownLatch(1);
    
        // Start the time manager and wait for 17 seconds
        timeManager.start(17000, latch);  // Run for 17 seconds
    
        try {
            latch.await();  // Block until the task finishes (17 seconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    
        // Shutdown the scheduler
        scheduler.shutdown();
    
        System.out.println("\nScenario 5 finished. Returning to menu...");
    }
}
