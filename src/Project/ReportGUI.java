package Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class ReportGUI extends JFrame {

    private JComboBox<String> reportTypeCombo;
    private JComboBox<String> timeTypeCombo;
    private JTextField fromDateField, toDateField, pdfPathField;
    private JTextArea outputArea;
    private Manager manager;
    private JButton btnLogout;
    
    // Navigation stack to track opened frames
    private Stack<JFrame> frameStack = new Stack<>();

    public ReportGUI(Manager manager) {
        this.manager = manager;
        
        // Push the dashboard itself onto the stack
        frameStack.push(this);

        initializeComponents();
        layoutComponents();
        setWindowProperties();
        
        setVisible(true);
    }

    private void initializeComponents() {
        // Report components
        reportTypeCombo = new JComboBox<>(new String[] { "Shipment", "Revenue", "Delivery", "Vehicle" });
        timeTypeCombo = new JComboBox<>(new String[] { "Daily", "Weekly", "Monthly"});
        fromDateField = new JTextField();
        pdfPathField = new JTextField();
        outputArea = new JTextArea();
        
        // Navigation button
        btnLogout = new JButton("Logout");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setContentPane(createContentPanel());
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel();
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL - Navigation =====
        JPanel navPanel = new JPanel(new BorderLayout());
        
        // Title with manager name
        JLabel titleLabel = new JLabel("Reporting Module - " + manager.getFirstName(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        navPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Navigation buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnLogout);
        navPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        content.add(navPanel, BorderLayout.NORTH);

        // ===== MAIN CONTENT PANEL =====
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // ===== REPORT CONTROLS PANEL =====
        JPanel controlsPanel = new JPanel(new GridLayout(6, 2, 8, 8));

        controlsPanel.add(new JLabel("Select Report Type:"));
        controlsPanel.add(reportTypeCombo);
        
        controlsPanel.add(new JLabel("Select Report Time Period:"));
        controlsPanel.add(timeTypeCombo);

        controlsPanel.add(new JLabel("Start Date (yyyy-MM-dd):"));
        controlsPanel.add(fromDateField);

        controlsPanel.add(new JLabel("Save PDF As:"));
        pdfPathField.setEditable(false);
        controlsPanel.add(pdfPathField);

        JButton browseBtn = new JButton("Browse");
        browseBtn.addActionListener(e -> selectFilePath());
        controlsPanel.add(browseBtn);

        // Add empty cell for alignment
        controlsPanel.add(new JLabel());

        mainPanel.add(controlsPanel, BorderLayout.NORTH);

        // ===== OUTPUT AREA =====
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setPreferredSize(new Dimension(580, 200));
        mainPanel.add(scroll, BorderLayout.CENTER);

        // ===== GENERATE BUTTON =====
        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setFont(new Font("Arial", Font.BOLD, 15));
        generateBtn.addActionListener(e -> generateReport());
        mainPanel.add(generateBtn, BorderLayout.SOUTH);

        content.add(mainPanel, BorderLayout.CENTER);

        return content;
    }

    private void setWindowProperties() {
        setTitle("Reporting Module - " + manager.getFirstName());
        setSize(600, 600); // Increased height to accommodate navigation
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add action listeners
        btnLogout.addActionListener(e -> backToLogin());
        
        // Add window listener to handle closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                navigateBack();
            }
        });
    }

    private void selectFilePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save PDF Report");
        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            pdfPathField.setText(chooser.getSelectedFile().getAbsolutePath() + ".pdf");
        }
    }

    private void generateReport() {
        try {
            String type = (String) reportTypeCombo.getSelectedItem();
            String period = (String) timeTypeCombo.getSelectedItem();
            String pdfPath = pdfPathField.getText();

            if (pdfPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please choose a location to save the PDF.");
                return;
            }

            Date from = parseDate(fromDateField.getText());
            Date to = from;

            Calendar cal = Calendar.getInstance();
            cal.setTime(from);

            // Adjust 'to' date based on period
            switch (period) {
                case "Weekly":
                    cal.add(Calendar.DATE, 6); // 7-day period
                    to = cal.getTime();
                    break;
                case "Monthly":
                    cal.add(Calendar.MONTH, 1);
                    cal.add(Calendar.DATE, -1); // end of month
                    to = cal.getTime();
                    break;
                default: // Daily
                    to = from;
                    break;
            }

            Report<?> report;
            switch (type) {
                case "Shipment":
                    report = ReportService.generateShipmentReport(from, to);
                    break;
                case "Delivery":
                    report = ReportService.generateDeliveryPerformanceReport(from, to);
                    break;
                case "Revenue":
                    report = ReportService.generateRevenueReport(from, to);
                    break;
                case "Vehicle":
                    report = ReportService.generateVehicleUtilizationReport();
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Unknown report type selected.");
                    return;
            }

            report.exportToPDF(pdfPath);

            // Show preview in text area
            outputArea.append("Generated " + type + " Report\n");
            outputArea.append("Time Period: " + period + "\n");
            outputArea.append("From: " + from + "\nTo: " + to + "\n");
            outputArea.append("Saved PDF: " + pdfPath + "\n\n");

            JOptionPane.showMessageDialog(this, type + " report generated successfully!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        clearFields();
    }

    private void clearFields() {
        // Reset combo boxes to first item
        reportTypeCombo.setSelectedIndex(0);
        timeTypeCombo.setSelectedIndex(0);
        
        // Clear text fields
        fromDateField.setText("");
        pdfPathField.setText("");
        
        // Clear output area 
        outputArea.setText("");
    }

    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    //Navigates back to the previous frame in the navigation stack
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
            // If this is the only frame, go back to login
            backToLogin();
        }
    }

    //Returns to login screen and closes all frames
    private void backToLogin() {
        // Close all frames in the stack
        while (!frameStack.isEmpty()) {
            JFrame frame = frameStack.pop();
            frame.dispose();
        }
        new LoginView();
    }
    
    //Method to add frames to the navigation stack from child components
    public void addToFrameStack(JFrame frame) {
        frameStack.push(frame);
    }
    
    //Method to refresh the report data
    public void refreshData() {
        clearFields();
        outputArea.setText("Ready to generate new report...\n");
    }
    
    //Override dispose to handle navigation properly
    @Override
    public void dispose() {
        // Remove this frame from stack before disposing
        if (!frameStack.isEmpty() && frameStack.peek() == this) {
            frameStack.pop();
        }
        super.dispose();
    }
}