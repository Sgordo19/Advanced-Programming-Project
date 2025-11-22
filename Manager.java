package Project;

public class Manager extends User
{
    // Primary Constructor
    public Manager(int userID, String firstName, String lastName, String email, String password)
    {
        super(userID, firstName, lastName, email, password, "Manager");
    }

    // Functional Methods 

    public void generateReport(String type, String dateRange)
    {
    	// Get generate report from  DB system 
        System.out.println("Generated " + type + " report for " + dateRange);
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
