package Project;

public class Customer extends User {
	private Address address;
	private String phoneNumber;

	public Customer() {
    	super();
        this.phoneNumber = "";
        this.address = new Address();
    }


 	public Customer (int userID, String firstName, String lastName, String email, String password, String role, String phoneNumber, Address address)
 	{
 		super();
 		this.address = address;
 		this.phoneNumber = phoneNumber;
 	}
 	public String getUserID()
 	{
 		return userID;
 	}
 	public void setUserID(String userID)
 	{
 		this.userID = userID;
 	}
 	public String getName()
 	{
 		return firstName + " " + lastName;
 	}
 	public void setName(String fullName) {
 	    String[] parts = fullName.trim().split(" ", 2);
 	    this.firstName = parts[0];
 	    this.lastName = parts.length > 1 ? parts[1] : "";
 	}

	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}
/*
// FUNCTIONAL METHODS — Connected to DAO
     
    // 1. Create a Shipment (Customer initiates shipment request)
    public boolean createShipment(Shipment shipment)
    {
        this.shipment = shipment;
		return ShipmentDAO.saveShipment(shipment);
    }


    // 2. Track Shipment (view status)
    public void trackShipment(String trackingNumber) 
    {
        // Retrieve all shipments for this customer
        List<Shipment> shipments = ShipmentDAO.getShipmentsByCustomer(String.valueOf(this.getUserID()));
        boolean found = false;

        // Loop through each shipment and find the one with the matching tracking number
        for (Shipment s : shipments) {
            if (s.getTrackingNumber().equalsIgnoreCase(trackingNumber)) {
                System.out.println("Shipment Status for " + trackingNumber + ": " + s.getStatus());
                found = true;
                break;
            }
        }

        // If no match was found
        if (!found) {
            System.out.println("No shipment found with tracking number: " + trackingNumber);
        }
    }
   
    // 3. View All Shipments by This Customer
    public void viewMyShipments()
    {
        List<Shipment> shipments = ShipmentDAO.getShipmentsByCustomer(String.valueOf(this.userID));

        System.out.println("\n=== SHIPMENTS FOR " + firstName + " " + lastName + " ===");

        if (shipments.isEmpty())
        {
            System.out.println("No shipments found.");
            return;
        }

        for (Shipment s : shipments)
        {
            System.out.println("Tracking: " + s.getTrackingNumber() +
                               " | Status: " + s.getStatus());
        }
    }*/
	public void viewInvoice(int invoiceID) {
		System.out.println("Viewing invoice #" + invoiceID + " for " + firstName);
	}

	public void makePayment(double amount) {
		System.out.println(firstName + " " + lastName + " made a payment of $" + amount);
	}
}
