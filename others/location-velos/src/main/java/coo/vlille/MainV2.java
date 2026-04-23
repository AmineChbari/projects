package coo.vlille;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;

import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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




public class MainV2 extends JFrame {

     private ControlCenter controlCenter;
     private JTextArea textArea;  // Zone de texte pour afficher les résultats

     // Image de fond et logo
     private Image backgroundImage;
     private Image logoImage;  // Image du logo

    // Variable pour suivre l'état du mode sombre
    private boolean darkMode = false;

     public MainV2() {
         // Charger le logo et réduire sa taille (ici on le met à 100x100 px)
         URL logoURL = getClass().getResource("/image/logo.png");
         if (logoURL != null) {
             ImageIcon logoIcon = new ImageIcon(logoURL);
             logoImage = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);  // Redimensionner le logo
         } else {
             System.err.println("Logo not found!");
         }

         // Configurer la fenêtre JFrame
         setTitle("V'lille System V2");
         setSize(800, 600);  // Augmenter la taille de la fenêtre pour un meilleur affichage
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setLocationRelativeTo(null);  // Centrer la fenêtre
         setUndecorated(true);  // Retirer les bordures et la barre de titre (optionnel)

         // Initialisation du ControlCenter
         controlCenter = new ControlCenter();
         controlCenter.setRedistributionStrategy(new RoundRobinStrategy());

         // Créer la page d'accueil
         JPanel homePage = createHomePage();
         getContentPane().add(homePage);

