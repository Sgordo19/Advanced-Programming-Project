package Project;

public class Clerk extends User
{
    // Primary Constructor 
    public Clerk(int userID, String firstName, String lastName, String email, String password)
    {
        super(userID, firstName, lastName, email, password, "Clerk");
    }

    // Functional Methods 
    public void processShipmentRequest(String shipmentID)
    {
        // Call ShipmentDAO to update the database
        System.out.println("Clerk processed shipment: " + shipmentID);
    }

    public void assignShipmentToVehicle(String shipmentID, String vehicleID)
    {
        System.out.println("Shipment " + shipmentID + " assigned to vehicle " + vehicleID);
    }
    
    public void updateShipmentStatus(String shipmentID, String status)
    {
        System.out.println("Shipment " + shipmentID + " status updated to: " + status);
    }
    
    public void handlePayment(int invoiceID, String paymentType)
    {
        System.out.println("Processed " + paymentType + " payment for invoice #" + invoiceID);
    }
}

