package Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportGUI extends JFrame {

    private JComboBox<String> reportTypeCombo;
    private JTextField fromDateField, toDateField, pdfPathField;
    private JTextArea outputArea;
    private Manager manager;

    public ReportGUI(Manager manager) {
        this.manager = manager;

        setTitle("Reporting Module");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.setLayout(new BorderLayout(10, 10));
        setContentPane(content);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new GridLayout(5, 2, 8, 8));

        topPanel.add(new JLabel("Select Report Type:"));
        reportTypeCombo = new JComboBox<>(new String[]{
                "Shipment",
                "Revenue",
                "Delivery",
                "Vehicle"
        });
        topPanel.add(reportTypeCombo);

        topPanel.add(new JLabel("From Date (yyyy-MM-dd):"));
        fromDateField = new JTextField();
        topPanel.add(fromDateField);

        topPanel.add(new JLabel("To Date (yyyy-MM-dd):"));
        toDateField = new JTextField();
        topPanel.add(toDateField);

        topPanel.add(new JLabel("Save PDF As:"));
        pdfPathField = new JTextField();
        topPanel.add(pdfPathField);

        JButton browseBtn = new JButton("Browse");
        browseBtn.addActionListener(e -> selectFilePath());
        topPanel.add(browseBtn);

        content.add(topPanel, BorderLayout.NORTH);

        // ===== OUTPUT AREA =====
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setPreferredSize(new Dimension(580, 200));
        content.add(scroll, BorderLayout.CENTER);

        // ===== GENERATE BUTTON =====
        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setFont(new Font("Arial", Font.BOLD, 15));
        generateBtn.addActionListener(e -> generateReport());
        content.add(generateBtn, BorderLayout.SOUTH);
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
            Date from = parseDate(fromDateField.getText());
            Date to = parseDate(toDateField.getText());
            String pdfPath = pdfPathField.getText();

            if (pdfPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please choose a location to save the PDF.");
                return;
            }

            // Call your Manager’s backend logic
            manager.generateAndExportReport(type, from, to, pdfPath);

            // Show preview
            outputArea.append("Generated " + type + " Report\n");
            outputArea.append("From: " + from + "\n");
            outputArea.append("To:   " + to + "\n");
            outputArea.append("Saved PDF: " + pdfPath + "\n\n");

            JOptionPane.showMessageDialog(this,
                    type + " report generated successfully!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error generating report: " + ex.getMessage());
        }
    }

    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    // For testing GUI alone:
    public static void main(String[] args) {
        Manager test = new Manager(1, "John", "Doe", "john@x.com", "pass123");
        new ReportGUI(test).setVisible(true);
    }
}
