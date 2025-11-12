package Project;

import java.util.ArrayList;
import java.util.List;

public class Clerk extends User
{
	private List<String> assignedShipments;

	 
	public Clerk(int userID, String firstName, String lastName, String email, String password)
	{
		super(userID, firstName, lastName, email, password, "Clerk");
		this.assignedShipments = new ArrayList<>();
	}
	
	
	public void processShipmentRequest(String shipmentID)
	{
		assignedShipments.add(shipmentID);
		System.out.println("Clerk processed shipment:" + shipmentID);
	}

	public void assignedShipmentToVehicle(String shipmentID, String vehicleID)
	{
		System.out.println("Shipment" + shipmentID + "assigned to vehicle" + vehicleID);
	}
	
	public void updateShipmentStatus(String shipmentID, String status)
	{
		System.out.println("Shipment" + shipmentID + "status updated to:" + status);
	}
	
	
	public void handlePayment(int invoiceID, String paymentType)
	{
		System.out.println("Processed " + paymentType + " payment for invoice #" + invoiceID);
    }
	
	
	@Override
	public void viewDasboard() 
	{
		System.out.println("Clerk Dashboard - Manage Shipments, Payments, and Assignments.");
		
	}
	
	
}
