package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    // Buttons
    JButton btnRequestShipment, btnTrackShipments,btnLogout;

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
    }
}


