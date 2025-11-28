package Project;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Stack;

public class ClerkDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    // Buttons
    private JButton btnAssignShipment, btnAssignVehicle, btnAssignDriver, btnHandlePayments, btnLogout;

    // Clerk info
    private Clerk clerk;
    
    // Navigation stack
    private Stack<JFrame> frameStack = new Stack<>();

    public ClerkDashboard(Clerk loggedInUser) {
        this.clerk = loggedInUser;
        
        // Push the dashboard itself onto the stack
        frameStack.push(this);

        initializeComponents();
        layoutComponents();
        setWindowProperties();

        // Button actions
        btnAssignShipment.addActionListener(e -> openAssignShipmentView());
        btnAssignVehicle.addActionListener(e -> openAssignVehicleView());
        btnAssignDriver.addActionListener(e -> openAssignDriverView());
        btnHandlePayments.addActionListener(e -> openHandlePaymentsView());
        btnLogout.addActionListener(e -> backToLogin());
    }

    private void initializeComponents() {
        btnAssignShipment = new JButton("Assign Shipments to Vehicles");
        btnAssignVehicle = new JButton("Assign Vehicles to Routes");
        btnAssignDriver = new JButton("Assign Drivers to Vehicles");
        btnHandlePayments = new JButton("Handle Payments");
        btnLogout = new JButton("Logout");
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
        gbc.gridy++;
        add(btnLogout, gbc);
    }

    private void setWindowProperties() {
        setTitle("Clerk Dashboard - " + clerk.getFirstName());
        setSize(400, 400); // Increased height for navigation buttons
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
    
    private void openAssignShipmentView() {
        AssignShipmentView view = new AssignShipmentView(clerk);
        frameStack.push(view);
    }
    
    private void openAssignVehicleView() {
        AssignVehicleView view = new AssignVehicleView(clerk);
        frameStack.push(view);
    }
    
    private void openAssignDriverView() {
        AssignDriverToVehicleView view = new AssignDriverToVehicleView(clerk);
        frameStack.push(view);
    }
    
    private void openHandlePaymentsView() {
        HandlePaymentsView view = new HandlePaymentsView();
        frameStack.push(view);
    }
    
    /**
     * Navigates back to the previous frame in the navigation stack
     */
    private void navigateBack() {
        if (frameStack.size() > 1) {
            // Close current frame
            JFrame currentFrame = frameStack.pop();
            currentFrame.dispose();
            
            // Show previous frame
            JFrame previousFrame = frameStack.peek();
            previousFrame.setVisible(true);
            previousFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "You are at the main dashboard");
        }
    }

    /**
     * Returns to login screen and closes all frames
     */
    private void backToLogin() {
        // Close all frames in the stack
        while (!frameStack.isEmpty()) {
            JFrame frame = frameStack.pop();
            frame.dispose();
        }
        new LoginView();
    }
    
    /**
     * Method to add frames to the navigation stack from child components
     */
    public void addToFrameStack(JFrame frame) {
        frameStack.push(frame);
    }

    // ------------------- SUB WINDOWS -------------------

    // Assign Shipments → Vehicles
    private class AssignShipmentView extends JFrame {
        private JTable shipmentTable, vehicleTable;
        private DefaultTableModel shipmentModel, vehicleModel;
        private JButton btnAssign, btnUnassign, btnBack;
        private Clerk clerk;

        public AssignShipmentView(Clerk clerk) {
            this.clerk = clerk;

            setTitle("Assign/Reassign Shipments - " + clerk.getFirstName());
            setSize(900, 500); // Increased height for back button
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
            JPanel btnPanel = new JPanel(new BorderLayout());
            JPanel actionPanel = new JPanel(new FlowLayout());
            btnAssign = new JButton("Assign/Reassign to Vehicle");
            btnUnassign = new JButton("Unassign Shipment");
            actionPanel.add(btnAssign);
            actionPanel.add(btnUnassign);
            
            btnBack = new JButton("Back");
            JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            backPanel.add(btnBack);
            
            btnPanel.add(backPanel, BorderLayout.WEST);
            btnPanel.add(actionPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);

            // Action listeners
            btnAssign.addActionListener(e -> assignShipment());
            btnUnassign.addActionListener(e -> unassignShipment());
            btnBack.addActionListener(e -> navigateBackFromChild());

            setVisible(true);
        }
        
        private void navigateBackFromChild() {
            dispose();
            ClerkDashboard.this.navigateBack();
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
            Invoice invoice = InvoiceDAO.getInvoiceBytracking(trackingNumber);

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
            if(invoice.getStatus().equals("PAID") || invoice.getStatus().equals("PARTIALLY PAID")) {
                JOptionPane.showMessageDialog(this,
                       "This shipment should be paid fully before assignment.");
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
        private JButton btnAssign, btnRemove, btnBack;

        public AssignVehicleView(Clerk clerk) {
            setTitle("Assign Vehicles to Routes - " + clerk.getFirstName());
            setSize(800, 450); // Increased height for back button
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

            JPanel btnPanel = new JPanel(new BorderLayout());
            JPanel actionPanel = new JPanel(new FlowLayout());
            btnAssign = new JButton("Assign Vehicle to Route");
            btnAssign.addActionListener(e -> assignVehicle());
            actionPanel.add(btnAssign);

            btnRemove = new JButton("Remove Vehicle from Route");
            btnRemove.addActionListener(e -> removeVehicleFromRoute());
            actionPanel.add(btnRemove);
            
            btnBack = new JButton("Back");
            JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            backPanel.add(btnBack);
            
            btnPanel.add(backPanel, BorderLayout.WEST);
            btnPanel.add(actionPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);
            
            btnBack.addActionListener(e -> navigateBackFromChild());

            setVisible(true);
        }
        
        private void navigateBackFromChild() {
            dispose();
            ClerkDashboard.this.navigateBack();
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
        private JButton btnAssign, btnRemove, btnBack;

        public AssignDriverToVehicleView(Clerk clerk) {
            setTitle("Assign Driver to Vehicle - " + clerk.getFirstName());
            setSize(900, 450); // Increased height for back button
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

            JPanel buttonPanel = new JPanel(new BorderLayout());
            JPanel actionPanel = new JPanel(new FlowLayout());
            btnAssign = new JButton("Assign/Reassign Driver to Vehicle");
            btnAssign.addActionListener(e -> assignDriver());

            btnRemove = new JButton("Remove Driver from Vehicle");
            btnRemove.addActionListener(e -> removeDriver());

            actionPanel.add(btnAssign);
            actionPanel.add(btnRemove);
            
            btnBack = new JButton("Back");
            JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            backPanel.add(btnBack);
            
            buttonPanel.add(backPanel, BorderLayout.WEST);
            buttonPanel.add(actionPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
            
            btnBack.addActionListener(e -> navigateBackFromChild());

            setVisible(true);
        }
        
        private void navigateBackFromChild() {
            dispose();
            ClerkDashboard.this.navigateBack();
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
    
    private class HandlePaymentsView extends JFrame {
        private static final long serialVersionUID = 1L;

        JTable table;
        DefaultTableModel model;
        JButton btnPay, btnBack;

        public HandlePaymentsView() {
            setTitle("Invoice Management");
            setSize(600, 400); // Increased size for better layout
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout(10, 10));

            model = new DefaultTableModel(new String[]{"Invoice Number", "Status", "Cost", "Balance", "Tracking Number"}, 0);
            table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Invoices"));
            add(scrollPane, BorderLayout.CENTER);

            loadShipments();
            
            // Buttons panel
            JPanel btnPanel = new JPanel(new BorderLayout());
            JPanel actionPanel = new JPanel(new FlowLayout());
            btnPay = new JButton("Make Payment");
            actionPanel.add(btnPay);
            
            btnBack = new JButton("Back");
            JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            backPanel.add(btnBack);
            
            btnPanel.add(backPanel, BorderLayout.WEST);
            btnPanel.add(actionPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);

            // Action listeners
            btnPay.addActionListener(e -> makeInvoicePayment());
            btnBack.addActionListener(e -> navigateBackFromChild());

            setVisible(true);
        }
        
        private void navigateBackFromChild() {
            dispose();
            ClerkDashboard.this.navigateBack();
        }
        
        private void loadShipments() {
            List<Invoice> invoices = InvoiceDAO.getAllInvoices(); // fetch all invoices
            model.setRowCount(0); // Clear existing rows
            for (Invoice i : invoices) {
                model.addRow(new Object[]{i.getInvoiceNum(), i.getStatus(), i.getTotal(), i.getBalance(), i.getTrackingNumber()});
            }
        }
        
        private void makeInvoicePayment() {
            boolean success = false;
            
            int invoiceRow = table.getSelectedRow();
            
            if (invoiceRow == -1 ) {
                JOptionPane.showMessageDialog(this, "Select an invoice.");
                return;
            }
            
            String invoiceNumber = model.getValueAt(invoiceRow, 0).toString();
            
            Invoice invoice = InvoiceDAO.getInvoiceByInvoiceNumber(invoiceNumber);
            
            if (invoice == null) {
                JOptionPane.showMessageDialog(this, "Invoice not found.");
                return;
            } else {
                success = true;
            }
            if (invoice.getStatus().equals("PAID")) {
                JOptionPane.showMessageDialog(this, "Invoice is already paid for.");
                return;
            } else {
                success = true;
            }
            
            if(success) {
                ProfessionalCheckoutView checkoutView = new ProfessionalCheckoutView(invoice, this);
                ClerkDashboard.this.addToFrameStack(checkoutView);
            }
        }
        
        // Professional Checkout View
        public class ProfessionalCheckoutView extends JFrame {
            private static final long serialVersionUID = 1L;
            
            private Invoice invoice;
            private HandlePaymentsView parentView;
            
            // Panels
            private JPanel mainPanel, deliveryPanel, packagePanel, costPanel, paymentPanel;
            
            // Labels
            private JLabel lblTitle, lblDelivery, lblPackage, lblCost, lblPayment;
            private JLabel lblRecipient, lblAddress, lblHeight, lblWidth, lblLength, lblWeight;
            private JLabel lblBaseCost, lblAdditionalCost, lblTotalCost;
            
            // Display fields
            private JTextField txtRecipient, txtAddress, txtHeight, txtWidth, txtLength, txtWeight;
            private JTextField txtBaseCost, txtAdditionalCost, txtTotalCost;
            
            // Payment components
            private JRadioButton rbCash, rbCard;
            private ButtonGroup paymentGroup;
            private JButton btnMakePayment, btnCancel, btnBack;
            
            public ProfessionalCheckoutView(Invoice invoice, HandlePaymentsView parentView) {
                this.invoice = invoice;
                this.parentView = parentView;
                
                initializeComponents();
                layoutComponents();
                setWindowProperties();
                loadInvoiceData();
                
                setVisible(true);
            }
            
            private void initializeComponents() {
                // Main panels
                mainPanel = new JPanel(new BorderLayout(10, 10));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                
                // Title
                lblTitle = new JLabel("Payment Checkout", JLabel.CENTER);
                lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
                lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
                
                // Delivery Information
                deliveryPanel = createTitledPanel("Delivery Information");
                lblRecipient = new JLabel("Recipient:");
                lblAddress = new JLabel("Address:");
                txtRecipient = createDisplayField();
                txtAddress = createDisplayField();
                
                // Package Information
                packagePanel = createTitledPanel("Package Details");
                lblHeight = new JLabel("Height (cm):");
                lblWidth = new JLabel("Width (cm):");
                lblLength = new JLabel("Length (cm):");
                lblWeight = new JLabel("Weight (kg):");
                txtHeight = createDisplayField();
                txtWidth = createDisplayField();
                txtLength = createDisplayField();
                txtWeight = createDisplayField();
                
                // Cost Information
                costPanel = createTitledPanel("Cost Breakdown");
                lblBaseCost = new JLabel("Base Cost:");
                lblAdditionalCost = new JLabel("Additional Charges:");
                lblTotalCost = new JLabel("Total Amount:");
                txtBaseCost = createDisplayField();
                txtAdditionalCost = createDisplayField();
                txtTotalCost = createDisplayField();
                
                // Payment Method
                paymentPanel = createTitledPanel("Payment Method");
                lblPayment = new JLabel("Select Payment Method:");
                rbCash = new JRadioButton("Cash Payment");
                rbCard = new JRadioButton("Credit/Debit Card");
                paymentGroup = new ButtonGroup();
                paymentGroup.add(rbCash);
                paymentGroup.add(rbCard);
                
                // Buttons
                btnMakePayment = new JButton("Process Payment");
                btnMakePayment.setBackground(new Color(70, 130, 180));
                btnMakePayment.setForeground(Color.WHITE);
                btnMakePayment.setFont(new Font("Arial", Font.BOLD, 14));
                
                btnCancel = new JButton("Cancel");
                btnBack = new JButton("Back");
            }
            
            private JPanel createTitledPanel(String title) {
                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1), title,
                    TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                    new Font("Arial", Font.BOLD, 12)));
                return panel;
            }
            
            private JTextField createDisplayField() {
                JTextField field = new JTextField();
                field.setEditable(false);
                field.setBackground(Color.WHITE);
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                return field;
            }
            
            private void layoutComponents() {
                // Delivery Panel Layout
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
                deliveryPanel.add(lblRecipient, gbc);
                gbc.gridx = 1; gbc.gridwidth = 2;
                deliveryPanel.add(txtRecipient, gbc);
                
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
                deliveryPanel.add(lblAddress, gbc);
                gbc.gridx = 1; gbc.gridwidth = 2;
                deliveryPanel.add(txtAddress, gbc);
                
                // Package Panel Layout
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
                packagePanel.add(lblHeight, gbc);
                gbc.gridx = 1;
                packagePanel.add(txtHeight, gbc);
                gbc.gridx = 2;
                packagePanel.add(lblWidth, gbc);
                gbc.gridx = 3;
                packagePanel.add(txtWidth, gbc);
                
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
                packagePanel.add(lblLength, gbc);
                gbc.gridx = 1;
                packagePanel.add(txtLength, gbc);
                gbc.gridx = 2;
                packagePanel.add(lblWeight, gbc);
                gbc.gridx = 3;
                packagePanel.add(txtWeight, gbc);
                
                // Cost Panel Layout
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
                costPanel.add(lblBaseCost, gbc);
                gbc.gridx = 1; gbc.gridwidth = 2;
                costPanel.add(txtBaseCost, gbc);
                
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
                costPanel.add(lblAdditionalCost, gbc);
                gbc.gridx = 1; gbc.gridwidth = 2;
                costPanel.add(txtAdditionalCost, gbc);
                
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
                costPanel.add(lblTotalCost, gbc);
                gbc.gridx = 1; gbc.gridwidth = 2;
                costPanel.add(txtTotalCost, gbc);
                
                // Payment Panel Layout
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
                paymentPanel.add(lblPayment, gbc);
                
                gbc.gridy = 1;
                paymentPanel.add(rbCash, gbc);
                
                gbc.gridy = 2;
                paymentPanel.add(rbCard, gbc);
                
                // Main Layout
                JPanel contentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
                contentPanel.add(deliveryPanel);
                contentPanel.add(packagePanel);
                contentPanel.add(costPanel);
                contentPanel.add(paymentPanel);
                
                // Button Panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                buttonPanel.add(btnMakePayment);
                buttonPanel.add(btnCancel);
                buttonPanel.add(btnBack);
                
                // Assemble main panel
                mainPanel.add(lblTitle, BorderLayout.NORTH);
                mainPanel.add(contentPanel, BorderLayout.CENTER);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                setContentPane(mainPanel);
                
                // Action listeners
                btnMakePayment.addActionListener(e -> processPayment());
                btnCancel.addActionListener(e -> dispose());
                btnBack.addActionListener(e -> navigateBackFromCheckout());
            }
            
            private void setWindowProperties() {
                setTitle("Payment Checkout - Invoice #" + invoice.getInvoiceNum());
                setSize(700, 600);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setResizable(false);
            }
            
            private void loadInvoiceData() {
                String trackingNumber = invoice.getTrackingNumber();
                Shipment s = ShipmentDAO.getShipmentByTrackingNumber(trackingNumber);
                
                if (s != null) {
                    double bCost = 0;
                    double aCost = 0;
                    
                    if (s.packageType.equals(Project.Type.EXPRESS)) {
                        bCost = s.getCost() / 2.0;
                        aCost = s.getCost() - bCost;
                    } else if (s.packageType.equals(Project.Type.FRAGILE)) {
                        bCost = s.getCost() / 3.0;
                        aCost = s.getCost() - bCost;
                    } else {
                        bCost = s.getCost();
                    }
                    
                    // Set values
                    txtRecipient.setText(s.getRecipient().getName());
                    txtAddress.setText(s.getRecipient().getAddress().toString());
                    txtHeight.setText(String.format("%.1f cm", s.getPkg().getHeight()));
                    txtWidth.setText(String.format("%.1f cm", s.getPkg().getWidth()));
                    txtLength.setText(String.format("%.1f cm", s.getPkg().getLength()));
                    txtWeight.setText(String.format("%.1f kg", s.getPkg().getWeight()));
                    txtBaseCost.setText(String.format("$%.2f", bCost));
                    txtAdditionalCost.setText(String.format("$%.2f", aCost));
                    txtTotalCost.setText(String.format("$%.2f", s.getCost()));
                }
            }
            
            private void processPayment() {
                if (!rbCash.isSelected() && !rbCard.isSelected()) {
                    JOptionPane.showMessageDialog(this, "Please select a payment method.", "Payment Method Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (rbCash.isSelected()) {
                    new ClerkCash(invoice, this);
                } else if (rbCard.isSelected()) {
                    new CardWindow(invoice, this);
                }
            }
            
            private void navigateBackFromCheckout() {
                dispose();
                ClerkDashboard.this.navigateBack();
            }
            
            public void refreshParent() {
                parentView.loadShipments();
            }
        }

        // Updated CardWindow with navigation
        private class CardWindow extends JFrame {
            private static final long serialVersionUID = 1L;
            
            private JLabel lblBalanceValue;
            private JTextField txtHolderName, txtCardNumber, txtExpiryDate;
            private JPasswordField txtCVV;
            private JButton btnMakePayment, btnCancel, btnBack;
            private ProfessionalCheckoutView parentCheckout;

            public CardWindow(Invoice invoice, ProfessionalCheckoutView parentCheckout) {
                this.parentCheckout = parentCheckout;
                setTitle("Credit Card Payment");
                setSize(450, 470); // Increased height for back button
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                initComponents(invoice);
                setVisible(true);
            }

            private void initComponents(Invoice invoice) {
                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // Title
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                JLabel lblTitle = new JLabel("Credit Card Payment", JLabel.CENTER);
                lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
                panel.add(lblTitle, gbc);

                // Balance Label
                gbc.gridy = 1; gbc.gridwidth = 1;
                panel.add(new JLabel("Outstanding Balance:"), gbc);
                gbc.gridx = 1;
                lblBalanceValue = new JLabel("$0.00");
                lblBalanceValue.setFont(new Font("Arial", Font.BOLD, 14));
                panel.add(lblBalanceValue, gbc);

                // Card Details
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
                panel.add(new JLabel("Card Holder Name:"), gbc);
                gbc.gridx = 1;
                txtHolderName = new JTextField(15);
                panel.add(txtHolderName, gbc);

                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Card Number:"), gbc);
                gbc.gridx = 1;
                txtCardNumber = new JTextField(15);
                panel.add(txtCardNumber, gbc);

                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Expiration Date (MM/YY):"), gbc);
                gbc.gridx = 1;
                txtExpiryDate = new JTextField(15);
                panel.add(txtExpiryDate, gbc);

                gbc.gridx = 0; gbc.gridy = 5;
                panel.add(new JLabel("CVV:"), gbc);
                gbc.gridx = 1;
                txtCVV = new JPasswordField(15);
                panel.add(txtCVV, gbc);

                // Buttons
                btnMakePayment = new JButton("Process Payment");
                btnCancel = new JButton("Cancel");
                btnBack = new JButton("Back");

                JPanel buttonPanel = new JPanel(new FlowLayout());
                buttonPanel.add(btnMakePayment);
                buttonPanel.add(btnCancel);
                buttonPanel.add(btnBack);

                gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(buttonPanel, gbc);

                // Set balance
                double balance = invoice.getBalance();
                setBalance(balance);

                // Action listeners
                btnCancel.addActionListener(e -> dispose());
                btnBack.addActionListener(e -> navigateBackFromCard());
                btnMakePayment.addActionListener(e -> {
                    onMakePayment(invoice);     
                    dispose();
                });

                add(panel);
            }

            public void setBalance(double amount) {
                lblBalanceValue.setText(String.format("$%.2f", amount));
            }

            private void navigateBackFromCard() {
                dispose();
            }

            private void onMakePayment(Invoice invoice) {
                String name = txtHolderName.getText().trim();
                String card = txtCardNumber.getText().trim();
                String expiry = txtExpiryDate.getText().trim();
                String cvv = new String(txtCVV.getPassword()).trim();

                // Validate card fields
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Card holder name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!card.matches("\\d{16}")) {
                    JOptionPane.showMessageDialog(this, "Card number must be exactly 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Expiration date must be in MM/YY format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!cvv.matches("\\d{3}")) {
                    JOptionPane.showMessageDialog(this, "CVV must be exactly 3 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String newStatus = "PAID";
                String invoiceNum = String.valueOf(invoice.getInvoiceNum());
                double newBalance = 0.0;
                InvoiceDAO.updateInvoiceStatusAndBalance(newStatus, newBalance, invoiceNum);
                JOptionPane.showMessageDialog(this, "Payment processed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh parent view
                parentCheckout.refreshParent();
                parentCheckout.dispose();
            }
        }

        // Updated ClerkCash with navigation
        public class ClerkCash extends JFrame {
            private static final long serialVersionUID = 1L;
            
            private JLabel lblBalanceValue;
            private JTextField txtAmountToPay;
            private JButton btnMakePayment, btnCancel, btnBack;
            private double currentBalance = 0.0;
            private ProfessionalCheckoutView parentCheckout;

            public ClerkCash(Invoice invoice, ProfessionalCheckoutView parentCheckout) {
                this.parentCheckout = parentCheckout;
                initialize(invoice);
                double balance = invoice.getBalance();
                setCBalance(balance);
                setVisible(true);
            }

            private void initialize(Invoice invoice) {
                setTitle("Cash Payment");
                setSize(400, 300); // Increased height for back button
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // Title
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                JLabel lblTitle = new JLabel("Cash Payment", JLabel.CENTER);
                lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
                panel.add(lblTitle, gbc);

                // Balance Label
                gbc.gridy = 1; gbc.gridwidth = 1;
                panel.add(new JLabel("Outstanding Balance:"), gbc);
                gbc.gridx = 1;
                lblBalanceValue = new JLabel("$0.00");
                lblBalanceValue.setFont(new Font("Arial", Font.BOLD, 14));
                panel.add(lblBalanceValue, gbc);

                // Amount to be paid
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Amount to Be Paid:"), gbc);
                gbc.gridx = 1;
                txtAmountToPay = new JTextField(10);
                panel.add(txtAmountToPay, gbc);

                // Buttons
                btnMakePayment = new JButton("Process Payment");
                btnCancel = new JButton("Cancel");
                btnBack = new JButton("Back");

                JPanel buttonPanel = new JPanel(new FlowLayout());
                buttonPanel.add(btnMakePayment);
                buttonPanel.add(btnCancel);
                buttonPanel.add(btnBack);

                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(buttonPanel, gbc);

                // Listeners
                btnCancel.addActionListener(e -> dispose());
                btnBack.addActionListener(e -> navigateBackFromCash());
                btnMakePayment.addActionListener(e -> onMakePayment(invoice));

                add(panel);
            }

            public void setCBalance(double amount) {
                this.currentBalance = amount;
                lblBalanceValue.setText(String.format("$%.2f", amount));
            }

            private void navigateBackFromCash() {
                dispose();
            }

            private void onMakePayment(Invoice invoice) {
                String amountStr = txtAmountToPay.getText().trim();

                if (!amountStr.matches("\\d+(\\.\\d{1,2})?")) {
                    JOptionPane.showMessageDialog(this, "Enter a valid payment amount.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amountPaid = Double.parseDouble(amountStr);

                if (amountPaid <= 0) {
                    JOptionPane.showMessageDialog(this, "Payment amount must be greater than zero.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // If paying more than the balance stops overflow
                if (amountPaid >= currentBalance) {
                    String newStatus = "PAID";
                    String invoiceNum = String.valueOf(invoice.getInvoiceNum());
                    double newBalance = 0.0;
                    InvoiceDAO.updateInvoiceStatusAndBalance(newStatus, newBalance, invoiceNum);
                    
                    JOptionPane.showMessageDialog(this,
                            "Payment processed! Full balance paid.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    parentCheckout.refreshParent();
                    parentCheckout.dispose();
                    return;
                }

                // Partial payment
                currentBalance -= amountPaid;
                lblBalanceValue.setText(String.format("$%.2f", currentBalance));
                
                String newStatus = "PARTIALLY PAID";
                String invoiceNum = String.valueOf(invoice.getInvoiceNum());
                double newBalance = currentBalance;
                InvoiceDAO.updateInvoiceStatusAndBalance(newStatus, newBalance, invoiceNum);

                JOptionPane.showMessageDialog(this,
                        "Payment processed! Remaining balance: $" +
                                String.format("%.2f", currentBalance),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose();
                parentCheckout.refreshParent();
            }
        }
    }
}