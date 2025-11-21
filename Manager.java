package smartship;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;


public class Manager extends User
{
	private List<User> managedUsers;
	private List<String> managedVehicles;
	private List<String> reports;
	
	// Primary Constructor
	public Manager(int userID, String firstName, String lastName, String email, String password)
	{
		super(userID, firstName, lastName, email, password, "Manager");
		this.managedUsers = new ArrayList<>();
		this.managedVehicles = new ArrayList<>();
		this.reports = new ArrayList<>();	
	}

	//Getters and Setters
	public List<User> getManagedUsers() 
	{
		return managedUsers;
	}

	public void setManagedUsers(List<User> managedUsers) 
	{
		this.managedUsers = managedUsers;
	}

	public List<String> getManagedVehicles() 
	{
		return managedVehicles;
	}

	public void setManagedVehicles(List<String> managedVehicles) 
	{
		this.managedVehicles = managedVehicles;
	}

	public List<String> getReports() 
	{
		return reports;
	}

	public void setReports(List<String> reports) 
	{
		this.reports = reports;
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
        System.out.println("Displaying vehicle utilization report for all vehicles...");
    }

    public void manageUserAccounts()
    {
        System.out.println("Managing user accounts...");
        for (User u : managedUsers)
        {
            System.out.println("- " + u.getFirstName() + " " + u.getLastName() + " (" + u.getRole() + ")");
        }
    }
}
