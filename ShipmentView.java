package view;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import ga.*;
import ga.Package;

public class ShipmentView extends JFrame implements ActionListener {

    // Labels
    JLabel tNumber, packageInfo, packageType, cost, status, cDate, dDate;
    JLabel cName, cID, rName, rAdd, rPhone, rZone;
    JLabel pWeight, pLength, pWidth, pHeight;

    // Fields
    JTextField txtTNumber, txtPInfo, txtCost, txtStatus, txtCDate, txtDDate;
    JTextField txtCName, txtCID, txtRName, txtRAdd, txtRPhone;
    JTextField txtPWeight, txtPLength, txtPWidth, txtPHeight;
    JComboBox<String> comboType;
    JComboBox<Integer> comboZone;

    JButton saveBtn;
    Shipment s;
    private Customer currentCustomer;

    public ShipmentView(Shipment s) {
        this.s = s;
        initializeComponents();
        layoutComponents();
        setWindowProperties();
        SwingUtilities.invokeLater(this::updateFields);
        
        //add customer cid (check for logged in customer)
        autoPopulateCID();
        
        //document listeners
        txtPWeight.getDocument().addDocumentListener(calculateCostListener);
        comboZone.addActionListener(e -> updateCost());
        comboType.addActionListener(e -> updateCost());
    }

    private void initializeComponents() {
        // Labels
        tNumber = new JLabel("Tracking Number:");
        packageInfo = new JLabel("Package:");
        packageType = new JLabel("Shipment Type:");
        cost = new JLabel("Total Cost:");
        status = new JLabel("Status:");
        cDate = new JLabel("Creation Date:");
        dDate = new JLabel("Estimated Delivery Date:");

        cName = new JLabel("Name:");
        cID = new JLabel("CID:");
        rName = new JLabel("Recipient Name:");
        rAdd = new JLabel("Recipient Address:");
        rPhone = new JLabel("Recipient Phone:");
        rZone = new JLabel("Recipient Zone:");
        
        pWeight = new JLabel("Weight (cm):");
        pLength = new JLabel("Length (cm):");
        pWidth = new JLabel("Width (cm):");
        pHeight = new JLabel("Height (cm):");

        // Text fields
        txtTNumber = new JTextField();
        txtPWeight = new JTextField();
        txtPLength = new JTextField();
        txtPWidth = new JTextField();
        txtPHeight = new JTextField();
        txtCost = new JTextField();
        txtStatus = new JTextField();
        txtCDate = new JTextField();
        txtDDate = new JTextField();
        txtCName = new JTextField();
        txtCID = new JTextField();
        txtRName = new JTextField();
        txtRAdd = new JTextField();
        txtRPhone = new JTextField();
        
        //read-only
        txtTNumber.setEditable(false);
        txtTNumber.setForeground(Color.BLUE);
        txtStatus.setEditable(false);
        txtStatus.setForeground(Color.BLUE);
        txtCDate.setEditable(false);
        txtCDate.setForeground(Color.BLUE);
        txtDDate.setEditable(false);
        txtDDate.setForeground(Color.BLUE);
        txtCost.setEditable(false);
        txtCost.setForeground(Color.BLUE);
        txtCID.setEditable(false);
        txtCID.setForeground(Color.BLUE);
        txtCName.setEditable(false);
        txtCName.setForeground(Color.BLUE);

        // Combo boxes
        comboType = new JComboBox<>(new String[]{"Standard Delivery", "Express Delivery", "Fragile Handling"});
        comboZone = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        // Button
        saveBtn = new JButton("Create Shipment");
        saveBtn.addActionListener(this);
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Customer Panel
        JPanel customerPanel = new JPanel(new GridBagLayout());
        customerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Customer Information", TitledBorder.CENTER, TitledBorder.TOP));
        addLabelField(customerPanel, cID, txtCID, 0);
        addLabelField(customerPanel, cName, txtCName, 1);
        

        // Recipient Panel 
        JPanel recipientPanel = new JPanel(new GridBagLayout());
        recipientPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Recipient Information", TitledBorder.CENTER, TitledBorder.TOP));
        addLabelField(recipientPanel, rName, txtRName, 0);
        addLabelField(recipientPanel, rAdd, txtRAdd, 1);
        addLabelField(recipientPanel, rPhone, txtRPhone, 2);
        addLabelField(recipientPanel, rZone, comboZone, 3);

        // Set Customer and Recipient side by side 
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        add(customerPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.5;
        add(recipientPanel, gbc);

        row++;

        // Package Panel
        JPanel packagePanel = new JPanel(new GridBagLayout());
        packagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Package Information", TitledBorder.CENTER, TitledBorder.TOP));
        addLabelField(packagePanel, tNumber, txtTNumber, 0);
        addLabelField(packagePanel, pWeight, txtPWeight, 1);
        addLabelField(packagePanel, pLength, txtPLength, 2);
        addLabelField(packagePanel, pWidth, txtPWidth, 3);
        addLabelField(packagePanel, pHeight, txtPHeight, 4);
        addLabelField(packagePanel, packageType, comboType, 5);
        addLabelField(packagePanel, cost, txtCost, 6);
        addLabelField(packagePanel, status, txtStatus, 7);
        addLabelField(packagePanel, cDate, txtCDate, 8);
        addLabelField(packagePanel, dDate, txtDDate, 9);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(packagePanel, gbc);

        //Save Button
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);
        add(saveBtn, gbc);
    }

