package view;

import models.*;
import dbFactories.*;
import controller.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Stack;

public class CustomerDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    // Buttons
    JButton btnRequestShipment, btnTrackShipments, btnLogout, btnMakePayment;
    
    // Navigation stack to track opened frames
    private Stack<JFrame> frameStack = new Stack<>();
    
    // Customer info
    private Customer customer;

    public CustomerDashboard(Customer loggedInUser) {
        this.customer = loggedInUser;
        
        // Push the dashboard itself onto the stack
        frameStack.push(this);

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
        setSize(350, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void openRequestShipment() {
        Shipment s = new Shipment();
        ShipmentView shipmentView = new ShipmentView(s, customer);
        frameStack.push(shipmentView);
    }

    private void openTrackShipments() {
        TrackShipmentsView trackView = new TrackShipmentsView(customer);
        frameStack.push(trackView);
    }

    private void backToLogin() {
        // Close all frames in the stack
        while (!frameStack.isEmpty()) {
            JFrame frame = frameStack.pop();
            frame.dispose();
        }
        new LoginView();
    }
    
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
    
    // Method to add frames to the navigation stack from child components
    public void addToFrameStack(JFrame frame) {
        frameStack.push(frame);
    }

    private class TrackShipmentsView extends JFrame {
        private static final long serialVersionUID = 1L;

        JTable table;
        DefaultTableModel model;
        JButton btnBack;
        private ProfessionalCheckoutView currentCheckoutView;

        public TrackShipmentsView(User customer) {
            setTitle("Your Shipments - " + customer.getFirstName());
            setSize(500, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            model = new DefaultTableModel(new String[]{"Tracking Number", "Status"}, 0);
            table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Your Shipments"));
            add(scrollPane, BorderLayout.CENTER);

            loadShipments(customer);
            
            // Buttons panel
            JPanel btnPanel = new JPanel(new BorderLayout());
            JPanel actionPanel = new JPanel(new FlowLayout());
            btnMakePayment = new JButton("Make Payment");
            actionPanel.add(btnMakePayment);
            
            btnBack = new JButton("Back");
            JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            backPanel.add(btnBack);
            
            btnPanel.add(backPanel, BorderLayout.WEST);
            btnPanel.add(actionPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);

            // Action listeners
            btnMakePayment.addActionListener(e -> makePayment());
            btnBack.addActionListener(e -> navigateBackFromTrack());

            setVisible(true);
        }
        
        private void navigateBackFromTrack() {
            dispose();
            CustomerDashboard.this.navigateBack();
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
        
        // Professional Checkout View
        public class ProfessionalCheckoutView extends JFrame {
            private static final long serialVersionUID = 1L;
            
            private Invoice invoice;
            private TrackShipmentsView parentView;
            
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
            
            public ProfessionalCheckoutView(Invoice invoice, TrackShipmentsView parentView) {
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
                    
                    if (s.packageType.equals(models.Type.EXPRESS)) {
                        bCost = s.getCost() / 2.0;
                        aCost = s.getCost() - bCost;
                    } else if (s.packageType.equals(models.Type.FRAGILE)) {
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
                    JOptionPane.showMessageDialog(this, 
                        "Please make payment with clerk at the front desk.\n\n" +
                        "Bring your invoice number: " + invoice.getInvoiceNum() + "\n" +
                        "Total Amount: " + txtTotalCost.getText(),
                        "Cash Payment Instructions", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else if (rbCard.isSelected()) {
                    CustomerCardWindow cardWindow = new CustomerCardWindow(invoice, this);
                    CustomerDashboard.this.addToFrameStack(cardWindow);
                }
            }
            
            private void navigateBackFromCheckout() {
                dispose();
                CustomerDashboard.this.navigateBack();
            }
            
            public void refreshParent() {
                parentView.loadShipments(customer);
            }
        }

        // Customer Card Window
        private class CustomerCardWindow extends JFrame {
            private static final long serialVersionUID = 1L;
            
            private JLabel lblBalanceValue;
            private JTextField txtHolderName, txtCardNumber, txtExpiryDate;
            private JPasswordField txtCVV;
            private JButton btnMakePayment, btnCancel, btnBack;
            private ProfessionalCheckoutView parentCheckout;

            public CustomerCardWindow(Invoice invoice, ProfessionalCheckoutView parentCheckout) {
                this.parentCheckout = parentCheckout;
                setTitle("Credit Card Payment");
                setSize(450, 470);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                initComponents(invoice);
                setVisible(true);
                
                // Set balance from shipment cost
                String trackingNumber = invoice.getTrackingNumber();
                Shipment s = ShipmentDAO.getShipmentByTrackingNumber(trackingNumber);
                if (s != null) {
                    setBalance(s.getCost());
                }
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
                panel.add(new JLabel("Total Amount:"), gbc);
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
                
                // Refresh parent view and close checkout
                parentCheckout.refreshParent();
                parentCheckout.dispose();
                
                // Update the track shipments view
                updateInvoice();
            }
        }
        
        private void makePayment() {
            boolean success = false;
            
            int shipmentRow = table.getSelectedRow();
            
            if (shipmentRow == -1 ) {
                JOptionPane.showMessageDialog(this, "Please select a shipment to make payment.");
                return;
            }
            
            String trackingNumber = model.getValueAt(shipmentRow, 0).toString();
            
            Invoice invoice = ShipmentDAO.getInvoiceByTrackingNumber(trackingNumber);
            
            if (invoice == null) {
                JOptionPane.showMessageDialog(this, "Invoice not found for this shipment.");
                return;
            } else {
                success = true;
            }
            if (invoice.getStatus().equals("PAID")) {
                JOptionPane.showMessageDialog(this, "This shipment is already paid for.");
                return;
            } else {
                success = true;
            }
            
            if(success) {
                ProfessionalCheckoutView checkoutView = new ProfessionalCheckoutView(invoice, this);
                currentCheckoutView = checkoutView;
                CustomerDashboard.this.addToFrameStack(checkoutView);
            }
        }
        
        public void updateInvoice() {
            // Refresh the shipments table after payment
            loadShipments(customer);
            JOptionPane.showMessageDialog(this, "Payment completed successfully! Shipment status updated.", "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}