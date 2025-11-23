package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DriverDashboard extends JFrame {
    private JTable routesTable, shipmentsTable;
    private DefaultTableModel routesModel, shipmentsModel;
    private JLabel lblVehicleInfo;
    private User driver;

    public DriverDashboard(User driver) {
        this.driver = driver;

        setTitle("Driver Dashboard - " + driver.getFirstName());
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Vehicle info label
        lblVehicleInfo = new JLabel("Vehicle info will appear here");
        add(lblVehicleInfo, BorderLayout.NORTH);

        // Routes table
        routesModel = new DefaultTableModel(new String[]{"Route ID", "Route Name", "Start", "End"}, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // routes table is read-only
            }
        };
        routesTable = new JTable(routesModel);

        // Shipments table
        shipmentsModel = new DefaultTableModel(new String[]{"Tracking #", "Recipient Name", "Address", "Weight", "Status"}, 0);
        shipmentsTable = new JTable(shipmentsModel);

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

        loadDriverData();

        setVisible(true);
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
            Vehicle vehicle = VehicleDAO.getVehicleById(vehicleId);
            lblVehicleInfo.setText("Vehicle ID: " + vehicle.getVehicle_id() +
                    ", Status: " + vehicle.getV_status() +
                    ", Max Weight: " + vehicle.getMax_weight() +
                    ", Max Packages: " + vehicle.getMax_quantity());

            // Get shipments for this vehicle
            List<Shipment> shipments = ShipmentDAO.getShipmentsByVehicle(vehicleId);
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
}
