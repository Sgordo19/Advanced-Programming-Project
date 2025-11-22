package Project;

import java.util.Scanner;

public class ClerkController {

    private final Scanner scanner;

    public ClerkController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(Clerk clerk) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- CLERK MENU ---");
            System.out.println("1. Process Shipment Request");
            System.out.println("2. Assign Shipment to Vehicle");
            System.out.println("3. Update Shipment Status");
            System.out.println("4. Handle Payment");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            int choice = MenVal.readInt(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Enter shipment ID to process: ");
                    String shipmentId = scanner.nextLine();
                    clerk.processShipmentRequest(shipmentId);
                    break;

                case 2:
                    System.out.print("Enter shipment ID to assign: ");
                    String sId = scanner.nextLine();
                    System.out.print("Enter vehicle ID: ");
                    String vId = scanner.nextLine();
                    clerk.assignShipmentToVehicle(sId, vId);
                    break;

                case 3:
                    System.out.print("Enter shipment ID: ");
                    String upId = scanner.nextLine();
                    System.out.print("Enter new status: ");
                    String status = scanner.nextLine();
                    clerk.updateShipmentStatus(upId, status);
                    break;

                case 4:
                    System.out.print("Enter invoice ID: ");
                    int invId = MenVal.readInt(scanner);
                    System.out.print("Enter payment type: ");
                    String paymentType = scanner.nextLine();
                    clerk.handlePayment(invId, paymentType);
                    break;

                case 0:
                    loggedIn = false;
                    System.out.println("Logging out clerk...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

