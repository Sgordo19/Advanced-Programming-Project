package Project;

import java.util.Scanner;

public class CustomerController {

    private final Scanner scanner;

    public CustomerController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(Customer customer) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. Create Shipment");
            System.out.println("2. Track Shipment");
            System.out.println("3. View Invoice");
            System.out.println("4. Make Payment");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            int choice = MenVal.readInt(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Enter shipment description: ");
                    String details = scanner.nextLine();
                    String shipmentId = "S" + System.currentTimeMillis();
                    customer.createShipment(shipmentId + " - " + details);
                    break;

                case 2:
                    System.out.print("Enter tracking number: ");
                    String tracking = scanner.nextLine();
                    customer.trackShipment(tracking);
                    break;

                case 3:
                    System.out.print("Enter invoice ID: ");
                    int invoiceId = MenVal.readInt(scanner);
                    customer.viewInvoice(invoiceId);
                    break;

                case 4:
                    System.out.print("Enter payment amount: ");
                    double amount = MenVal.readDouble(scanner);
                    customer.makePayment(amount);
                    break;

                case 0:
                    loggedIn = false;
                    System.out.println("Logging out customer...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

