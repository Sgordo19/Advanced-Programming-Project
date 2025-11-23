package Project;

import java.util.Scanner;

public class LoginController {

	private final Scanner scanner;
	private final UserDAO userDAO;
	private final CustomerController customerController;
	private final ClerkController clerkController;
	private final DriverController driverController;
	private final ManagerController managerController;

	public LoginController(Scanner scanner, UserDAO userDAO, CustomerController customerController,
			ClerkController clerkController, DriverController driverController, ManagerController managerController) {
		this.scanner = scanner;
		this.userDAO = userDAO;
		this.customerController = customerController;
		this.clerkController = clerkController;
		this.driverController = driverController;
		this.managerController = managerController;
	}

	public void login() {
		System.out.println("\n--- LOGIN ---");
		System.out.print("Email: ");
		String email = scanner.nextLine();

		System.out.print("Password: ");
		String password = scanner.nextLine();

		User loggedIn = userDAO.authenticate(email, password);

		if (loggedIn == null) {
			System.out.println("Invalid email or password.");
			return;
		}

		System.out.println("\nLogin successful!");
		System.out.println("Welcome " + loggedIn.getFirstName() + " (" + loggedIn.getRole() + ")");

		// Show dashboard using existing method in User
		loggedIn.viewDashboard();

		String role = loggedIn.getRole().toLowerCase();

		switch (role) {
		case "customer":
			customerController.showMenu((Customer) loggedIn);
			break;
		case "clerk":
			clerkController.showMenu((Clerk) loggedIn);
			break;
		case "driver":
			driverController.showMenu((Driver) loggedIn);
			break;
		case "manager":
			managerController.showMenu((Manager) loggedIn);
			break;
		default:
			System.out.println("Unknown role. No menu available.");
		}
	}
}
