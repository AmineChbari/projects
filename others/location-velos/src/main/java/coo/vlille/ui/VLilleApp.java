package coo.vlille.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
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
 * Permet de prendre et rendre des véhicules, déclencher la redistribution,
 * lancer les réparations et vérifier les vols.
 */
public class VLilleApp extends JFrame {

    // ── Palette de couleurs ────────────────────────────────────────────────
    private static final Color C_BG     = new Color(18,  18,  30);
    private static final Color C_CARD   = new Color(28,  28,  45);
    private static final Color C_ACCENT = new Color(99,  102, 241);
    private static final Color C_GREEN  = new Color(52,  211, 153);
    private static final Color C_RED    = new Color(239, 68,  68);
    private static final Color C_YELLOW = new Color(251, 191, 36);
    private static final Color C_TEXT   = new Color(229, 231, 235);
    private static final Color C_MUTED  = new Color(107, 114, 128);

    // ── Noms des stations ──────────────────────────────────────────────────
    private static final String[] STATION_NAMES = {
        "Gare Lille-Flandres", "Grand Place", "Vieux-Lille", "Euralille"
    };

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    // ── Domaine ────────────────────────────────────────────────────────────
    private ControlCenter   center;
    private List<Station>   stations      = new ArrayList<>();
    private List<Vehicle>   vehiclesInUse = new ArrayList<>();
    private Station         selectedStation;

    // ── Composants UI ─────────────────────────────────────────────────────
    private JPanel          stationGrid;
    private JLabel          stationTitle;
    private JLabel          lblInUse, lblBroken, lblStolen;
    private JComboBox<String> strategyBox;
    private JTextArea       logArea;

    private DefaultListModel<Vehicle> vehicleListModel = new DefaultListModel<>();
    private DefaultListModel<Vehicle> inUseListModel   = new DefaultListModel<>();
    private JList<Vehicle>  vehicleList;
    private JList<Vehicle>  inUseList;

