package Project;

import java.util.Scanner;

public class RegistrationController {

	private final Scanner scanner;
	private final UserDAO userDAO;

	public RegistrationController(Scanner scanner, UserDAO userDAO) {
		this.scanner = scanner;
		this.userDAO = userDAO;
	}

	public void registerUser() {

		System.out.println("\n--- USER REGISTRATION ---");
		System.out.println("Select user type:");
		System.out.println("1. Customer");
		System.out.println("2. Clerk");
		System.out.println("3. Driver");
		System.out.println("4. Manager");
		System.out.print("Enter option: ");

		int choice = Integer.parseInt(scanner.nextLine());

		// Common fields
		System.out.print("First name: ");
		String firstName = scanner.nextLine();

		System.out.print("Last name: ");
		String lastName = scanner.nextLine();

		System.out.print("Email: ");
		String email = scanner.nextLine();

		System.out.print("Password: ");
		String password = scanner.nextLine();

		User user = null; // will hold the subclass object

		switch (choice) {

		case 1: // CUSTOMER
			System.out.print("Address: ");
			String address = scanner.nextLine();

			System.out.print("Phone number: ");
			String phone = scanner.nextLine();

			user = new Customer(0, firstName, lastName, email, password, address, phone, null);

			userDAO.saveCustomer((Customer) user);
			break;

		case 2: // CLERK
			user = new Clerk(0, firstName, lastName, email, password);

			userDAO.saveClerk((Clerk) user);
			break;

		case 3: // DRIVER
			System.out.print("Assigned Vehicle: ");
			String vehicle = scanner.nextLine();

			user = new Driver(0, firstName, lastName, email, password, vehicle);

			userDAO.saveDriver((Driver) user);
			break;

		case 4: // MANAGER
			user = new Manager(0, firstName, lastName, email, password);

			userDAO.saveManager((Manager) user);
			break;

		default:
			System.out.println("Invalid selection.");
			return;
		}

		System.out.println("\nAccount created successfully!");
		System.out.println("Your user ID is: " + user.getUserID());
	}
}
