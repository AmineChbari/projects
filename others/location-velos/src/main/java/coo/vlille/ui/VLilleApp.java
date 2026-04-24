package coo.vlille.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import coo.vlille.controlCenter.ControlCenter;
import coo.vlille.controlCenter.redistribution.RandomStrategy;
import coo.vlille.controlCenter.redistribution.RoundRobinStrategy;
import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.station.Station;
import coo.vlille.vehicle.Vehicle;
import coo.vlille.vehicle.bike.classicBike.ClassicBike;
import coo.vlille.vehicle.bike.electricBike.ElectricBike;
import coo.vlille.vehicle.decorator.BasketDecorator;
import coo.vlille.vehicle.decorator.LuggageRackDecorator;
import coo.vlille.vehicle.scooter.Scooter;
import coo.vlille.vehicle.state.Available;
import coo.vlille.vehicle.state.Broken;
import coo.vlille.vehicle.state.Stolen;
import coo.vlille.vehicle.state.util.VehicleException;

/**
 * Interface graphique interactive du système V'Lille.
 *
 * Toutes les illustrations (logo, véhicules, stations) sont dessinées en
 * Graphics2D vectoriel — aucune ressource externe, rendu net à toute résolution.
 */
public class VLilleApp extends JFrame {

    // ── Palette ────────────────────────────────────────────────────────────
    private static final Color C_BG        = new Color(14,  17,  32);
    private static final Color C_CARD      = new Color(24,  28,  47);
    private static final Color C_CARD_HI   = new Color(34,  39,  63);
    private static final Color C_BORDER    = new Color(55,  65,  100);
    private static final Color C_ACCENT    = new Color(124, 131, 253);
    private static final Color C_ACCENT_HI = new Color(156, 163, 255);
    private static final Color C_CORAL     = new Color(244, 114, 128);
    private static final Color C_GREEN     = new Color(74,  222, 168);
    private static final Color C_YELLOW    = new Color(251, 191, 36);
    private static final Color C_RED       = new Color(239, 88,  88);
    private static final Color C_TEXT      = new Color(232, 234, 246);
    private static final Color C_TEXT_DIM  = new Color(160, 170, 200);
    private static final Color C_MUTED     = new Color(88,  96,  130);

    private static final String[] STATION_NAMES = {
        "Gare Lille-Flandres", "Grand Place", "Vieux-Lille", "Euralille"
    };
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    // ── Domaine ────────────────────────────────────────────────────────────
    private ControlCenter center;
    private final List<Station> stations   = new ArrayList<>();
    private final List<Vehicle> vehiclesInUse = new ArrayList<>();
    private Station selectedStation;

    // ── Composants ────────────────────────────────────────────────────────
    private JPanel stationGrid;
    private JLabel stationTitle;
    private JLabel lblInUse, lblBroken, lblStolen;
    private JComboBox<String> strategyBox;
    private JTextArea logArea;
    private final DefaultListModel<Vehicle> vehicleListModel = new DefaultListModel<>();
    private final DefaultListModel<Vehicle> inUseListModel   = new DefaultListModel<>();
    private JList<Vehicle> vehicleList;
    private JList<Vehicle> inUseList;

    public VLilleApp() {
        initSystem();
        buildUI();
        setVisible(true);
        log("Système V'Lille initialisé — " + stations.size()
                + " stations, " + countTotalVehicles() + " véhicules");
    }

    // ══════════════════════════════════════════════════════════════════════
    // DOMAINE
    // ══════════════════════════════════════════════════════════════════════
    private void initSystem() {
        center = new ControlCenter();
        center.setRedistributionStrategy(new RoundRobinStrategy());
        for (int i = 1; i <= 4; i++) {
            Station s = new Station(i, 5, center);
            center.registerStation(s);
            stations.add(s);
        }
        putSafe(stations.get(0), new ClassicBike(101));
        putSafe(stations.get(0), new ElectricBike(102));
        putSafe(stations.get(0), new BasketDecorator(new ClassicBike(103)));
        putSafe(stations.get(1), new Scooter(104));
        putSafe(stations.get(1), new ElectricBike(105));
        putSafe(stations.get(1), new LuggageRackDecorator(new ClassicBike(106)));
        putSafe(stations.get(2), new ClassicBike(107));
        putSafe(stations.get(2), new Scooter(108));
        putSafe(stations.get(3), new ElectricBike(109));
        putSafe(stations.get(3), new ClassicBike(110));
        putSafe(stations.get(3), new Scooter(111));
        putSafe(stations.get(3), new BasketDecorator(new ElectricBike(112)));
    }