    // ── Constructeur ───────────────────────────────────────────────────────
    public VLilleApp() {
        initSystem();
        buildUI();
        setVisible(true);
        log("Système V'Lille initialisé — " + stations.size()
                + " stations, " + countTotalVehicles() + " véhicules.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // INITIALISATION DU SYSTÈME
    // ══════════════════════════════════════════════════════════════════════

    private void initSystem() {
        center = new ControlCenter();
        center.setRedistributionStrategy(new RoundRobinStrategy());

        for (int i = 1; i <= 4; i++) {
            Station s = new Station(i, 5, center);
            center.registerStation(s);
            stations.add(s);
        }

        // Station 1 — Gare Lille-Flandres
        putSafe(stations.get(0), new ClassicBike(101));
        putSafe(stations.get(0), new ElectricBike(102));
        putSafe(stations.get(0), new BasketDecorator(new ClassicBike(103)));

        // Station 2 — Grand Place
        putSafe(stations.get(1), new Scooter(104));
        putSafe(stations.get(1), new ElectricBike(105));
        putSafe(stations.get(1), new LuggageRackDecorator(new ClassicBike(106)));

        // Station 3 — Vieux-Lille
        putSafe(stations.get(2), new ClassicBike(107));
        putSafe(stations.get(2), new Scooter(108));

        // Station 4 — Euralille
        putSafe(stations.get(3), new ElectricBike(109));
        putSafe(stations.get(3), new ClassicBike(110));
        putSafe(stations.get(3), new Scooter(111));
        putSafe(stations.get(3), new BasketDecorator(new ElectricBike(112)));
    }

    private void putSafe(Station s, Vehicle v) {
        try { s.put(v); } catch (VehicleException ignored) { }
    }

    private int countTotalVehicles() {
        return stations.stream().mapToInt(Station::totalBikes).sum();
    }

    // ══════════════════════════════════════════════════════════════════════
    // CONSTRUCTION DE L'INTERFACE
    // ══════════════════════════════════════════════════════════════════════

    private void buildUI() {
        setTitle("V'Lille — Réseau de location de véhicules");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 580));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));

        add(buildTitleBar(),   BorderLayout.NORTH);
        add(buildMainArea(),   BorderLayout.CENTER);
        add(buildBottomBar(),  BorderLayout.SOUTH);

        selectedStation = stations.get(0);
        refreshDetail();
    }

    // ── Barre de titre ────────────────────────────────────────────────────

    private JPanel buildTitleBar() {
        JPanel p = card();
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel title = lbl("🚲  V'Lille — Réseau de location de véhicules", Font.BOLD, 18);
        title.setForeground(C_ACCENT);
        p.add(title, BorderLayout.WEST);

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        stats.setOpaque(false);
        lblInUse  = lbl("En circulation : 0", Font.PLAIN, 12);
        lblBroken = lbl("Hors service : 0",   Font.PLAIN, 12);
        lblStolen = lbl("Volés : 0",           Font.PLAIN, 12);
        lblInUse.setForeground(C_GREEN);
        lblBroken.setForeground(C_YELLOW);
        lblStolen.setForeground(C_RED);
        stats.add(lblInUse);
        stats.add(lblBroken);
        stats.add(lblStolen);
        p.add(stats, BorderLayout.EAST);

        return p;
    }

    // ── Zone principale ───────────────────────────────────────────────────

    private JSplitPane buildMainArea() {
        // Grille des stations (gauche)
        stationGrid = new JPanel(new GridLayout(2, 2, 8, 8));
        stationGrid.setOpaque(false);
        stationGrid.setPreferredSize(new Dimension(370, 0));
        for (Station s : stations) {
            stationGrid.add(buildStationCard(s));
        }

        // Panneau de détail (droite)
        JPanel detail = buildDetailPanel();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                stationGrid, detail);
        split.setDividerLocation(375);
        split.setDividerSize(4);
        split.setBorder(null);
        split.setOpaque(false);
        split.setBackground(C_BG);
        return split;
    }

    // ── Carte d'une station ───────────────────────────────────────────────

    private JPanel buildStationCard(Station s) {
        JPanel card = card();
        card.setLayout(new BorderLayout(6, 6));
        card.setBorder(new CompoundBorder(
            new LineBorder(C_ACCENT, 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setName("station_" + s.getId());

        JLabel name = lbl("📍 " + STATION_NAMES[s.getId() - 1], Font.BOLD, 13);
        name.setForeground(C_TEXT);
        card.add(name, BorderLayout.NORTH);

        card.add(buildGauge(s.totalBikes(), s.getCapacity()), BorderLayout.CENTER);

        int filled = s.totalBikes();
        int cap    = s.getCapacity();
        Color countColor = s.isFull() ? C_RED : s.isEmpty() ? C_YELLOW : C_GREEN;
        JLabel count = lbl(filled + " / " + cap + " véhicule(s)", Font.BOLD, 12);
        count.setForeground(countColor);
        card.add(count, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedStation = s;
                refreshDetail();
                highlightCard(s);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(C_CARD.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(C_CARD);
            }
        });

        return card;
    }

    private JPanel buildGauge(int filled, int cap) {
        JPanel g = new JPanel(new GridLayout(1, cap, 2, 0));
        g.setOpaque(false);
        for (int i = 0; i < cap; i++) {
            JPanel slot = new JPanel();
            slot.setPreferredSize(new Dimension(14, 12));
            slot.setBackground(i < filled ? C_ACCENT : C_MUTED.darker());
            slot.setBorder(new LineBorder(C_BG, 1));
            g.add(slot);
        }
        return g;
    }

    private void highlightCard(Station selected) {
        for (Component c : stationGrid.getComponents()) {
            if (c instanceof JPanel) {
                JPanel card = (JPanel) c;
                boolean sel = ("station_" + selected.getId()).equals(card.getName());
                card.setBorder(new CompoundBorder(
                    new LineBorder(sel ? C_GREEN : C_ACCENT, sel ? 2 : 1, true),
                    new EmptyBorder(10, 12, 10, 12)
                ));
            }
        }
    }

    // ── Panneau de détail ─────────────────────────────────────────────────

    private JPanel buildDetailPanel() {
        JPanel p = card();
        p.setLayout(new BorderLayout(0, 8));
        p.setBorder(new EmptyBorder(12, 12, 12, 12));

        stationTitle = lbl("— Sélectionnez une station —", Font.BOLD, 15);
        stationTitle.setForeground(C_ACCENT);
        p.add(stationTitle, BorderLayout.NORTH);

        // Deux listes côte à côte
        JPanel lists = new JPanel(new GridLayout(1, 2, 10, 0));
        lists.setOpaque(false);

        JPanel leftPane = new JPanel(new BorderLayout(0, 4));
        leftPane.setOpaque(false);
        leftPane.add(lbl("Véhicules en station :", Font.BOLD, 12), BorderLayout.NORTH);
        vehicleList = new JList<>(vehicleListModel);
        styleList(vehicleList);
        vehicleList.setCellRenderer(new VehicleRenderer());
        JScrollPane ls = new JScrollPane(vehicleList);
        ls.setBorder(new LineBorder(C_ACCENT.darker(), 1));
        ls.getViewport().setBackground(new Color(22, 22, 38));
        leftPane.add(ls, BorderLayout.CENTER);
        lists.add(leftPane);

        JPanel rightPane = new JPanel(new BorderLayout(0, 4));
        rightPane.setOpaque(false);
        rightPane.add(lbl("Vos véhicules (en main) :", Font.BOLD, 12), BorderLayout.NORTH);
        inUseList = new JList<>(inUseListModel);
        styleList(inUseList);
        inUseList.setCellRenderer(new VehicleRenderer());
        JScrollPane rs = new JScrollPane(inUseList);
        rs.setBorder(new LineBorder(C_GREEN.darker(), 1));
        rs.getViewport().setBackground(new Color(22, 22, 38));
        rightPane.add(rs, BorderLayout.CENTER);
        lists.add(rightPane);

        p.add(lists, BorderLayout.CENTER);
        p.add(buildActionButtons(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildActionButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setOpaque(false);

        JButton btnTake   = btn("🚲 Prendre",   C_ACCENT);
        JButton btnReturn = btn("↩ Rendre ici", C_GREEN);
        JButton btnRepair = btn("🔧 Réparer station", C_YELLOW);

        btnTake.setToolTipText("Prendre le véhicule sélectionné dans la liste de gauche");
        btnReturn.setToolTipText("Rendre le véhicule sélectionné (liste de droite) dans cette station");
        btnRepair.setToolTipText("Réparer les véhicules hors-service de la station sélectionnée");

        btnTake.addActionListener(e -> actionTake());
        btnReturn.addActionListener(e -> actionReturn());
        btnRepair.addActionListener(e -> actionRepair());

        bar.add(btnTake);
        bar.add(btnReturn);
        bar.add(btnRepair);
        return bar;
    }

    // ── Barre de contrôle (bas) ───────────────────────────────────────────

    private JPanel buildBottomBar() {
        JPanel p = new JPanel(new BorderLayout(8, 4));
        p.setOpaque(false);

        // Ligne de contrôle
        JPanel ctrl = card();
        ctrl.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        ctrl.setBorder(new EmptyBorder(2, 10, 2, 10));

        JLabel lbl = lbl("Centre de contrôle :", Font.BOLD, 12);
        lbl.setForeground(C_MUTED);

        strategyBox = new JComboBox<>(new String[]{"RoundRobin", "Random"});
        strategyBox.setBackground(C_CARD);
        strategyBox.setForeground(C_TEXT);
        strategyBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton btnRedist = btn("⚖ Redistribuer",   C_ACCENT);
        JButton btnStolen = btn("🚨 Vérifier vols", C_RED);

        strategyBox.addActionListener(e -> {
            if ("Random".equals(strategyBox.getSelectedItem())) {
                center.setRedistributionStrategy(new RandomStrategy());
                log("Stratégie → Random");
            } else {
                center.setRedistributionStrategy(new RoundRobinStrategy());
                log("Stratégie → RoundRobin");
            }
        });
        btnRedist.addActionListener(e -> actionRedistribute());
        btnStolen.addActionListener(e -> actionCheckStolen());

        ctrl.add(lbl);
        ctrl.add(strategyBox);
        ctrl.add(btnRedist);
        ctrl.add(btnStolen);
        p.add(ctrl, BorderLayout.NORTH);

        // Zone de log
        logArea = new JTextArea(4, 0);
        logArea.setEditable(false);
        logArea.setBackground(new Color(10, 10, 20));
        logArea.setForeground(new Color(110, 231, 183));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        logArea.setBorder(new EmptyBorder(4, 8, 4, 8));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(new LineBorder(C_CARD, 1));
        scroll.setPreferredSize(new Dimension(0, 90));
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // ACTIONS
    // ══════════════════════════════════════════════════════════════════════

    private void actionTake() {
        Vehicle v = vehicleList.getSelectedValue();
        if (v == null) {
            showMsg("Sélectionnez un véhicule dans la liste de gauche.");
            return;
        }
        try {
            selectedStation.take(v);
            vehiclesInUse.add(v);
            inUseListModel.addElement(v);
            log("✅ Véhicule #" + v.getId() + " pris à "
                    + STATION_NAMES[selectedStation.getId() - 1]);
            refresh();
        } catch (VehicleException ex) {
            log("⚠ Impossible de prendre #" + v.getId() + " : " + ex.getMessage());
        }
    }

    private void actionReturn() {
        Vehicle v = inUseList.getSelectedValue();
        if (v == null) {
            showMsg("Sélectionnez un véhicule dans votre liste (à droite).");
            return;
        }
        if (selectedStation == null) {
            showMsg("Sélectionnez d'abord une station de retour.");
            return;
        }
        try {
            selectedStation.put(v);
            vehiclesInUse.remove(v);
            inUseListModel.removeElement(v);
            log("↩ Véhicule #" + v.getId() + " rendu à "
                    + STATION_NAMES[selectedStation.getId() - 1]);
            refresh();
        } catch (VehicleException ex) {
            log("⚠ Impossible de rendre #" + v.getId() + " : " + ex.getMessage());
        }
    }

    private void actionRepair() {
        if (selectedStation == null) return;
        ConcreteRepairer repairer = new ConcreteRepairer();
        log("🔧 Réparation en cours sur "
                + STATION_NAMES[selectedStation.getId() - 1] + "...");
        Station target = selectedStation;
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                target.checkReparation(repairer);
                return null;
            }
            @Override
            protected void done() {
                log("✅ Réparations terminées sur "
                        + STATION_NAMES[target.getId() - 1]);
                refresh();
            }
        }.execute();
    }

    private void actionRedistribute() {
        try {
            center.checkAllRedistribute();
            log("⚖ Redistribution effectuée ("
                    + strategyBox.getSelectedItem() + ")");
            refresh();
        } catch (VehicleException ex) {
            log("⚠ Redistribution échouée : " + ex.getMessage());
        }
    }

    private void actionCheckStolen() {
        center.checkAllStolen();
        log("🚨 Vérification des vols effectuée");
        refresh();
    }

    // ══════════════════════════════════════════════════════════════════════
    // RAFRAÎCHISSEMENT UI
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
        for (Station s : stations) {
            stationGrid.add(buildStationCard(s));
        }
        if (selectedStation != null) {
            highlightCard(selectedStation);
        }
        stationGrid.revalidate();
        stationGrid.repaint();
    }

    private void refreshDetail() {
        if (selectedStation == null) return;
        stationTitle.setText("📍 " + STATION_NAMES[selectedStation.getId() - 1]
                + "   —   " + selectedStation.totalBikes()
                + " / " + selectedStation.getCapacity() + " véhicule(s)");

        vehicleListModel.clear();
        for (Vehicle v : selectedStation.getAllVehicles()) {
            vehicleListModel.addElement(v);
        }

        inUseListModel.clear();
        for (Vehicle v : vehiclesInUse) {
            inUseListModel.addElement(v);
        }
    }

    private void refreshStats() {
        long broken = 0, stolen = 0;
        for (Station s : stations) {
            for (Vehicle v : s.getAllVehicles()) {
                if (v.getState() instanceof Broken) broken++;
                if (v.getState() instanceof Stolen) stolen++;
            }
        }
        lblInUse.setText("En circulation : " + vehiclesInUse.size());
        lblBroken.setText("Hors service : " + broken);
        lblStolen.setText("Volés : " + stolen);
    }

    // ══════════════════════════════════════════════════════════════════════
    // HELPERS UI
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

    private JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(C_CARD);
        return p;
    }

    private JLabel lbl(String text, int style, int size) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(C_TEXT);
        return l;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        return b;
    }

    private void styleList(JList<Vehicle> list) {
        list.setBackground(new Color(22, 22, 38));
        list.setForeground(C_TEXT);
        list.setFont(new Font("Consolas", Font.PLAIN, 12));
        list.setSelectionBackground(C_ACCENT);
        list.setSelectionForeground(Color.WHITE);
        list.setFixedCellHeight(28);
    }

    // ══════════════════════════════════════════════════════════════════════
    // RENDERER VÉHICULES
    // ══════════════════════════════════════════════════════════════════════

    private class VehicleRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean selected, boolean focus) {
            super.getListCellRendererComponent(list, value, index, selected, focus);

            if (value instanceof Vehicle) {
                Vehicle v = (Vehicle) value;
                String stateIcon;
                Color  stateColor;

                if (v.getState() instanceof Available) {
                    stateIcon  = "🟢";
                    stateColor = selected ? Color.WHITE : C_GREEN;
                } else if (v.getState() instanceof Broken) {
                    stateIcon  = "🔴";
                    stateColor = selected ? Color.WHITE : C_RED;
                } else if (v.getState() instanceof Stolen) {
                    stateIcon  = "⚫";
                    stateColor = selected ? Color.WHITE : C_MUTED;
                } else {
                    stateIcon  = "🟡";
                    stateColor = selected ? Color.WHITE : C_YELLOW;
                }

                String type  = shortType(v);
                String state = v.getState().getClass().getSimpleName();
                setText(stateIcon + "  #" + v.getId() + "  " + type
                        + "   [" + state + "]");
                setForeground(stateColor);
                setBackground(selected ? C_ACCENT : new Color(22, 22, 38));
                setBorder(new EmptyBorder(2, 6, 2, 6));
            }
            return this;
        }

        private String shortType(Vehicle v) {
            String cn = v.getClass().getSimpleName();
            switch (cn) {
                case "ClassicBike":            return "Vélo classique";
                case "ElectricBike":           return "Vélo électrique ⚡";
                case "Scooter":                return "Scooter 🛴";
                case "BasketDecorator":        return "Vélo + panier 🧺";
                case "LuggageRackDecorator":   return "Vélo + bagages 🧳";
                default:                       return cn;
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // POINT D'ENTRÉE
    // ══════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        SwingUtilities.invokeLater(VLilleApp::new);
    }
}
