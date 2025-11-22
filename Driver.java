package Project;

public class Driver extends User {
	private String assignedVehicle;

	// Primary Constructor
	public Driver(int userID, String firstName, String lastName, String email, String password,
			String assignedVehicle) {
		super();
		this.assignedVehicle = assignedVehicle;
	}

	// Getters and Setters
	public String getAssignedVehicle() {
		return assignedVehicle;
	}

	public void setAssignedVehicle(String assignedVehicle) {
		this.assignedVehicle = assignedVehicle;
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
