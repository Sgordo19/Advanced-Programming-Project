package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {
	protected String userID;
	protected String firstName;
	protected String lastName;
	protected String email;
	protected String password;
	protected String role;
	private Address address;
	private String phoneNumber;
	
	//Default Constructor	
	public User()
	{
		this.userID = "";
		this.firstName = "";
		this.lastName = "";
		this.email = "";
		this.password = "";
		this.role = "";
		this.address = new Address();
		this.phoneNumber = "";
	}

	// Primary Constructor
	public User(String userID, String firstName, String lastName, String email, String password, String role, Address address, String phoneNumber) {
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	// Getters and Setters
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	//auto increment user ID using database
    public static int getNextUserSequence() {
        int nextSeq = 1; 

        String url = "jdbc:mysql://localhost:3306/test_ap";
        String user = "root";
        String pass = "";

        String query = "SELECT MAX(user_seq) FROM users";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                nextSeq = rs.getInt(1) + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nextSeq;
    }
    public static String getPrefixForType(String role) {
        switch (role.toLowerCase()) {
            case "customer": return "CUS";
            case "clerk": return "CLK";
            case "driver": return "DRV";
            case "manager": return "MNG";
            default: return "UNK";
        }
    }
    
    public static String generateUserID(int seq, String role) {
        String prefix = getPrefixForType(role);
        String formattedSeq = String.format("%06d", seq);
        return prefix + "-" + formattedSeq;
    }
	
	/*
	// Functional Methods
	public boolean checkPassword(String enteredPassword) {
		return this.password.equals(enteredPassword);
	}

	public boolean checkLogin(String enteredEmail, String enteredPassword) {
		return this.email.equalsIgnoreCase(enteredEmail) && this.password.equals(enteredPassword);
	}

	public void updateProfile(String newEmail, String newPassword) {
		this.email = newEmail;
		this.password = newPassword;
	}

	public void viewDashboard() {
		ViewDashboard dashboard = new ViewDashboard();

		switch (role.toLowerCase()) {
		case "customer":
			dashboard.displayCustomerDashboard((Customer) this);
			break;

		case "clerk":
			dashboard.displayClerkDashboard((Clerk) this);
			break;

		case "driver":
			dashboard.displayDriverDashboard((Driver) this);
			break;

		case "manager":
			dashboard.displayManagerDashboard((Manager) this);
			break;

		default:
			System.out.println("Unknown role. Dashboard unavailable.");
			break;
		}
	}
	*/
}