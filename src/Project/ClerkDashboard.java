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
        btnHandlePayments.addActionListener(e -> new HandlePaymentsView());
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
            if(invoice.getStatus().equals("PAID") || invoice.getStatus().equals("PARTIALLY PAID"))
            {
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
    
    
    private class HandlePaymentsView extends JFrame {
        private static final long serialVersionUID = 1L;

        JTable table;
        DefaultTableModel model;
        JButton btnPay;

        public HandlePaymentsView() {
            setTitle("Invoices");
            setSize(500, 300);
            setLocationRelativeTo(null);

            model = new DefaultTableModel(new String[]{"Invoice Number", "Status", "Cost", "Balance", "Tracking Number"}, 0);
            table = new JTable(model);
            add(new JScrollPane(table), BorderLayout.CENTER);

            loadShipments();
            
            // Buttons panel
            JPanel btnPanel = new JPanel(new FlowLayout());
            btnPay = new JButton("Make Payment");
            btnPanel.add(btnPay);
            add(btnPanel, BorderLayout.SOUTH);

            // Action listeners
            btnPay.addActionListener(e -> makeInvoicePayment());

            setVisible(true);
        }
        
        private void loadShipments() {
            List<Invoice> invoices = InvoiceDAO.getAllInvoices(); // fetch all invoices
            for (Invoice i : invoices) {
                model.addRow(new Object[]{i.getInvoiceNum(), i.getStatus(), i.getTotal(), i.getBalance(), i.getTrackingNumber()});
            }
        }
        
        private void makeInvoicePayment()
        {
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
            }else {
            	success = true;
            }
            if (invoice.getStatus().equals("PAID")) {
                JOptionPane.showMessageDialog(this, "Invoice is already paid for.");
                return;
            }else {
            	success = true;
            }
            
            if(success) {
            	new CheckoutView(invoice);
            }
        }
        
        public class CheckoutView extends JFrame{
        	private JFrame main; 
        	private JPanel mainPanel; 
        	private JPanel mainLeft; 
        	private JPanel mainRight; 
        	private JPanel costInfo; 
        	
        	
        	
        	private JLabel checkout;
        	private JLabel delivering;
        	private JLabel packageInfo;
        	private JLabel height;
        	private JLabel width;
        	private JLabel length;
        	private JLabel weight; 
        	//labels for cost section 
        	private JLabel preCost;
        	private JLabel addCosts;
        	private JLabel orderTotal; 
        	private JLabel paymentMethod;
        	
        	//separator 
        	private JSeparator panelSeparator;
        	private JSeparator packageSeparator;
        	
        	//buttons 
        	private JRadioButton cash;
        	private JRadioButton card; 
        	private ButtonGroup payment;
        	private JButton makePayment; 
        	private JButton cancelPayment; 
        	
        	Invoice invoice = new Invoice();
        	
        	JTextField txtRName, txtRAddress, txtPHeight, txtPWeight, txtPLength, txtPWidth, 
        				txtPBCost, txtPACost, txtPTCost;
        	
        	
        	public CheckoutView(Invoice invoice)  {
        		initialize(invoice);
        	}
        	
        	private void initialize(Invoice invoice) {
        		setFrameProperties();
        		initialComponents();
        		addComponentsToFrame();
        		addComponentsToPanel();
        		SwingUtilities.invokeLater(this::updateFields);
        		
        		
        		makePayment.addActionListener(e -> {
        		    if (cash.isSelected()) {
        		    	 // Handle cash payment
        		    	new ClerkCash(invoice);
        		    } else if (card.isSelected()) {
        		        // Handle card payment  
        		        new CardWindow(invoice);
        		    } else {
        		        // No payment method selected
        		        JOptionPane.showMessageDialog(null, "Please select a payment method");
        		    }
        		});
        		
        		cancelPayment.addActionListener(e -> dispose());
        	}
        	
        	private void initialComponents() {
        		
        		mainPanel= new JPanel();
        		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        		
        		mainLeft= new JPanel();
        		mainLeft.setLayout(new BoxLayout(mainLeft,BoxLayout.PAGE_AXIS));
        		mainLeft.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        		
        		mainRight= new JPanel();
        		mainRight.setLayout(new BoxLayout(mainRight,BoxLayout.PAGE_AXIS));
        		mainRight.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        		
        		costInfo= new JPanel();
        		costInfo.setLayout(new BoxLayout(costInfo,BoxLayout.PAGE_AXIS));
        		costInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        		
        		checkout= new JLabel("Checkout");
        		delivering= new JLabel("Delivering to: ");
        		txtRName = new JTextField();
        		txtRAddress = new JTextField();
        		packageInfo= new JLabel("Package");
        		height= new JLabel("height");
        		txtPHeight = new JTextField();
        		width= new JLabel("width");
        		txtPWidth = new JTextField();
        		length= new JLabel("length");
        		txtPLength = new JTextField();
        		weight= new JLabel("Weight");
        		txtPWeight = new JTextField();
        		
        		preCost= new JLabel("Base Cost: ");
        		txtPBCost = new JTextField();
        		addCosts= new JLabel("Discount or Surplus: ");
        		txtPACost = new JTextField();
        		orderTotal= new JLabel("Order Total: ");
        		txtPTCost = new JTextField();
        		paymentMethod= new JLabel("Payment Method ");
        		
        		panelSeparator= new JSeparator(SwingConstants.VERTICAL);
        		panelSeparator.setMaximumSize(new Dimension(5, Integer.MAX_VALUE));
        		
        		packageSeparator= new JSeparator(SwingConstants.HORIZONTAL);
        		
        		cash= new JRadioButton("Cash");
        		card= new JRadioButton("Card");
        		
        		payment= new ButtonGroup();
        		payment.add(cash);
        		payment.add(card);
        		
        		makePayment= new JButton("Make Payment");
        		cancelPayment= new JButton("Cancel Payment");
        		
        	}
        	
        	private void setFrameProperties() {
        		main= new JFrame("Checkout");
        		main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        		main.setSize(800,500);
        		main.setLocationRelativeTo(null);
        		main.setVisible(true);
        		
        	}
        	
        	private void addComponentsToFrame() {
        		main.add(checkout,BorderLayout.NORTH);
        		main.add(mainPanel,BorderLayout.CENTER);
        	}
        	
        	private void addComponentsToPanel() {
        		mainPanel.add(mainLeft);
        		mainPanel.add(panelSeparator);
        		mainPanel.add(mainRight);
        		
        		mainLeft.add(delivering);
        		mainLeft.add(txtRName);
        		mainLeft.add(txtRAddress);
        		mainLeft.add(packageSeparator);
        		mainLeft.add(packageInfo);
        		mainLeft.add(height);
        		mainLeft.add(txtPHeight);
        		mainLeft.add(length);
        		mainLeft.add(txtPLength);
        		mainLeft.add(width);
        		mainLeft.add(txtPWidth);
        		mainLeft.add(weight);
        		mainLeft.add(txtPWeight);
        		
        		mainRight.add(costInfo);
        		mainRight.add(paymentMethod);
        		mainRight.add(cash);
        		mainRight.add(card);
        		mainRight.add(makePayment);
        		mainRight.add(cancelPayment);
        		
        		costInfo.add(preCost);
        		costInfo.add(txtPBCost);
        		costInfo.add(addCosts);
        		costInfo.add(txtPACost);
        		costInfo.add(orderTotal);
        		costInfo.add(txtPTCost);
        	}
        	
        	
        	public void updateFields() {
        		int invoiceRow = table.getSelectedRow();
        		String trackingNumber = model.getValueAt(invoiceRow, 4).toString();
        		Shipment s = ShipmentDAO.getShipmentByTrackingNumber(trackingNumber);
        		double bCost = 0;
    			double aCost = 0;
        		
        		if(s.packageType.equals(Project.Type.EXPRESS))
        		{
        			bCost = s.getCost()/2.0;
        			aCost = s.getCost() - bCost;
        		}
        		else if(s.packageType.equals(Project.Type.FRAGILE))
        		{
        			bCost = s.getCost()/3.0;
        			aCost = s.getCost() - bCost;
        		}else {
        			bCost = s.getCost();
        		}
        		
            	txtRName.setText(s.getRecipient().getName());
            	txtRAddress.setText(s.getRecipient().getAddress().toString());
            	txtPHeight.setText(String.valueOf(s.getPkg().getHeight()));
            	txtPWeight.setText(String.valueOf(s.getPkg().getWeight()));
            	txtPLength.setText(String.valueOf(s.getPkg().getLength()));
            	txtPWidth.setText(String.valueOf(s.getPkg().getWidth()));
            	txtPBCost.setText(String.valueOf(bCost));
            	txtPACost.setText(String.valueOf(aCost));
            	txtPTCost.setText(String.valueOf(s.getCost()));
            	
            }
        	
        	
        }
        
        private void makePayment()
        {
        	boolean success = false;
        	
        	int shipmentRow = table.getSelectedRow();
        	
        	if (shipmentRow == -1 ) {
                JOptionPane.showMessageDialog(this, "Select a shipment.");
                return;
            }
        	
        	String trackingNumber = model.getValueAt(shipmentRow, 0).toString();
        	
        	Invoice invoice = ShipmentDAO.getInvoiceByTrackingNumber(trackingNumber);
        	
            if (invoice == null) {
                JOptionPane.showMessageDialog(this, "Invoice not found.");
                return;
            }else {
            	success = true;
            }
            if (invoice.getStatus().equals("PAID")) {
                JOptionPane.showMessageDialog(this,
                    "This Shipment is already paid for");
                return;
            }else {
            	success = true;
            }
            
            if(success) {
            	new CheckoutView(invoice);
            }
        }
        
        public void updateInvoice(){
        	int shipmentRow = table.getSelectedRow();
        	String trackingNumber = model.getValueAt(shipmentRow, 0).toString();
        	
        	String newStatus = "PAID";
        	if (newStatus != null) {
                InvoiceDAO.updateInvoiceStatus(trackingNumber, newStatus);
                JOptionPane.showMessageDialog(this,
                        "Invoice paid");
                dispose();
            }
        }
        //Card payment window
        private class CardWindow extends JFrame {
            private JLabel lblBalanceValue;

            private JTextField txtHolderName;
            private JTextField txtCardNumber;
            private JTextField txtExpiryDate;
            private JPasswordField txtCVV;

            private JButton btnMakePayment;
            private JButton btnCancel;

            public CardWindow(Invoice invoice) {
                setTitle("Add Credit Card");
                setSize(450, 420);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                initComponents(invoice);

                double balance = invoice.getBalance();
            	setBalance(balance);
            	setVisible(true);
            }

            private void initComponents(Invoice invoice) {

                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                //Balance Label
                gbc.gridx = 0; gbc.gridy = 0;
                gbc.gridwidth = 1;
                panel.add(new JLabel("Outstanding Balance:"), gbc);

                gbc.gridx = 1;
                lblBalanceValue = new JLabel("$0.00");
                lblBalanceValue.setFont(new Font("Arial", Font.BOLD, 14));
                panel.add(lblBalanceValue, gbc);

                //Card Name
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Card Holder Name:"), gbc);

                gbc.gridx = 1;
                txtHolderName = new JTextField(10);
                panel.add(txtHolderName, gbc);

                //Card Num
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Card Number:"), gbc);

                gbc.gridx = 1;
                txtCardNumber = new JTextField(10);
                panel.add(txtCardNumber, gbc);

                // Expiry date
                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Expiration Date (MM/YY):"), gbc);

                gbc.gridx = 1;
                txtExpiryDate = new JTextField(10);
                panel.add(txtExpiryDate, gbc);

                //CVV
                gbc.gridx = 0; gbc.gridy = 5;
                panel.add(new JLabel("CVV:"), gbc);

                gbc.gridx = 1;
                txtCVV = new JPasswordField(10);
                panel.add(txtCVV, gbc);

               
                btnMakePayment = new JButton("Make Payment");
                btnCancel = new JButton("Cancel");

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(btnMakePayment);
                buttonPanel.add(btnCancel);

                gbc.gridx = 0; 
                gbc.gridy = 6;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(buttonPanel, gbc);

                //Registered listeners
                btnCancel.addActionListener(e -> this.dispose());
                btnMakePayment.addActionListener(e -> {
                	onMakePayment(invoice);     
                	dispose();
                });

                add(panel);
            }

            //balance setter method
            public void setBalance(double amount) {
                lblBalanceValue.setText(String.format("$%.2f", amount));
            }

            //Validation checks
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
                JOptionPane.showMessageDialog(this, "Payment Made!", "Success", JOptionPane.INFORMATION_MESSAGE); 
            }

        }
        //cash payment
        public class ClerkCash {
       	 private JFrame frame;

       	    private JLabel lblBalanceValue;
       	    private JTextField txtAmountToPay;

       	    private JButton btnMakePayment;
       	    private JButton btnCancel;

       	    private double currentBalance = 0.0;

       	    public ClerkCash(Invoice invoice) {
       	        initialize(invoice);
       	        
       	        double balance = invoice.getBalance();
       	        setCBalance(balance);
       	        frame.setVisible(true);
       	    }

       	    private void initialize(Invoice invoice) {
       	        frame = new JFrame("Cash Payment");
       	        frame.setSize(400, 250);
       	        frame.setLocationRelativeTo(null);
       	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

       	        JPanel panel = new JPanel(new GridBagLayout());
       	        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       	        GridBagConstraints gbc = new GridBagConstraints();
       	        gbc.insets = new Insets(8, 8, 8, 8);
       	        gbc.anchor = GridBagConstraints.WEST;
       	        gbc.fill = GridBagConstraints.HORIZONTAL;

       	        //Balance Label
       	        gbc.gridx = 0; gbc.gridy = 0;
       	        panel.add(new JLabel("Outstanding Balance:"), gbc);

       	        gbc.gridx = 1;
       	        lblBalanceValue = new JLabel("$0.00");
       	        lblBalanceValue.setFont(new Font("Arial", Font.BOLD, 14));
       	        panel.add(lblBalanceValue, gbc);

       	        //Amt to be paid 
       	        gbc.gridx = 0; gbc.gridy = 1;
       	        panel.add(new JLabel("Amount to Be Paid:"), gbc);

       	        gbc.gridx = 1;
       	        txtAmountToPay = new JTextField(10);
       	        panel.add(txtAmountToPay, gbc);

       	        //Buttons
       	        btnMakePayment = new JButton("Make Payment");
       	        btnCancel = new JButton("Cancel");

       	        JPanel buttonPanel = new JPanel();
       	        buttonPanel.add(btnMakePayment);
       	        buttonPanel.add(btnCancel);

       	        gbc.gridx = 0;
       	        gbc.gridy = 2;
       	        gbc.gridwidth = 2;
       	        gbc.anchor = GridBagConstraints.CENTER;
       	        panel.add(buttonPanel, gbc);

       	        // Listeners
       	        btnCancel.addActionListener(e -> frame.dispose());
       	        btnMakePayment.addActionListener(e -> onMakePayment(invoice));

       	        frame.add(panel);
       	    }

       	    //Set Balance 
       	    public void setCBalance(double amount) {
       	        this.currentBalance = amount;
       	        lblBalanceValue.setText(String.format("$%.2f", amount));
       	    }

       	    //Payment validation
       	    private void onMakePayment(Invoice invoice) {
       	        String amountStr = txtAmountToPay.getText().trim();

       	        if (!amountStr.matches("\\d+(\\.\\d{1,2})?")) {
       	            JOptionPane.showMessageDialog(frame, "Enter a valid payment amount.",
       	                    "Error", JOptionPane.ERROR_MESSAGE);
       	            return;
       	        }

       	        double amountPaid = Double.parseDouble(amountStr);

       	        if (amountPaid <= 0) {
       	            JOptionPane.showMessageDialog(frame, "Payment amount must be greater than zero.",
       	                    "Error", JOptionPane.ERROR_MESSAGE);
       	            return;
       	        }

       	        // If paying more than the balance stops overflow
       	        if (amountPaid >= currentBalance) {
       	            JOptionPane.showMessageDialog(frame,
       	                    "Payment Made! Full balance paid.",
       	                    "Success", JOptionPane.INFORMATION_MESSAGE);
       	            frame.dispose();
       	            return;
       	        }

       	        // Partial payment
       	        currentBalance -= amountPaid;
       	        lblBalanceValue.setText(String.format("$%.2f", currentBalance));
       	        
       	        String newStatus = "PARTIALLY PAID";
       	        String invoiceNum = String.valueOf(invoice.getInvoiceNum());
       	        double newBalance = currentBalance;
       	        InvoiceDAO.updateInvoiceStatusAndBalance(newStatus, newBalance, invoiceNum);

       	        JOptionPane.showMessageDialog(frame,
       	                "Payment Made! Remaining balance: $" +
       	                        String.format("%.2f", currentBalance),
       	                "Success", JOptionPane.INFORMATION_MESSAGE);

       	        frame.dispose();
       	    }
       }
        
    }
}