         // Rediriger System.out vers la JTextArea
         PrintStream printStream = new PrintStream(new OutputStream() {
             @Override
             public void write(int b) {
                 textArea.append(String.valueOf((char) b));
                 textArea.setCaretPosition(textArea.getDocument().getLength());  // Faire défiler vers le bas
             }
        });
         System.setOut(printStream);  // Rediriger System.out vers la JTextArea
     }

     // Créer la page d'accueil avec l'image de fond
     private JPanel createHomePage() {
         JPanel panel = new JPanel() {
            @Override
             public void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 // Dessiner l'image de fond uniquement pour la page d'accueil
                 if (backgroundImage != null) {
                     g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                 }
             }
         };
         panel.setLayout(new BorderLayout());

         // Charger l'image de fond pour la page d'accueil
         URL backgroundURL = getClass().getResource("/image/background.jpg");  // Assurez-vous que le chemin est correct
        if (backgroundURL != null) {
            ImageIcon backgroundIcon = new ImageIcon(backgroundURL);
            backgroundImage = backgroundIcon.getImage();
        } else {
           System.err.println("Background image not found!");
        }

         // Créer un label pour afficher "Welcome to VLille"
        JLabel titleLabel = new JLabel("Welcome to V'lille System", SwingConstants.CENTER);
         titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
         titleLabel.setForeground(Color.WHITE);

         // Créer un label pour la description
         JLabel descriptionLabel = new JLabel("<html>This is a bike rental system that allows users to rent bikes and other urban transport modes such as scooters, with a control center overseeing the redistribution and maintenance of the vehicles.</html>", SwingConstants.CENTER);
         descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
         descriptionLabel.setForeground(Color.WHITE);

        // Créer un bouton "Start" pour passer à la page principale
        JButton startButton = new JButton("Start");
         startButton.setFont(new Font("Arial", Font.BOLD, 18));  // Police du bouton
         startButton.setBackground(new Color(70, 130, 180));  // Couleur moderne
         startButton.setForeground(Color.WHITE);
         startButton.setPreferredSize(new Dimension(200, 50));
         startButton.addActionListener(e -> showMainPage());

         // Ajouter les composants à la page d'accueil
         panel.add(titleLabel, BorderLayout.NORTH);
         panel.add(descriptionLabel, BorderLayout.CENTER);
         panel.add(startButton, BorderLayout.SOUTH);

         return panel;
     }

     // Fonction pour afficher la page principale
     private void showMainPage() {
         // Vider la fenêtre actuelle
         getContentPane().removeAll();         
         // Créer et ajouter la vue principale avec les boutons et la zone de text        
        createMainPage();         
        // Rafraîchir la fenêtre         
        revalidate();          
        repaint();     
    }
    

     // Créer la page principale avec les boutons et la zone de texte
     private void createMainPage() {
         // Panneau principal avec BorderLayout
         setLayout(new BorderLayout());

        // Créer un panneau pour le logo et les boutons
         JPanel leftPanel = new JPanel();
         leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));  // Empilement vertical (logo puis boutons)

         // Ajouter un panneau avec FlowLayout pour le logo à gauche
         JPanel logoPanel = new JPanel();
         logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Aligner le logo à gauche

         // Ajouter l'image du logo dans le panneau
         JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
         logoPanel.add(logoLabel);  // Ajouter l'image du logo au panneau

         // Ajouter le panneau du logo à gauche
         leftPanel.add(logoPanel);

         // Créer un panneau pour les boutons
         JPanel buttonPanel = new JPanel();
         buttonPanel.setLayout(new GridLayout(7, 1, 10, 10));  // 7 boutons avec un espace entre

         // Créer les boutons
         JButton scenario1Button = createModernButton("\uD83D\uDD04 Scenario 1: Initialization and Redistribution");
         JButton scenario2Button = createModernButton("\u26A1 Scenario 2: Trigger Redistribution with a station Full");
         JButton scenario3Button = createModernButton("\u274C Scenario 3 - Trigger Redistribution with a station Empty");
         JButton scenario4Button = createModernButton("🛠️ Scenario 4 - Vehicle Broken then fix it");
         JButton scenario5Button = createModernButton("🚨 Scenario 5: Stolen Vehicle");
         JButton exitButton = createModernButton("Exit");
         JButton darkModeButton = createModernButton("Dark Mode");

         // Ajouter des action listeners aux boutons
         scenario1Button.addActionListener(e -> {
            try {
                scenario1();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
        
        scenario2Button.addActionListener(e -> {
            scenario2();  
        });

        scenario3Button.addActionListener(e -> {
            scenario3();  
        });

        scenario4Button.addActionListener(e -> {
            scenario4();  
        });

        scenario5Button.addActionListener(e -> {
            scenario5();  
        });
        
         exitButton.addActionListener(e -> System.exit(0));

         // Toggle dark mode
         darkModeButton.addActionListener(e -> toggleDarkMode());

         // Ajouter les boutons au panneau
         buttonPanel.add(scenario1Button);
         buttonPanel.add(scenario2Button);
         buttonPanel.add(scenario3Button);
         buttonPanel.add(scenario4Button);
         buttonPanel.add(scenario5Button);
         buttonPanel.add(exitButton);
         buttonPanel.add(darkModeButton);

         // Ajouter les boutons au panneau principal à gauche
         leftPanel.add(buttonPanel);

         // Ajouter le panneau de gauche (logo + boutons) à la fenêtre (à gauche)
         add(leftPanel, BorderLayout.WEST);

         // Créer la JTextArea et la rendre non modifiable
         textArea = new JTextArea();
         textArea.setEditable(false);
         textArea.setRows(10);  // Nombre de lignes visibles
         textArea.setColumns(30);  // Nombre de colonnes visibles

         // Ajouter la JTextArea dans un JScrollPane pour permettre le défilement
         JScrollPane scrollPane = new JScrollPane(textArea);
         add(scrollPane, BorderLayout.CENTER);
     }

     private JButton createModernButton(String text) {
         JButton button = new JButton(text);
         button.setFont(new Font("Arial", Font.BOLD, 14));  // Police moderne
         button.setBackground(new Color(70, 130, 180));  // Couleur bleu moderne
         button.setForeground(Color.WHITE);
         button.setFocusPainted(false);
         button.setBorder(BorderFactory.createLineBorder(new Color(30, 60, 100), 2));  // Bordure légère
         button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
         button.setPreferredSize(new Dimension(300, 50));  // Taille du bouton
         return button;
     }

     // Fonction pour activer/désactiver le mode sombre
     private void toggleDarkMode() {
         darkMode = !darkMode;
         Color backgroundColor, textColor, buttonBackgroundColor, buttonForegroundColor;

         if (darkMode) {
             backgroundColor = Color.DARK_GRAY;
             textColor = Color.WHITE;
             buttonBackgroundColor = Color.GRAY;
             buttonForegroundColor = Color.WHITE;
         } else {
             backgroundColor = Color.WHITE;
             textColor = Color.BLACK;
             buttonBackgroundColor = new Color(70, 130, 180);  // Bleu moderne
             buttonForegroundColor = Color.WHITE;
         }

         // Appliquer les couleurs en fonction du mode
         getContentPane().setBackground(backgroundColor);
         textArea.setBackground(backgroundColor);
         textArea.setForeground(textColor);

         // Changer les couleurs des boutons
         Component[] components = getContentPane().getComponents();
         for (Component component : components) {
             if (component instanceof JButton) {
                 JButton button = (JButton) component;
                 button.setBackground(buttonBackgroundColor);
                 button.setForeground(buttonForegroundColor);
             }
         }

         // Rafraîchir l'affichage
         revalidate();
         repaint();
     }
    
     // Scénarios
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

        System.out.println("\n\uD83D\uDD04 Scenario 1: Initialization and Redistribution");

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

        System.out.println("\n\u26A1 Scenario 2: Trigger Redistribution with a station Full");
        Station station1 = new Station(1, 3, controlCenter);
        Station station2 = new Station(2, 3, controlCenter);
        controlCenter.registerStation(station1);
        controlCenter.registerStation(station2);

        ClassicBike bike1 = new ClassicBike(101);
        ElectricBike bike2 = new ElectricBike(102);
        Scooter scooter1 = new Scooter(103);
        Scooter scooter2 = new Scooter(104);

        // Create a scheduled executor service
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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

        System.out.println("\n\u274C Scenario 3: Trigger Redistribution with a station Empty");
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

        System.out.println("\n🛠️ Scenario 4: Vehicle Out of Service");
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

        // Start the time manager and wait for 20 seconds
        timeManager.start(11000, latch);  // Run for 12 seconds

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
            latch.await();  // Block until the task finishes (12 seconds)
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
        }, 6, TimeUnit.SECONDS);
    
        // Set up a latch to wait until the task completes
        CountDownLatch latch = new CountDownLatch(1);
    
        // Start the time manager and wait for 12 seconds
        timeManager.start(17000, latch);  // Run for 12 seconds
    
        try {
            latch.await();  // Block until the task finishes (12 seconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    
        // Shutdown the scheduler
        scheduler.shutdown();
    
        System.out.println("\nScenario 5 finished. Returning to menu...");
    }


     public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
             MainV2 mainFrame = new MainV2();
             mainFrame.setVisible(true);
         });
    }
}
