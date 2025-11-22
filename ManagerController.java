package Project;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ManagerController 
{

    private final Scanner scanner;
    private final ReportService reportService;
    private final UserDAO userDAO;

    public ManagerController(Scanner scanner, ReportService reportService, UserDAO userDAO) 
    {
        this.scanner = scanner;
        this.reportService = reportService;
        this.userDAO = userDAO;
    }

    public void showMenu(Manager manager) 
    {
        boolean loggedIn = true;

        while (loggedIn) 
        {
            System.out.println("\n--- MANAGER MENU ---");
            System.out.println("1. Generate Simple Report (text only)");
            System.out.println("2. Generate Shipment Report (ReportService)");
            System.out.println("3. Generate Delivery Performance Report");
            System.out.println("4. View Vehicle Utilization (dummy)");
            System.out.println("5. View All User Accounts (from DB)");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            int choice = MenVal.readInt(scanner);

            switch (choice) 
            {
                case 1:
                    System.out.print("Enter report type (e.g. Shipments): ");
                    String type = scanner.nextLine();
                    System.out.print("Enter date range (e.g. Jan–Mar 2025): ");
                    String dateRange = scanner.nextLine();
                    manager.generateReport(type, dateRange);
                    break;

                case 2:
                    System.out.println("Generating shipment report using ReportService...");
                    Report shipmentReport =
                            reportService.generateShipmentReport(new Date(), new Date());
                    shipmentReport.printReport();
                    break;

                case 3:
                    System.out.println("Generating delivery performance report...");
                    Report perfReport =
                            reportService.generateDeliveryPerformanceReport(new Date(), new Date());
                    perfReport.printReport();
                    break;

                case 4:
                    manager.viewVehicleUtilization();
                    break;

                case 5:
                    System.out.println("Listing all user accounts from database:");
                    List<User> users = userDAO.findAllUsers();
                    for (User u : users) {
                        System.out.println(
                                u.getUserID() + " - " +
                                u.getFirstName() + " " + u.getLastName() +
                                " (" + u.getRole() + ")"
                        );
                    }
                    break;

                case 0:
                    loggedIn = false;
                    System.out.println("Logging out manager...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
