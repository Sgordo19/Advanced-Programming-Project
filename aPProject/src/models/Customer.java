package models;

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

	// Functional Methods

	public void viewInvoice(int invoiceID) {
		System.out.println("Viewing invoice #" + invoiceID + " for " + firstName);
	}

	public void makePayment(double amount) {
		System.out.println(firstName + " " + lastName + " made a payment of $" + amount);
	}
}