package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class ManagerDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    // Buttons
    JButton btnShipmentReport, btnRevenueReport, btnDeliveryReport, btnVehicleUtilReport,btnBackToLogin;

    // Manager reference
    private Manager manager;

    public ManagerDashboard(Manager loggedInManager) {
        this.manager = loggedInManager;

        initializeComponents();
        layoutComponents();
        setWindowProperties();

        // Add events
        btnShipmentReport.addActionListener(e -> openShipmentReport());
        btnRevenueReport.addActionListener(e -> openRevenueReport());
        btnDeliveryReport.addActionListener(e -> openDeliveryPerformanceReport());
        btnVehicleUtilReport.addActionListener(e -> openVehicleUtilizationReport());
        btnBackToLogin.addActionListener(e -> backToLogin());
    }
    
    //Buttons
    private void initializeComponents() {
        btnShipmentReport = new JButton("Shipment Report");
        btnRevenueReport = new JButton("Revenue Report");
        btnDeliveryReport = new JButton("Delivery Performance");
        btnVehicleUtilReport = new JButton("Vehicle Utilization");
        btnBackToLogin = new JButton("Back to Login");
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 
        gbc.gridy = 0;

        add(btnShipmentReport, gbc);

        gbc.gridy++;
        add(btnRevenueReport, gbc);

        gbc.gridy++;
        add(btnDeliveryReport, gbc);

        gbc.gridy++;
        add(btnVehicleUtilReport, gbc);
        gbc.gridy++;
        add(btnBackToLogin, gbc);
    }

    private void setWindowProperties() {
        setTitle("Manager Dashboard - " + manager.getFirstName());
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
    
    // Return to login screen
        private void backToLogin() 
        {
        	this.dispose(); 
        	new LoginView();
        }

    // OPEN SUB-WINDOWS
   
    private void openShipmentReport() {
        new ReportViewWindow("Shipment Report", ReportTypeEnum.SHIPMENT);
    }

    private void openRevenueReport() {
        new ReportViewWindow("Revenue Report", ReportTypeEnum.REVENUE);
    }

    private void openDeliveryPerformanceReport() {
        new ReportViewWindow("Delivery Performance", ReportTypeEnum.DELIVERY);
    }

    private void openVehicleUtilizationReport() {
        new ReportViewWindow("Vehicle Utilization", ReportTypeEnum.VEHICLE);
    }

  
    // INNER CLASS: VIEW REPORT WINDOW (REUSABLE FOR ALL REPORT TYPES)


    private class ReportViewWindow extends JFrame {

        private static final long serialVersionUID = 1L;

        JTable table;
        DefaultTableModel model;

        public ReportViewWindow(String title, ReportTypeEnum reportType) {
            setTitle(title);
            setSize(600, 350);
            setLocationRelativeTo(null);

            model = new DefaultTableModel(new String[]{"Metric", "Value"}, 0);
            table = new JTable(model);

            add(new JScrollPane(table), BorderLayout.CENTER);
            loadReport(reportType);

            setVisible(true);
        }

        private void loadReport(ReportTypeEnum type) {
            ReportService service = new ReportService();

            // Example date range — you can change to a date picker later
            Date from = Date.valueOf("2025-01-01");
            Date to = Date.valueOf("2025-12-31");

            Report report;

            switch (type) {
                case SHIPMENT:
                    report = service.generateShipmentReport(from, to);
                    break;
                case REVENUE:
                    report = service.generateRevenueReport(from, to);
                    break;
                case DELIVERY:
                   report = service.generateDeliveryPerformanceReport(from, to);
                    break;
                case VEHICLE:
                    report = service.generateVehicleUtilizationReport(to, to);
                    break;
                default:
                    return;
            }

            model.setRowCount(0);
            for (List entry : report.getEntries()) {
                model.addRow(new Object[]{
                        entry.getMetric_name(),
                        entry.getMetric_value()
                });
            }
        }
    }

    // Helper enum so we don't use string literals everywhere
    private enum ReportTypeEnum {
        SHIPMENT, REVENUE, DELIVERY, VEHICLE
    }
}