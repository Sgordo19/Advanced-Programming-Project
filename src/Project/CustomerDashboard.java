package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    // Buttons
    JButton btnRequestShipment, btnTrackShipments,btnLogout, btnMakePayment;

    // Customer info
    private Customer customer;

    public CustomerDashboard(Customer loggedInUser) {
        this.customer = loggedInUser;

        initializeComponents();
        layoutComponents();
        setWindowProperties();

        btnRequestShipment.addActionListener(e -> openRequestShipment());
        btnTrackShipments.addActionListener(e -> openTrackShipments());
        btnLogout.addActionListener(e -> backToLogin());
    }

    private void initializeComponents() {
        btnRequestShipment = new JButton("Request Shipment");
        btnTrackShipments = new JButton("Track Shipments");
        btnLogout = new JButton("Back to Login");
        
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        gbc.gridy++;
        add(btnRequestShipment, gbc);

        gbc.gridy++;
        add(btnTrackShipments, gbc);

        gbc.gridy++;
        add(btnLogout, gbc);
    }

    private void setWindowProperties() {
        setTitle("Customer Dashboard - " + customer.getFirstName());
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void openRequestShipment() {
    	Shipment s = new Shipment();
    	new ShipmentView(s, customer);
    }

    private void openTrackShipments() {
        new TrackShipmentsView(customer);
    }

    private void backToLogin()
    {
    	this.dispose();
    	new LoginView();
    }

    private class TrackShipmentsView extends JFrame {
        private static final long serialVersionUID = 1L;

        JTable table;
        DefaultTableModel model;

        public TrackShipmentsView(User customer) {
            setTitle("Your Shipments - " + customer.getFirstName());
            setSize(500, 300);
            setLocationRelativeTo(null);

            model = new DefaultTableModel(new String[]{"Tracking Number", "Status"}, 0);
            table = new JTable(model);
            add(new JScrollPane(table), BorderLayout.CENTER);

            loadShipments(customer);
            
            // Buttons panel
            JPanel btnPanel = new JPanel(new FlowLayout());
            btnMakePayment = new JButton("Make Payment");
            btnPanel.add(btnMakePayment);
            add(btnPanel, BorderLayout.SOUTH);

            // Action listeners
            btnMakePayment.addActionListener(e -> makePayment());

            setVisible(true);
        }

        private void loadShipments(User customer) {
        	System.out.println("Fetching shipments for userID: " + customer.getUserID());

            List<Shipment> shipments = ShipmentDAO.getShipmentsByCustomer(customer.getUserID());
            model.setRowCount(0);

            for (Shipment s : shipments) {
            	String statusDisplay = s.getStatus() != null ? s.getStatus().getDisplayName() : "Unknown";
                model.addRow(new Object[]{s.getTrackingNumber(), statusDisplay});
            }
        }
        
        private class CheckoutView extends JFrame{
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
        	
        	
        	public CheckoutView()  {
        		initialize();
        	}
        	
        	private void initialize() {
        		setFrameProperties();
        		initialComponents();
        		addComponentsToFrame();
        		addComponentsToPanel();
        		SwingUtilities.invokeLater(this::updateFields);
        		
        		 makePayment.addActionListener(e -> updateInvoice());
        		
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
        		int shipmentRow = table.getSelectedRow();
        		String trackingNumber = model.getValueAt(shipmentRow, 0).toString();
        		Shipment s = ShipmentDAO.getShipmentByTrackingNumber(trackingNumber);
        		double bCost = 0;
    			double aCost = 0;
        		
        		if(s.packageType.equals(Project.Type.EXPRESS))
        		{
        			bCost = s.getCost()/2.0;
        			aCost = s.getCost() - bCost;
        		}
        		if(s.packageType.equals(Project.Type.FRAGILE))
        		{
        			bCost = s.getCost()/3.0;
        			aCost = s.getCost() - bCost;
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
            	new CheckoutView();
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
        
    }
}


