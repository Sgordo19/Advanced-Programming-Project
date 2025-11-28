package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Stack;

public class DriverDashboard extends JFrame {
    private JTable routesTable, shipmentsTable;
    private DefaultTableModel routesModel, shipmentsModel;
    private JLabel lblVehicleInfo;
    private User driver;
    private JButton btnLogout;
    
    // Navigation stack to track opened frames
    private Stack<JFrame> frameStack = new Stack<>();

    public DriverDashboard(User driver) {
        this.driver = driver;
        
        // Push the dashboard itself onto the stack
        frameStack.push(this);

        initializeComponents();
        layoutComponents();
        setWindowProperties();
        loadDriverData();

        setVisible(true);
    }

    private void initializeComponents() {
        // Vehicle info label
        lblVehicleInfo = new JLabel("Vehicle info will appear here");
        
        // Tables
        routesModel = new DefaultTableModel(new String[]{"Route ID", "Route Name", "Start", "End"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // routes table is read-only
            }
        };
        routesTable = new JTable(routesModel);

        shipmentsModel = new DefaultTableModel(new String[]{"Tracking #", "Recipient Name", "Address", "Weight", "Status"}, 0);
        shipmentsTable = new JTable(shipmentsModel);

        // Button
        btnLogout = new JButton("Logout");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel with vehicle info and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblVehicleInfo, BorderLayout.NORTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnLogout);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);

        // Make status column a combo box
        JComboBox<String> statusCombo = new JComboBox<>();
        for (Status s : Status.values()) {
            statusCombo.addItem(s.getDisplayName());
        }
        shipmentsTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));

        // Listen for status changes
        shipmentsModel.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && e.getColumn() == 4) {
                int row = e.getFirstRow();
                String trackingNumber = (String) shipmentsModel.getValueAt(row, 0);
                String newStatusStr = (String) shipmentsModel.getValueAt(row, 4);
                Status newStatus = Status.fromString(newStatusStr);
                if (newStatus != null) {
                    ShipmentDAO.updateShipmentStatus(trackingNumber, newStatus);
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(routesTable), new JScrollPane(shipmentsTable));
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        // Action listeners
        btnLogout.addActionListener(e -> backToLogin());
    }

    private void setWindowProperties() {
        setTitle("Driver Dashboard - " + driver.getFirstName());
        setSize(900, 550); // Slightly increased height for buttons
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
    }

    private void loadDriverData() {
        routesModel.setRowCount(0);
        shipmentsModel.setRowCount(0);

        // Get routes for this driver
        List<Route> routes = RouteDAO.getRoutesByDriver(driver.getUserID());
        int vehicleId = -1;
        for (Route r : routes) {
            routesModel.addRow(new Object[]{r.getRoute_id(), r.getRoute_name(), r.getStart_location(), r.getEnd_location()});
            if (r.getVehicle_id() != 0) vehicleId = r.getVehicle_id();
        }

        // Get vehicle info
        if (vehicleId != -1) {
            Vehicle vehicle = VehicleDAO.getVehicleByDriverId(driver.getUserID());
            lblVehicleInfo.setText("Vehicle ID: " + vehicle.getVehicle_id() +
                    ", Status: " + vehicle.getV_status() +
                    ", Max Weight: " + vehicle.getMax_weight() +
                    ", Max Packages: " + vehicle.getMax_quantity());

            // Get shipments for this vehicle
            List<Shipment> shipments = ShipmentDAO.getShipmentsByVehicle(vehicle.getVehicle_id());
            for (Shipment s : shipments) {
                Status shipmentStatus = s.getStatus();
                if (shipmentStatus == Status.ASSIGNED || shipmentStatus == Status.IN_TRANSIT) {
                    shipmentsModel.addRow(new Object[]{
                            s.getTrackingNumber(),
                            s.getRecipient().getName(),
                            s.getRecipient().getAddress().getAddress(),
                            s.getPkg().getWeight(),
                            shipmentStatus.getDisplayName()
                    });
                }
            }
        } else {
            lblVehicleInfo.setText("No vehicle assigned.");
        }
    }

    
    //Returns to login screen and closes all frames
    private void backToLogin() {
        // Close all frames in the stack
        while (!frameStack.isEmpty()) {
            JFrame frame = frameStack.pop();
            frame.dispose();
        }
        new LoginView();
    }
    
    //Method to add frames to the navigation stack from child components
    public void addToFrameStack(JFrame frame) {
        frameStack.push(frame);
    }

    //Method to refresh the dashboard data
    public void refreshData() {
        loadDriverData();
    }

    //Method to clear all data from tables
    private void clearTables() {
        routesModel.setRowCount(0);
        shipmentsModel.setRowCount(0);
    }
}