    private void putSafe(Station s, Vehicle v) {
        try { s.put(v); } catch (VehicleException ignored) { }
    }

    private int countTotalVehicles() {
        int t = 0; for (Station s : stations) t += s.totalBikes(); return t;
    }

    // ══════════════════════════════════════════════════════════════════════
    // UI
    // ══════════════════════════════════════════════════════════════════════
    private void buildUI() {
        setTitle("V'Lille — Réseau de location de véhicules");
        setSize(1240, 760);
        setMinimumSize(new Dimension(1040, 640));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout(12, 12));
        getRootPane().setBorder(new EmptyBorder(14, 16, 14, 16));

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildMain(),      BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        selectedStation = stations.get(0);
        refreshDetail();
        refreshStats();
    }

    // ── Header ────────────────────────────────────────────────────────────
    private JComponent buildHeader() {
        JPanel root = new RoundedPanel(18, C_CARD, C_BORDER);
        root.setLayout(new BorderLayout(14, 0));
        root.setBorder(new EmptyBorder(12, 18, 12, 18));

        // Logo + titre
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        JComponent logo = new JComponent() {
            { setPreferredSize(new Dimension(46, 46)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aa(g2);
                // disque dégradé
                GradientPaint gp = new GradientPaint(0, 0, C_ACCENT, 46, 46, C_CORAL);
                g2.setPaint(gp);
                g2.fillOval(0, 0, 46, 46);
                g2.setColor(Color.WHITE);
                VectorIcons.paintBike(g2, 7, 12, 32, C_BG);
                g2.dispose();
            }
        };
        left.add(logo);

        JPanel titleCol = new JPanel();
        titleCol.setLayout(new BoxLayout(titleCol, BoxLayout.Y_AXIS));
        titleCol.setOpaque(false);
        JLabel t1 = lbl("V'Lille", Font.BOLD, 22);
        t1.setForeground(C_TEXT);
        JLabel t2 = lbl("Réseau de location de vélos, scooters & véhicules électriques",
                Font.PLAIN, 12);
        t2.setForeground(C_TEXT_DIM);
        titleCol.add(t1);
        titleCol.add(t2);
        left.add(titleCol);
        root.add(left, BorderLayout.WEST);

        // Stats
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        stats.setOpaque(false);
        lblInUse  = pill("En circulation", "0", C_GREEN);
        lblBroken = pill("Hors service",   "0", C_YELLOW);
        lblStolen = pill("Volés",           "0", C_RED);
        stats.add(lblInUse);
        stats.add(lblBroken);
        stats.add(lblStolen);
        root.add(stats, BorderLayout.EAST);

        return root;
    }

    private JLabel pill(String label, String value, Color accent) {
        JLabel l = new JLabel(label + "  " + value) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aa(g2);
                g2.setColor(C_CARD_HI);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor((Color) getClientProperty("pill.accent"));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(accent);
        l.setBorder(new EmptyBorder(6, 14, 6, 14));
        l.setOpaque(false);
        l.putClientProperty("pill.accent", accent);
        return l;
    }

    private void updatePill(JLabel pill, String label, String value, Color accent) {
        pill.setText(label + "  " + value);
        pill.setForeground(accent);
    }

    // ── Zone principale ───────────────────────────────────────────────────
    private JComponent buildMain() {
        stationGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        stationGrid.setOpaque(false);
        stationGrid.setPreferredSize(new Dimension(440, 0));
        for (Station s : stations) stationGrid.add(buildStationCard(s));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                stationGrid, buildDetailPanel());
        split.setDividerLocation(445);
        split.setDividerSize(6);
        split.setBorder(null);
        split.setOpaque(false);
        return split;
    }

    // ── Carte station ─────────────────────────────────────────────────────
    private JPanel buildStationCard(Station s) {
        final boolean isSelected = (selectedStation != null && s.getId() == selectedStation.getId());
        final Color borderCol = isSelected ? C_ACCENT : C_BORDER;

        RoundedPanel card = new RoundedPanel(16, C_CARD, borderCol);
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setName("station_" + s.getId());

        // Header : pin + nom
        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        head.setOpaque(false);
        head.add(new IconView(28, 28) {
            @Override protected void draw(Graphics2D g2) {
                VectorIcons.paintPin(g2, 0, 0, 28, C_CORAL);
            }
        });
        JLabel name = lbl(STATION_NAMES[s.getId() - 1], Font.BOLD, 14);
        name.setForeground(C_TEXT);
        head.add(name);
        card.add(head, BorderLayout.NORTH);

        // Illustration : rack de vélos
        card.add(new StationRackView(s), BorderLayout.CENTER);

        // Pied : compteur + badge
        int filled = s.totalBikes();
        int cap    = s.getCapacity();
        Color badgeColor = s.isFull() ? C_CORAL : (s.isEmpty() ? C_YELLOW : C_GREEN);
        String badgeText = s.isFull() ? "COMPLÈTE" : (s.isEmpty() ? "VIDE" : "OK");

        JPanel foot = new JPanel(new BorderLayout());
        foot.setOpaque(false);
        JLabel count = lbl(filled + " / " + cap + " véhicule(s)", Font.BOLD, 12);
        count.setForeground(C_TEXT_DIM);
        foot.add(count, BorderLayout.WEST);

        JLabel badge = new JLabel(badgeText);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(badgeColor);
        badge.setBorder(new CompoundBorder(
                new RoundedBorder(10, badgeColor, 1, null),
                new EmptyBorder(2, 8, 2, 8)));
        foot.add(badge, BorderLayout.EAST);
        card.add(foot, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                selectedStation = s;
                refresh();
            }
            @Override public void mouseEntered(MouseEvent e) {
                if (!isSelected) { card.setBgColor(C_CARD_HI); card.repaint(); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!isSelected) { card.setBgColor(C_CARD); card.repaint(); }
            }
        });
        return card;
    }

    // ── Panneau détail ────────────────────────────────────────────────────
    private JComponent buildDetailPanel() {
        RoundedPanel p = new RoundedPanel(16, C_CARD, C_BORDER);
        p.setLayout(new BorderLayout(0, 10));
        p.setBorder(new EmptyBorder(16, 18, 16, 18));

        stationTitle = lbl("— Sélectionnez une station —", Font.BOLD, 16);
        stationTitle.setForeground(C_TEXT);
        p.add(stationTitle, BorderLayout.NORTH);

        JPanel lists = new JPanel(new GridLayout(1, 2, 12, 0));
        lists.setOpaque(false);
        lists.add(buildListPane("Véhicules en station", C_ACCENT, vehicleListModel, true));
        lists.add(buildListPane("Vos véhicules (en main)", C_GREEN, inUseListModel, false));
        p.add(lists, BorderLayout.CENTER);
        p.add(buildActions(), BorderLayout.SOUTH);
        return p;
    }

    private JComponent buildListPane(String title, Color accent,
                                     DefaultListModel<Vehicle> model, boolean isStationList) {
        JPanel pane = new JPanel(new BorderLayout(0, 6));
        pane.setOpaque(false);

        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        head.setOpaque(false);
        JComponent dot = new JComponent() {
            { setPreferredSize(new Dimension(8, 8)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
                g2.setColor(accent); g2.fillOval(0, 0, 8, 8); g2.dispose();
            }
        };
        head.add(dot);
        JLabel l = lbl(title, Font.BOLD, 12);
        l.setForeground(C_TEXT_DIM);
        head.add(l);
        pane.add(head, BorderLayout.NORTH);

        JList<Vehicle> list = new JList<>(model);
        list.setBackground(new Color(18, 22, 38));
        list.setForeground(C_TEXT);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        list.setSelectionBackground(C_ACCENT);
        list.setSelectionForeground(Color.WHITE);
        list.setFixedCellHeight(38);
        list.setCellRenderer(new VehicleRenderer());
        if (isStationList) vehicleList = list; else inUseList = list;

        JScrollPane sc = new JScrollPane(list);
        sc.setBorder(new RoundedBorder(10, C_BORDER, 1, null));
        sc.getViewport().setBackground(new Color(18, 22, 38));
        sc.setBackground(new Color(18, 22, 38));
        pane.add(sc, BorderLayout.CENTER);
        return pane;
    }

    private JComponent buildActions() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        GradientButton take   = new GradientButton("Prendre",        C_ACCENT,  C_ACCENT_HI);
        GradientButton ret    = new GradientButton("Rendre ici",     C_GREEN,   new Color(120, 240, 190));
        GradientButton repair = new GradientButton("Réparer station", C_YELLOW, new Color(255, 210, 80));
        take.addActionListener(e -> actionTake());
        ret.addActionListener(e -> actionReturn());
        repair.addActionListener(e -> actionRepair());
        bar.add(take); bar.add(ret); bar.add(repair);
        return bar;
    }

    // ── Barre de contrôle ─────────────────────────────────────────────────
    private JComponent buildBottomBar() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setOpaque(false);

        RoundedPanel ctrl = new RoundedPanel(14, C_CARD, C_BORDER);
        ctrl.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        ctrl.setBorder(new EmptyBorder(6, 14, 6, 14));

        JLabel l = lbl("Centre de contrôle", Font.BOLD, 12);
        l.setForeground(C_TEXT_DIM);

        strategyBox = new JComboBox<>(new String[]{"RoundRobin", "Random"});
        strategyBox.setBackground(C_CARD_HI);
        strategyBox.setForeground(C_TEXT);
        strategyBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        strategyBox.addActionListener(e -> {
            if ("Random".equals(strategyBox.getSelectedItem())) {
                center.setRedistributionStrategy(new RandomStrategy());
                log("Stratégie → Random");
            } else {
                center.setRedistributionStrategy(new RoundRobinStrategy());
                log("Stratégie → RoundRobin");
            }
        });

        GradientButton redist = new GradientButton("Redistribuer",  C_ACCENT, C_ACCENT_HI);
        GradientButton stolen = new GradientButton("Vérifier vols", C_CORAL,  new Color(255, 150, 160));
        redist.addActionListener(e -> actionRedistribute());
        stolen.addActionListener(e -> actionCheckStolen());

        ctrl.add(l);
        ctrl.add(strategyBox);
        ctrl.add(redist);
        ctrl.add(stolen);
        root.add(ctrl, BorderLayout.NORTH);

        logArea = new JTextArea(5, 0);
        logArea.setEditable(false);
        logArea.setBackground(new Color(10, 14, 28));
        logArea.setForeground(new Color(140, 225, 190));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        JScrollPane sc = new JScrollPane(logArea);
        sc.setBorder(new RoundedBorder(12, C_BORDER, 1, null));
        sc.setPreferredSize(new Dimension(0, 110));
        root.add(sc, BorderLayout.CENTER);
        return root;
    }

    // ══════════════════════════════════════════════════════════════════════
    // ACTIONS
    // ══════════════════════════════════════════════════════════════════════
    private void actionTake() {
        Vehicle v = vehicleList.getSelectedValue();
        if (v == null) { showMsg("Sélectionnez un véhicule dans la station."); return; }
        try {
            selectedStation.take(v);
            vehiclesInUse.add(v);
            log("✓ Véhicule #" + v.getId() + " pris à "
                    + STATION_NAMES[selectedStation.getId() - 1]);
            refresh();
        } catch (VehicleException ex) {
            log("✗ Impossible de prendre #" + v.getId() + " : " + ex.getMessage());
        }
    }

    private void actionReturn() {
        Vehicle v = inUseList.getSelectedValue();
        if (v == null) { showMsg("Sélectionnez un véhicule à rendre."); return; }
        if (selectedStation == null) { showMsg("Choisissez une station."); return; }
        try {
            selectedStation.put(v);
            vehiclesInUse.remove(v);
            log("↩ Véhicule #" + v.getId() + " rendu à "
                    + STATION_NAMES[selectedStation.getId() - 1]);
            refresh();
        } catch (VehicleException ex) {
            log("✗ Impossible de rendre #" + v.getId() + " : " + ex.getMessage());
        }
    }

    private void actionRepair() {
        if (selectedStation == null) return;
        ConcreteRepairer repairer = new ConcreteRepairer();
        log("🔧 Réparation en cours sur "
                + STATION_NAMES[selectedStation.getId() - 1] + "...");
        final Station target = selectedStation;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() {
                target.checkReparation(repairer); return null;
            }
            @Override protected void done() {
                log("✓ Réparations terminées sur " + STATION_NAMES[target.getId() - 1]);
                refresh();
            }
        }.execute();
    }

    private void actionRedistribute() {
        try {
            center.checkAllRedistribute();
            log("⚖ Redistribution effectuée (" + strategyBox.getSelectedItem() + ")");
            refresh();
        } catch (VehicleException ex) {
            log("✗ Redistribution échouée : " + ex.getMessage());
        }
    }

    private void actionCheckStolen() {
        center.checkAllStolen();
        log("🚨 Vérification des vols effectuée");
        refresh();
    }

    // ══════════════════════════════════════════════════════════════════════
    // REFRESH
    // ══════════════════════════════════════════════════════════════════════
    private void refresh() {
        SwingUtilities.invokeLater(() -> {
            refreshStationGrid();
            refreshDetail();
            refreshStats();
        });
    }

    private void refreshStationGrid() {
        stationGrid.removeAll();
        for (Station s : stations) stationGrid.add(buildStationCard(s));
        stationGrid.revalidate();
        stationGrid.repaint();
    }

    private void refreshDetail() {
        if (selectedStation == null) return;
        stationTitle.setText(STATION_NAMES[selectedStation.getId() - 1]
                + "   ·   " + selectedStation.totalBikes()
                + " / " + selectedStation.getCapacity() + " véhicules");
        vehicleListModel.clear();
        for (Vehicle v : selectedStation.getAllVehicles()) vehicleListModel.addElement(v);
        inUseListModel.clear();
        for (Vehicle v : vehiclesInUse) inUseListModel.addElement(v);
    }

    private void refreshStats() {
        long broken = 0, stolen = 0;
        for (Station s : stations)
            for (Vehicle v : s.getAllVehicles()) {
                if (v.getState() instanceof Broken) broken++;
                if (v.getState() instanceof Stolen) stolen++;
            }
        updatePill(lblInUse,  "En circulation", String.valueOf(vehiclesInUse.size()), C_GREEN);
        updatePill(lblBroken, "Hors service",   String.valueOf(broken),               C_YELLOW);
        updatePill(lblStolen, "Volés",           String.valueOf(stolen),               C_RED);
    }

    // ══════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════
    private void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + LocalTime.now().format(TIME_FMT) + "]  " + msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel lbl(String text, int style, int size) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(C_TEXT);
        return l;
    }

    private static void aa(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
    }

    // ══════════════════════════════════════════════════════════════════════
    // CELL RENDERER (avec icône vectorielle)
    // ══════════════════════════════════════════════════════════════════════
    private class VehicleRenderer extends JPanel implements ListCellRenderer<Vehicle> {
        private final JComponent iconComp;
        private final JLabel lId, lType, lState;
        private Vehicle current;
        VehicleRenderer() {
            setLayout(new BorderLayout(8, 0));
            setBorder(new EmptyBorder(4, 10, 4, 10));
            iconComp = new JComponent() {
                { setPreferredSize(new Dimension(34, 30)); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
                    paintVehicleIcon(g2, current, 34, 30);
                    g2.dispose();
                }
            };
            add(iconComp, BorderLayout.WEST);

            JPanel txt = new JPanel(); txt.setOpaque(false);
            txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
            lId    = new JLabel(); lId.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lType  = new JLabel(); lType.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            txt.add(lId); txt.add(lType);
            add(txt, BorderLayout.CENTER);

            lState = new JLabel();
            lState.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lState.setBorder(new EmptyBorder(0, 8, 0, 2));
            add(lState, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Vehicle> list, Vehicle v,
                int index, boolean selected, boolean focus) {
            current = v;
            Color fg     = selected ? Color.WHITE : C_TEXT;
            Color dimfg  = selected ? new Color(230, 232, 245) : C_TEXT_DIM;
            setBackground(selected ? C_ACCENT : new Color(18, 22, 38));
            setOpaque(true);

            lId.setText("#" + v.getId());
            lId.setForeground(fg);
            lType.setText(typeName(v));
            lType.setForeground(dimfg);

            Color stCol; String stTxt;
            if (v.getState() instanceof Available) { stCol = C_GREEN;  stTxt = "● DISPO"; }
            else if (v.getState() instanceof Broken)  { stCol = C_YELLOW; stTxt = "● CASSÉ"; }
            else if (v.getState() instanceof Stolen)  { stCol = C_RED;    stTxt = "● VOLÉ";  }
            else                                      { stCol = C_ACCENT; stTxt = "● UTIL."; }
            lState.setText(stTxt);
            lState.setForeground(selected ? Color.WHITE : stCol);
            return this;
        }

        private String typeName(Vehicle v) {
            String cn = v.getClass().getSimpleName();
            switch (cn) {
                case "ClassicBike":          return "Vélo classique";
                case "ElectricBike":         return "Vélo électrique";
                case "Scooter":              return "Scooter";
                case "BasketDecorator":      return "Vélo avec panier";
                case "LuggageRackDecorator": return "Vélo avec porte-bagages";
                default: return cn;
            }
        }
    }

    private void paintVehicleIcon(Graphics2D g2, Vehicle v, int w, int h) {
        if (v == null) return;
        String cn = v.getClass().getSimpleName();
        Color main = C_ACCENT_HI;
        if ("ElectricBike".equals(cn)) main = new Color(140, 230, 200);
        else if ("Scooter".equals(cn)) main = C_CORAL;
        else if ("BasketDecorator".equals(cn)) main = new Color(250, 200, 120);
        else if ("LuggageRackDecorator".equals(cn)) main = new Color(200, 180, 255);

        if ("Scooter".equals(cn)) VectorIcons.paintScooter(g2, 2, 4, w - 4, main);
        else if ("ElectricBike".equals(cn)) VectorIcons.paintElectricBike(g2, 2, 4, w - 4, main);
        else if ("BasketDecorator".equals(cn)) VectorIcons.paintBikeWithBasket(g2, 2, 4, w - 4, main);
        else if ("LuggageRackDecorator".equals(cn)) VectorIcons.paintBikeWithRack(g2, 2, 4, w - 4, main);
        else VectorIcons.paintBike(g2, 2, 4, w - 4, main);
    }

    // ══════════════════════════════════════════════════════════════════════
    // COMPOSANTS CUSTOM (panneaux, bordures, boutons)
    // ══════════════════════════════════════════════════════════════════════

    /** Panneau avec coins arrondis, fond et bordure légère. */
    static class RoundedPanel extends JPanel {
        private int radius;
        private Color bg;
        private Color border;
        RoundedPanel(int radius, Color bg, Color border) {
            this.radius = radius; this.bg = bg; this.border = border;
            setOpaque(false);
        }
        void setBgColor(Color c) { this.bg = c; }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
            g2.dispose();
        }
    }

    /** Bordure arrondie avec remplissage optionnel. */
    static class RoundedBorder implements Border {
        private final int radius; private final Color color; private final int thickness;
        private final Color fill;
        RoundedBorder(int radius, Color color, int thickness, Color fill) {
            this.radius = radius; this.color = color; this.thickness = thickness; this.fill = fill;
        }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 1, thickness + 1, thickness + 1, thickness + 1);
        }
        @Override public boolean isBorderOpaque() { return false; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            if (fill != null) {
                g2.setColor(fill);
                g2.fillRoundRect(x, y, w, h, radius, radius);
            }
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
    }

    /** Bouton arrondi avec dégradé et état hover. */
    class GradientButton extends JButton {
        private final Color c1, c2;
        private boolean hover = false;
        GradientButton(String text, Color c1, Color c2) {
            super(text);
            this.c1 = c1; this.c2 = c2;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(new EmptyBorder(8, 18, 8, 18));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            Color a = hover ? c2 : c1;
            Color b = hover ? c1 : c2;
            g2.setPaint(new GradientPaint(0, 0, a, 0, getHeight(), b));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Vue abstraite pour dessiner une icône vectorielle dans un composant. */
    static abstract class IconView extends JComponent {
        IconView(int w, int h) { setPreferredSize(new Dimension(w, h)); }
        protected abstract void draw(Graphics2D g2);
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            draw(g2);
            g2.dispose();
        }
    }

    /** Rack de la station : dessin d'un mur + emplacements de vélos. */
    class StationRackView extends JComponent {
        private final Station station;
        StationRackView(Station s) {
            this.station = s;
            setPreferredSize(new Dimension(0, 95));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            int w = getWidth(), h = getHeight();
            int cap = station.getCapacity();
            // sol
            g2.setColor(new Color(40, 46, 72));
            g2.fillRoundRect(4, h - 10, w - 8, 5, 3, 3);

            // slots
            int slotW = (w - 8) / cap;
            List<Vehicle> vs = station.getAllVehicles();
            for (int i = 0; i < cap; i++) {
                int x = 4 + i * slotW;
                // poteau
                g2.setColor(new Color(55, 62, 95));
                g2.fillRoundRect(x + slotW / 2 - 1, 12, 2, h - 20, 1, 1);
                // véhicule s'il existe
                if (i < vs.size()) {
                    Vehicle v = vs.get(i);
                    Color tint = C_ACCENT_HI;
                    if (v.getState() instanceof Broken) tint = C_YELLOW;
                    else if (v.getState() instanceof Stolen) tint = C_MUTED;
                    paintVehicleMini(g2, v, x + 2, 18, slotW - 4, tint);
                }
            }
            g2.dispose();
        }
        private void paintVehicleMini(Graphics2D g2, Vehicle v, int x, int y, int w, Color tint) {
            String cn = v.getClass().getSimpleName();
            if ("Scooter".equals(cn))                 VectorIcons.paintScooter(g2, x, y, w, tint);
            else if ("ElectricBike".equals(cn))       VectorIcons.paintElectricBike(g2, x, y, w, tint);
            else if ("BasketDecorator".equals(cn))    VectorIcons.paintBikeWithBasket(g2, x, y, w, tint);
            else if ("LuggageRackDecorator".equals(cn)) VectorIcons.paintBikeWithRack(g2, x, y, w, tint);
            else                                      VectorIcons.paintBike(g2, x, y, w, tint);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // ICÔNES VECTORIELLES
    // ══════════════════════════════════════════════════════════════════════
    static class VectorIcons {

        /** Pin de localisation (goutte). */
        static void paintPin(Graphics2D g2, int x, int y, int size, Color c) {
            g2.setColor(c);
            Path2D p = new Path2D.Double();
            double cx = x + size / 2.0;
            double cy = y + size * 0.40;
            double r  = size * 0.32;
            p.moveTo(cx, y + size);
            p.curveTo(cx - size * 0.45, y + size * 0.65,
                     cx - r, y + size * 0.55,
                     cx - r, cy);
            p.append(new Arc2D.Double(cx - r, cy - r, 2 * r, 2 * r, 180, -180, Arc2D.OPEN), true);
            p.curveTo(cx + r, y + size * 0.55,
                     cx + size * 0.45, y + size * 0.65,
                     cx, y + size);
            g2.fill(p);
            g2.setColor(new Color(255, 255, 255, 230));
            double dr = size * 0.13;
            g2.fill(new Ellipse2D.Double(cx - dr, cy - dr, 2 * dr, 2 * dr));
        }

        /** Vélo classique : deux roues + cadre triangle + guidon. */
        static void paintBike(Graphics2D g2, int x, int y, int size, Color c) {
            float s = size / 40f;
            Stroke old = g2.getStroke();
            g2.setStroke(new BasicStroke(Math.max(1.5f, 2f * s), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(c);
            double wheelR = 6 * s;
            double ly = y + 22 * s;
            double wl = x + 8 * s, wr = x + 32 * s;
            // roues
            g2.draw(new Ellipse2D.Double(wl - wheelR, ly - wheelR, 2 * wheelR, 2 * wheelR));
            g2.draw(new Ellipse2D.Double(wr - wheelR, ly - wheelR, 2 * wheelR, 2 * wheelR));
            // cadre (triangle)
            Path2D frame = new Path2D.Double();
            frame.moveTo(wl, ly);
            frame.lineTo(x + 18 * s, y + 12 * s);
            frame.lineTo(wr, ly);
            frame.lineTo(x + 22 * s, y + 12 * s);
            frame.lineTo(wl, ly);
            g2.draw(frame);
            // selle
            g2.draw(new Line2D.Double(x + 16 * s, y + 10 * s, x + 20 * s, y + 10 * s));
            // guidon
            g2.draw(new Line2D.Double(x + 22 * s, y + 12 * s, x + 26 * s, y + 8 * s));
            g2.draw(new Line2D.Double(x + 24 * s, y + 8 * s, x + 28 * s, y + 9 * s));
            g2.setStroke(old);
        }

        /** Vélo électrique : vélo + éclair. */
        static void paintElectricBike(Graphics2D g2, int x, int y, int size, Color c) {
            paintBike(g2, x, y, size, c);
            float s = size / 40f;
            g2.setColor(new Color(255, 220, 80));
            Path2D bolt = new Path2D.Double();
            double cx = x + 18 * s, cy = y + 16 * s;
            bolt.moveTo(cx - 2 * s, cy - 4 * s);
            bolt.lineTo(cx + 1 * s, cy - 4 * s);
            bolt.lineTo(cx - 1 * s, cy);
            bolt.lineTo(cx + 2 * s, cy);
            bolt.lineTo(cx - 1 * s, cy + 5 * s);
            bolt.lineTo(cx,          cy + 1 * s);
            bolt.lineTo(cx - 2 * s, cy + 1 * s);
            bolt.closePath();
            g2.fill(bolt);
        }

        /** Vélo + panier devant. */
        static void paintBikeWithBasket(Graphics2D g2, int x, int y, int size, Color c) {
            paintBike(g2, x, y, size, c);
            float s = size / 40f;
            g2.setColor(new Color(240, 190, 120));
            g2.setStroke(new BasicStroke(Math.max(1f, 1.2f * s)));
            Path2D basket = new Path2D.Double();
            basket.moveTo(x + 26 * s, y + 10 * s);
            basket.lineTo(x + 32 * s, y + 10 * s);
            basket.lineTo(x + 31 * s, y + 16 * s);
            basket.lineTo(x + 27 * s, y + 16 * s);
            basket.closePath();
            g2.fill(basket);
            g2.setColor(new Color(170, 110, 40));
            g2.draw(basket);
        }

        /** Vélo + porte-bagages arrière. */
        static void paintBikeWithRack(Graphics2D g2, int x, int y, int size, Color c) {
            paintBike(g2, x, y, size, c);
            float s = size / 40f;
            g2.setColor(new Color(170, 170, 230));
            g2.setStroke(new BasicStroke(Math.max(1f, 1.5f * s)));
            // petit rectangle au-dessus de la roue arrière
            g2.fill(new Rectangle2D.Double(x + 4 * s, y + 13 * s, 8 * s, 3 * s));
            g2.setColor(new Color(100, 100, 160));
            g2.draw(new Rectangle2D.Double(x + 4 * s, y + 13 * s, 8 * s, 3 * s));
        }

        /** Scooter : plateau + une grande roue avant + siège. */
        static void paintScooter(Graphics2D g2, int x, int y, int size, Color c) {
            float s = size / 40f;
            Stroke old = g2.getStroke();
            g2.setStroke(new BasicStroke(Math.max(1.5f, 2f * s), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(c);
            double ly = y + 22 * s;
            double wlR = 6 * s, wrR = 6 * s;
            double wl = x + 10 * s, wr = x + 30 * s;
            // roues
            g2.draw(new Ellipse2D.Double(wl - wlR, ly - wlR, 2 * wlR, 2 * wlR));
            g2.draw(new Ellipse2D.Double(wr - wrR, ly - wrR, 2 * wrR, 2 * wrR));
            // plateau
            Path2D body = new Path2D.Double();
            body.moveTo(x + 6 * s,  y + 16 * s);
            body.lineTo(x + 28 * s, y + 16 * s);
            body.lineTo(x + 32 * s, y + 20 * s);
            body.lineTo(x + 10 * s, y + 20 * s);
            body.closePath();
            g2.fill(body);
            // siège
            g2.setColor(c.darker());
            g2.fill(new RoundRectangle2D.Double(x + 18 * s, y + 10 * s, 10 * s, 4 * s, 3 * s, 3 * s));
            // guidon
            g2.setColor(c);
            g2.draw(new Line2D.Double(x + 8 * s, y + 16 * s, x + 6 * s, y + 6 * s));
            g2.draw(new Line2D.Double(x + 3 * s, y + 6 * s, x + 9 * s, y + 7 * s));
            g2.setStroke(old);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // POINT D'ENTRÉE
    // ══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) { }
        SwingUtilities.invokeLater(VLilleApp::new);
    }
}
