package smartship;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User
{
	private String address;
	private String phoneNumber;
	private List<String> shipments; // To store shipments IDs
	
	
	// Primary Constructor
	public Customer (int userID, String firstName, String lastName, String email, String password, String address, String phoneNumber)
	{
		super(userID, firstName, lastName, email, password, "Customer");
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.shipments = new ArrayList<>();
	}


	//Getters and Setters
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


	public List<String> getShipments() 
	{
		return shipments;
	}


	public void setShipments(List<String> shipments) 
	{
		this.shipments = shipments;
	}
	
	// Functional Methods 
	
	public void createShipment(String shipmentDetails)
	{
		shipments.add(shipmentDetails);
		System.out.println("Shipment created for " + firstName + "" + lastName +":" + shipmentDetails);
	}
	
	public void trackShipment(String trackingNumber)
	{
		System.out.println(firstName + " " + lastName + " is tracking shipment: " + trackingNumber);

	}
	
	public void viewInvoice (int invoiceID)
	{
		System.out.println("Viewing invoice #" + invoiceID + " for " + firstName);
	}
	
	public void makePayment(double amount)
	{
		System.out.println(firstName + "" + lastName + "make a payment of $" + amount);
	}
}
