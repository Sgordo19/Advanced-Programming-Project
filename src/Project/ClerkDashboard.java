package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClerkDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    // Buttons
    private JButton btnAssignShipment, btnAssignVehicle, btnAssignDriver, btnHandlePayments;

    // Clerk info
    private Clerk clerk;

    public ClerkDashboard(Clerk loggedInUser) {
        this.clerk = loggedInUser;

        initializeComponents();
        layoutComponents();
        setWindowProperties();

        // Button actions
        btnAssignShipment.addActionListener(e -> new AssignShipmentView(clerk));
        btnAssignVehicle.addActionListener(e -> new AssignVehicleView(clerk));
        btnAssignDriver.addActionListener(e -> new AssignDriverToVehicleView(clerk));
        btnHandlePayments.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Payment handling not implemented yet."));
    }

    private void initializeComponents() {
        btnAssignShipment = new JButton("Assign Shipments to Vehicles");
        btnAssignVehicle = new JButton("Assign Vehicles to Routes");
        btnAssignDriver = new JButton("Assign Drivers to Vehicles");
        btnHandlePayments = new JButton("Handle Payments");
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(btnAssignShipment, gbc);
        gbc.gridy++;
        add(btnAssignVehicle, gbc);
        gbc.gridy++;
        add(btnAssignDriver, gbc);
        gbc.gridy++;
        add(btnHandlePayments, gbc);
    }

    private void setWindowProperties() {
        setTitle("Clerk Dashboard - " + clerk.getFirstName());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    // ------------------- SUB WINDOWS -------------------

    // Assign Shipments → Vehicles
    private class AssignShipmentView extends JFrame {
        private JTable shipmentTable, vehicleTable;
        private DefaultTableModel shipmentModel, vehicleModel;
        private JButton btnAssign, btnUnassign;
        private Clerk clerk;

        public AssignShipmentView(Clerk clerk) {
            this.clerk = clerk;

            setTitle("Assign/Reassign Shipments - " + clerk.getFirstName());
            setSize(900, 450);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            // Shipment table
            shipmentModel = new DefaultTableModel(new String[]{"Tracking Number", "Status", "Weight"}, 0);
            shipmentTable = new JTable(shipmentModel);
            loadShipments();

            // Vehicle table
            vehicleModel = new DefaultTableModel(new String[]{"Vehicle ID", "Max Weight", "Max Quantity", "Current Weight", "Current Quantity"}, 0);
            vehicleTable = new JTable(vehicleModel);
            loadVehicles();

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(shipmentTable), new JScrollPane(vehicleTable));
            splitPane.setResizeWeight(0.5);
            add(splitPane, BorderLayout.CENTER);

            // Buttons panel
            JPanel btnPanel = new JPanel(new FlowLayout());
            btnAssign = new JButton("Assign/Reassign to Vehicle");
            btnUnassign = new JButton("Unassign Shipment");
            btnPanel.add(btnAssign);
            btnPanel.add(btnUnassign);
            add(btnPanel, BorderLayout.SOUTH);

            // Action listeners
            btnAssign.addActionListener(e -> assignShipment());
            btnUnassign.addActionListener(e -> unassignShipment());

            setVisible(true);
        }

        private void loadShipments() {
            shipmentModel.setRowCount(0);
            List<Shipment> shipments = ShipmentDAO.getPendingShipments(); // fetch all PENDING + unassigned
            for (Shipment s : shipments) {
                shipmentModel.addRow(new Object[]{s.getTrackingNumber(), s.getStatus().getDisplayName(), s.getPkg().getWeight()});
            }
        }

        private void loadVehicles() {
            vehicleModel.setRowCount(0);
            List<Vehicle> vehicles = VehicleDAO.getAllVehicles();

            for (Vehicle v : vehicles) {

                double currentWeight = VehicleDAO.getTotalWeightForVehicle(v.getVehicle_id());
                int currentQty = VehicleDAO.getTotalQuantityForVehicle(v.getVehicle_id());

                vehicleModel.addRow(new Object[]{
                        v.getVehicle_id(),
                        v.getMax_weight(),
                        v.getMax_quantity(),
                        currentWeight,
                        currentQty
                });
            }
        }

        private void assignShipment() {
            int shipmentRow = shipmentTable.getSelectedRow();
            int vehicleRow = vehicleTable.getSelectedRow();

            if (shipmentRow == -1 || vehicleRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a shipment and a vehicle.");
                return;
            }

            String trackingNumber = shipmentModel.getValueAt(shipmentRow, 0).toString();
            int vehicleId = (int) vehicleModel.getValueAt(vehicleRow, 0);

            Shipment shipment = ShipmentDAO.getShipmentByTrackingNumber(trackingNumber);
            if (shipment == null) {
                JOptionPane.showMessageDialog(this, "Shipment not found.");
                return;
            }

            Vehicle vehicle = VehicleDAO.getVehicleById(vehicleId);
            if (vehicle == null) {
                JOptionPane.showMessageDialog(this, "Vehicle not found.");
                return;
            }
            if (shipment.getStatus() == Status.ASSIGNED) {
                JOptionPane.showMessageDialog(this,
                    "This shipment is already assigned to a vehicle.\nUse the Unassign button first.");
                return;
            }

            // Check capacity using the DAO helper
            if (!ShipmentDAO.canAssignShipmentToVehicle(shipment, vehicle)) {
                JOptionPane.showMessageDialog(this, "Cannot assign shipment: exceeds vehicle capacity.");
                return;
            }

            // Assign shipment
            boolean success = ShipmentDAO.assignVehicleToShipment(trackingNumber, vehicleId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Shipment assigned/reassigned successfully!");
                loadShipments();
                loadVehicles(); 
            } else {
                JOptionPane.showMessageDialog(this, "Assignment failed. Database error.");
            }
        }

        private void unassignShipment() {
            int shipmentRow = shipmentTable.getSelectedRow();

            if (shipmentRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a shipment to unassign.");
                return;
            }

            String trackingNumber = shipmentModel.getValueAt(shipmentRow, 0).toString();
            Shipment shipment = ShipmentDAO.getShipmentByTrackingNumber(trackingNumber);

            if (shipment == null) {
                JOptionPane.showMessageDialog(this, "Shipment not found.");
                return;
            }

            if (!shipment.getStatus().equals(Status.ASSIGNED)) {
                JOptionPane.showMessageDialog(this, "Only ASSIGNED shipments can be unassigned.");
                return;
            }

            boolean success = ShipmentDAO.assignVehicleToShipment(trackingNumber, 0);

            if (success) {
                JOptionPane.showMessageDialog(this, "Shipment unassigned successfully!");
                loadShipments();
                loadVehicles();  // ⬅ IMPORTANT: Refresh vehicle totals
            } else {
                JOptionPane.showMessageDialog(this, "Unassignment failed.");
            }
        }

    }

    // Assign Vehicles → Routes
    private class AssignVehicleView extends JFrame {
        private JTable vehicleTable, routeTable;
        private DefaultTableModel vehicleModel, routeModel;
        private JButton btnAssign, btnRemove;

        public AssignVehicleView(Clerk clerk) {
            setTitle("Assign Vehicles to Routes - " + clerk.getFirstName());
            setSize(800, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            vehicleModel = new DefaultTableModel(new String[]{"Vehicle ID", "Driver ID"}, 0);
            vehicleTable = new JTable(vehicleModel);
            loadVehicles();

            routeModel = new DefaultTableModel(new String[]{"Route ID", "Route Name", "Vehicle ID", "Driver ID"}, 0);
            routeTable = new JTable(routeModel);
            loadRoutes();

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(vehicleTable), new JScrollPane(routeTable));
            splitPane.setResizeWeight(0.5);
            add(splitPane, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel();
            btnAssign = new JButton("Assign Vehicle to Route");
            btnAssign.addActionListener(e -> assignVehicle());
            btnPanel.add(btnAssign);

            btnRemove = new JButton("Remove Vehicle from Route");
            btnRemove.addActionListener(e -> removeVehicleFromRoute());
            btnPanel.add(btnRemove);

            add(btnPanel, BorderLayout.SOUTH);
            setVisible(true);
        }

        private void loadVehicles() {
            vehicleModel.setRowCount(0);
            List<Vehicle> vehicles = VehicleDAO.getAllVehicles();
            for (Vehicle v : vehicles) {
                vehicleModel.addRow(new Object[]{v.getVehicle_id(), v.getDriver_id()});
            }
        }

        private void loadRoutes() {
            routeModel.setRowCount(0);
            List<Route> routes = RouteDAO.getAllRoutes();
            for (Route r : routes) {
                routeModel.addRow(new Object[]{r.getRoute_id(), r.getRoute_name(), r.getVehicle_id(), r.getDriver_id()});
            }
        }

        private void assignVehicle() {
            int vehicleRow = vehicleTable.getSelectedRow();
            int routeRow = routeTable.getSelectedRow();

            if (vehicleRow == -1 || routeRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a vehicle and a route.");
                return;
            }

            int vehicleId = (int) vehicleModel.getValueAt(vehicleRow, 0);
            int routeId = (int) routeModel.getValueAt(routeRow, 0);

            boolean success = RouteDAO.assignVehicleToRoute(vehicleId, routeId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Vehicle assigned to route successfully!");
                loadRoutes();
            } else {
                JOptionPane.showMessageDialog(this, "Assignment failed. Vehicle may already be assigned to 1 route.");
            }
        }

        private void removeVehicleFromRoute() {
            int routeRow = routeTable.getSelectedRow();

            if (routeRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a route to remove the vehicle from.");
                return;
            }

            int routeId = (int) routeModel.getValueAt(routeRow, 0);
            boolean success = RouteDAO.assignVehicleToRoute(0, routeId); // unassign
            if (success) {
                JOptionPane.showMessageDialog(this, "Vehicle removed from route successfully!");
                loadRoutes();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove vehicle from route.");
            }
        }
    }

    // Assign Drivers → Vehicles
    private class AssignDriverToVehicleView extends JFrame {
        private JTable driverTable, vehicleTable;
        private DefaultTableModel driverModel, vehicleModel;
        private JButton btnAssign, btnRemove;

        public AssignDriverToVehicleView(Clerk clerk) {
            setTitle("Assign Driver to Vehicle - " + clerk.getFirstName());
            setSize(900, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            driverModel = new DefaultTableModel(new String[]{"Driver ID", "Name"}, 0);
            driverTable = new JTable(driverModel);
            loadDrivers();

            vehicleModel = new DefaultTableModel(new String[]{"Vehicle ID", "Current Driver", "Status"}, 0);
            vehicleTable = new JTable(vehicleModel);
            loadVehicles();

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(driverTable), new JScrollPane(vehicleTable));
            splitPane.setResizeWeight(0.5);
            add(splitPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            btnAssign = new JButton("Assign/Reassign Driver to Vehicle");
            btnAssign.addActionListener(e -> assignDriver());

            btnRemove = new JButton("Remove Driver from Vehicle");
            btnRemove.addActionListener(e -> removeDriver());

            buttonPanel.add(btnAssign);
            buttonPanel.add(btnRemove);

            add(buttonPanel, BorderLayout.SOUTH);
            setVisible(true);
        }

        private void loadDrivers() {
            driverModel.setRowCount(0);
            List<User> drivers = UserDAO.getAllDrivers();
            for (User d : drivers) {
                driverModel.addRow(new Object[]{d.getUserID(), d.getFirstName() + " " + d.getLastName()});
            }
        }

        private void loadVehicles() {
            vehicleModel.setRowCount(0);
            List<Vehicle> vehicles = VehicleDAO.getAllVehicles();
            for (Vehicle v : vehicles) {
                vehicleModel.addRow(new Object[]{v.getVehicle_id(), v.getDriver_id(), v.getV_status()});
            }
        }

        private void assignDriver() {
            int driverRow = driverTable.getSelectedRow();
            int vehicleRow = vehicleTable.getSelectedRow();

            if (driverRow == -1 || vehicleRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a driver and a vehicle.");
                return;
            }

            String driverId = (String) driverModel.getValueAt(driverRow, 0);
            int vehicleId = (int) vehicleModel.getValueAt(vehicleRow, 0);

            // Reassign driver if needed
            Vehicle currentVehicle = VehicleDAO.getVehicleByDriverId(driverId);
            if (currentVehicle != null && currentVehicle.getVehicle_id() != vehicleId) {
                VehicleDAO.removeDriverFromVehicle(currentVehicle.getVehicle_id());
            }

            boolean success = VehicleDAO.assignDriverToVehicle(driverId, vehicleId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Driver assigned/reassigned successfully!");
                RouteDAO.updateDriverForVehicleRoutes(vehicleId, driverId);
                loadVehicles();
            } else {
                JOptionPane.showMessageDialog(this, "Assignment failed.");
            }
        }

        private void removeDriver() {
            int vehicleRow = vehicleTable.getSelectedRow();
            if (vehicleRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a vehicle to remove its driver.");
                return;
            }

            int vehicleId = (int) vehicleModel.getValueAt(vehicleRow, 0);
            boolean success = VehicleDAO.removeDriverFromVehicle(vehicleId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Driver removed successfully!");
                RouteDAO.updateDriverForVehicleRoutes(vehicleId, null);
                loadVehicles();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove driver.");
            }
        }
    }
}
