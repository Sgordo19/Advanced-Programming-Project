package Project;

public class Customer extends User
{
    private String address;
    private String phoneNumber;

    // Primary Constructor
    public Customer(int userID, String firstName, String lastName, String email, String password, String address, String phoneNumber)
    {
        super(userID, firstName, lastName, email, password, "Customer");
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getAddress() 
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    // Functional Methods 
    public void createShipment(String shipmentDetails)
    {
    	// Call ShipmentDAO to create shipment in the database
        System.out.println("Shipment created for " + firstName + " " + lastName + ": " + shipmentDetails);
    }

    public void trackShipment(String trackingNumber)
    {
        System.out.println(firstName + " " + lastName + " is tracking shipment: " + trackingNumber);
    }

    public void viewInvoice(int invoiceID)
    {
        System.out.println("Viewing invoice #" + invoiceID + " for " + firstName);
    }

    public void makePayment(double amount)
    {
        System.out.println(firstName + " " + lastName + " made a payment of $" + amount);
    }
}
