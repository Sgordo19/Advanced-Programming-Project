package Project;

public class Manager extends User
{
    // Primary Constructor
    public Manager(int userID, String firstName, String lastName, String email, String password)
    {
        super(userID, firstName, lastName, email, password, "Manager");
    }

    // Functional Methods 

   // Generate and export report to PDF
	public void generateAndExportReport(String type, Date from, Date to, String pdfPath) {
		ReportService service = new ReportService();
		Report report = null;
		switch (type.toLowerCase()) {
			case "shipment":
				report = service.generateShipmentReport(from, to);
				break;
			case "revenue":
				report = service.generateRevenueReport(from, to);
				break;
			case "delivery":
				report = service.generateDeliveryPerformanceReport(from, to);
				break;
			case "vehicle":
				report = service.generateVehicleUtilizationReport(from, to);
				break;
			default:
				System.out.println("Unknown report type: " + type);
				return;
		}
		if (report != null) {
			reports.add("Generated " + type + " report for: " + from + " to " + to);
			report.printReport();
			report.exportToPDF(pdfPath);
		}
	}

    public void viewVehicleUtilization()
    {
    	// Get generate report from  DB system 
        System.out.println("Displaying vehicle utilization report for all vehicles...");
    }

    public void manageUserAccounts()
    {
    	// Get generate report from  DB system 
        System.out.println("Managing user accounts...");
        System.out.println("(User list retrieved from database)");
    }
}
