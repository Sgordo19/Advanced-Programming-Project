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
        		    	JOptionPane.showMessageDialog(null, "Please make payment with clerk at the front desk");
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

                setVisible(true);
                
                int shipmentRow = table.getSelectedRow();
        		String trackingNumber = model.getValueAt(shipmentRow, 0).toString();
                Shipment s = ShipmentDAO.getShipmentByTrackingNumber(trackingNumber);
            	setBalance(s.getCost());
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
                btnCancel.addActionListener(e -> dispose());
                btnMakePayment.addActionListener(e -> {
                    onMakePayment(invoice);  
                    dispose();               //close the frame
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
        
    }
}


