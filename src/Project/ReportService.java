package Project;

import java.util.Date;
import java.util.Random;

public class ReportService {

	private int report_counter = 1000;
	private Random rand = new Random();

	// Utility method
	private int generateID() {
		return report_counter++;
	}

	public Report generateShipmentReport(Date from, Date to) {
		Report report = new Report(generateID(), from, to, new ReportType(1, "Shipment Report"));
		report.addEntry(new ReportEntry(1, "Total Shipments", rand.nextInt(200)));
		return report;
	}

	public Report generateRevenueReport(Date from, Date to) {
		Report report = new Report(generateID(), from, to, new ReportType(2, "Revenue Report"));
		report.addEntry(new ReportEntry(1, "Total Revenue", rand.nextInt(500000)));
		return report;
	}

	public Report generateDeliveryPerformanceReport(Date from, Date to) {
		Report report = new Report(generateID(), from, to, new ReportType(3, "Delivery Performance"));
		report.addEntry(new ReportEntry(1, "On-time Deliveries (%)", rand.nextDouble() * 100));
		return report;
	}

	public Report generateVehicleUtilizationReport(Date from, Date to) {
		Report report = new Report(generateID(), from, to, new ReportType(4, "Vehicle Utilization"));
		report.addEntry(new ReportEntry(1, "Average Load (%)", rand.nextDouble() * 100));
		return report;
	}
}
