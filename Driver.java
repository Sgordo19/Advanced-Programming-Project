package Project;

public class Driver extends User {
	private String assignedRoute;
	
	public Driver()
	{
		super();
		this.assignedRoute = "";
	}
	// Primary Constructor
	public Driver(int userID, String firstName, String lastName, String email, String password,
			String assignedRoute) {
		super();
		this.assignedRoute = assignedRoute;
	}

	// Getters and Setters
	public String getAssignedRoute() {
		return assignedRoute;
	}

	public void setAssignedRoute(String assignedRoute) {
		this.assignedRoute = assignedRoute;
	}

	// Functional Methods

	public void viewAssignedDeliveries() {
		// Get delivery info from DB system
		System.out.println("Viewing deliveries assigned to driver: " + firstName + " " + lastName);
	}

	public void updateDeliveries(String shipmentID, String status) {
		// Get shipment info from DB system
		System.out.println(
				"Shipment " + shipmentID + " marked as " + status + " by driver " + firstName + " " + lastName);
	}

	public void confirmDelivery(String shipmentID) {
		// // Get delivery confirmation info from DB system
		System.out.println("Delivery confirmed for shipment: " + shipmentID);
	}
}
