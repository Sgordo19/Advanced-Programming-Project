package smartship;

import java.util.ArrayList;
import java.util.List;

public class Driver extends User
{
	private String assignedVehicle;
	private List<String> routeList;
	
	public Driver(int userID, String firstName, String lastName, String email, String password,String assignedVehicle)
	{
		super(userID, firstName, lastName, email, password, "Driver");
		this.assignedVehicle = assignedVehicle;
		this.routeList = new ArrayList<>();
	}

	//Getters and Setters
	public String getAssignedVehicle() 
	{
		return assignedVehicle;
	}

	public void setAssignedVehicle(String assignedVehicle) 
	{
		this.assignedVehicle = assignedVehicle;
	}

	public List<String> getRouteList() 
	{
		return routeList;
	}

	public void setRouteList(List<String> routeList) 
	{
		this.routeList = routeList;
	}
	
	public void viewAssignedDeliveries() 
	{
		System.out.println("Deliveries for " + firstName + "" + lastName +": " + routeList);
	}
	
	public void updateDeliveries(String shipmentID, String status)
	{
		System.out.println("Shipment " + shipmentID + " marked as " + status + " by driver " + firstName+ "" + lastName);;
	}
	
	public void confirmDelivery(String shipmentID) 
	{
        System.out.println("Delivery confirmed for shipment: " + shipmentID);
    }
	
	@Override
	public void viewDasboard() 
	{
		System.out.println("Driver Dashboard - View Routes and Update Deliveries.");
		
	}
	
}
