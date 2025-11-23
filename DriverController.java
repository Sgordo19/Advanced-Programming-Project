package Project;

import java.util.Scanner;

public class DriverController {

	private final Scanner scanner;

	public DriverController(Scanner scanner) {
		this.scanner = scanner;
	}

	public void showMenu(Driver driver) {
		boolean loggedIn = true;

		while (loggedIn) {
			System.out.println("\n--- DRIVER MENU ---");
			System.out.println("1. View Assigned Deliveries");
			System.out.println("2. Confirm Delivery");
			System.out.println("3. Update Delivery Status");
			System.out.println("0. Logout");
			System.out.print("Enter choice: ");

			int choice = MenVal.readInt(scanner);

			switch (choice) {
			case 1:
				driver.viewAssignedDeliveries();
				break;

			case 2:
				System.out.print("Enter shipment ID to confirm: ");
				String shipmentId = scanner.nextLine();
				driver.confirmDelivery(shipmentId);
				break;

			case 3:
				System.out.print("Enter shipment ID: ");
				String sId = scanner.nextLine();
				System.out.print("Enter new delivery status: ");
				String status = scanner.nextLine();
				driver.updateDeliveries(sId, status);
				break;

			case 0:
				loggedIn = false;
				System.out.println("Logging out driver...");
				break;

			default:
				System.out.println("Invalid choice.");
			}
		}
	}
}
