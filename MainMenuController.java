package Project;

import java.util.Scanner;

public class MainMenuController {

	private final Scanner scanner;
	private final LoginController loginController;
	private final RegistrationController registrationController;

	public MainMenuController(Scanner scanner, LoginController loginController,
			RegistrationController registrationController) {
		this.scanner = scanner;
		this.loginController = loginController;
		this.registrationController = registrationController;
	}

	public void start() {
		boolean running = true;

		while (running) {
			System.out.println("\n==============================");
			System.out.println("      SMARTSHIP MAIN MENU     ");
			System.out.println("==============================");
			System.out.println("1. Register New User");
			System.out.println("2. Login");
			System.out.println("0. Exit");
			System.out.print("Enter choice: ");

			int choice = MenVal.readInt(scanner);

			switch (choice) {
			case 1:
				registrationController.registerUser(); // <-- updated
				break;

			case 2:
				loginController.login();
				break;

			case 0:
				running = false;
				System.out.println("Exiting SmartShip. Goodbye!");
				break;

			default:
				System.out.println("Invalid choice, please try again.");
			}
		}
	}
}
