package smartship;

import java.util.ArrayList;
import java.util.List;

public class Manager extends User
{
	private List<User> managedUsers;
	private List<String> managedVehicles;
	private List<String> reports;
	
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
	
	
	public void generateReport(String type, String dateRange) 
	{
        String report = "Generated " + type + " report for " + dateRange;
        reports.add(report);
        System.out.println(report);
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
            System.out.println("- " + u.getLastName() + " (" + u.getRole() + ")");
        }
    }
	
	public void manageFleet() {
        System.out.println("Managing vehicle fleet...");
        for (String vehicle : managedVehicles) {
            System.out.println("- Vehicle ID: " + vehicle);
        }
    }
	
	@Override
	public void viewDasboard() 
	{
		System.out.println("Manager Dashboard - Reports, Fleet Management, and User Oversight.");
		
	}
	
 
}