    private void addLabelField(JPanel panel, JLabel label, JComponent field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(field, gbc);
    }

    private void setWindowProperties() {
        setTitle("Shipment");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Shipment ship = new Shipment();

            // Tracking info
            ship.setTrackingNumber(txtTNumber.getText());
            ship.setCreationDate(txtCDate.getText());
            ship.setDeliveryDate(txtDDate.getText());
            ship.setStatus(ga.Status.PENDING);

            // Sender - Only need CID for sender_id
            Customer sender = new Customer();
            sender.setCID(txtCID.getText()); // This will be used as sender_id in database
            sender.setName(txtCName.getText());

            // We don't need sender address for the database, but keep it for the object
            Destination senderDest = new Destination();
            senderDest.setAddress("N/A");
            senderDest.setZone(Zone.getZoneById((Integer) comboZone.getSelectedItem()));
            sender.setAddress(senderDest);
            ship.setSender(sender);

            // Recipient
            Customer recipient = new Customer();
            recipient.setName(txtRName.getText());
            recipient.setPhone(txtRPhone.getText());

            Destination recDest = new Destination();
            recDest.setAddress(txtRAdd.getText());
            recDest.setZone(Zone.getZoneById((Integer) comboZone.getSelectedItem()));
            recipient.setAddress(recDest);
            ship.setRecipient(recipient);

            // Package
            Package pkg = new Package();
            pkg.setWeight(Double.parseDouble(txtPWeight.getText()));
            pkg.setLength(Double.parseDouble(txtPLength.getText()));
            pkg.setWidth(Double.parseDouble(txtPWidth.getText()));
            pkg.setHeight(Double.parseDouble(txtPHeight.getText()));
            pkg.setDestination(recDest);
            ship.setPkg(pkg);

            // Shipment Type
            String typeStr = (String) comboType.getSelectedItem();
            ship.setPackageType(ga.Type.fromDescription(typeStr));

            // distance + cost
            int zoneId = (Integer) comboZone.getSelectedItem();
            ship.setDistance(ship.calculateDistance(zoneId));
            ship.setCost(ship.calculateCost(pkg.getWeight(), ship.getDistance(), typeStr));

            // Debugging
            System.out.println("Shipment object created: " + ship.getTrackingNumber());

            // Save to database
            boolean success = ShipmentDAO.saveShipment(ship);

            if (success) {
                JOptionPane.showMessageDialog(this, "Shipment saved successfully!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save shipment");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void updateFields() {
        txtTNumber.setText(Shipment.generateTrackingNumber(Shipment.getNextTrackingSequence()));
        txtStatus.setText("Pending");
        txtCDate.setText(s.generateCreationDate());
        txtDDate.setText(s.generateDeliveryDate());
    }
    
    DocumentListener calculateCostListener = new DocumentListener() 
    {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateCost();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateCost();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateCost();
        }
    };
    
    private void autoPopulateCID() {
        //getting the first customer from database
        currentCustomer = CustomerDAO.getFirstCustomer();
        
        if (currentCustomer != null) {
            // Populate the CID  and name field
            txtCID.setText(currentCustomer.getCID());
            txtCName.setText(currentCustomer.getName());
            
        } else {
            System.out.println("No customers found in database");
        }
    }
    
    private void updateCost()
    {
    	try {
    		int selectedZone = (Integer)comboZone.getSelectedItem();
        	if(txtPWeight == null)return;
    		double weight = Double.parseDouble(txtPWeight.getText());
        	String type = (String)comboType.getSelectedItem();
        	double cost = s.calculateCost(weight , s.calculateDistance(selectedZone), type);
        	txtCost.setText(String.valueOf(cost));
    	}catch(NumberFormatException e) 
    	{
            txtCost.setText("");
    	}
    }
}
