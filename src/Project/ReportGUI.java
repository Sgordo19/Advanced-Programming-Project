package Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ReportGUI extends JFrame {

	private JComboBox<String> reportTypeCombo;
	private JComboBox<String> timeTypeCombo;
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
		JPanel topPanel = new JPanel(new GridLayout(6, 2, 8, 8));

		topPanel.add(new JLabel("Select Report Type:"));
		reportTypeCombo = new JComboBox<>(new String[] { "Shipment", "Revenue", "Delivery", "Vehicle" });
		topPanel.add(reportTypeCombo);
		
		topPanel.add(new JLabel("Select Report Time Period:"));
		timeTypeCombo = new JComboBox<>(new String[] { "Daily", "Weekly", "Monthly"});
		topPanel.add(timeTypeCombo);

		topPanel.add(new JLabel("Start Date (yyyy-MM-dd):"));
		fromDateField = new JTextField();
		topPanel.add(fromDateField);


		topPanel.add(new JLabel("Save PDF As:"));
		pdfPathField = new JTextField();
		topPanel.add(pdfPathField);
		pdfPathField.setEditable(false);

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
		
		setVisible(true);
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
		Report<Shipment> r = new Report<>();
		
		try {
			String type = (String) reportTypeCombo.getSelectedItem();
			String period = (String) timeTypeCombo.getSelectedItem();
			Date start = parseDate(fromDateField.getText());
			String pdfPath = pdfPathField.getText();
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


			if (pdfPath.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please choose a location to save the PDF.");
				return;
			}

			// Call report
			if (period.equals("Daily")) 
			{
				
				r.setReport_id(0);
				r.setDate_from(start);
				r.setDate_to(start);
				r.setType(type);
				r.setGenerated_at(parseDate(sdf.format(today)));
				List<Shipment> shipments = ShipmentDAO.getShipmentsByDate(start);
				for (Shipment s : shipments) {
				    r.addEntry(s);
				}
				
				r.exportToPDF(pdfPath);
			}

			// Show preview
			
			outputArea.append("Generated " + type + " Report\n");
			outputArea.append("Time Period:   " + period + "\n");
			outputArea.append("Start date: " + start + "\n");
			outputArea.append("Saved PDF: " + pdfPath + "\n\n");

			JOptionPane.showMessageDialog(this, type + " report generated successfully!");

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage());
		}
	}

	private Date parseDate(String date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	}

}
