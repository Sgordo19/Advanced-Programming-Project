package smartship;

public abstract class User 
{
	protected int userID;
	protected String firstName;
	protected String lastName;
	protected String email;
	protected String password;
	protected String role;
	
		
	// Primary Constructor
	public User(int userID, String firstName, String lastName, String email, String password, String role)
	{
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	 
	//Getters and Setters

	public int getUserID() 
	{
		return userID;
	}

	public void setUserID(int userID) 
	{
		this.userID = userID;
	}

	public String getFirstName() 
	{
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() 
	{
		return lastName;
	}

	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public String getRole() 
	{
		return role;
	}

	public void setRole(String role) 
	{
		this.role = role;
	}
	
	// Functional Methods  
	public boolean checkPassword(String enteredPassword)
	{
		return this.password.equals(enteredPassword);
	}
	public boolean checkLogin(String enteredEmail, String enteredPassword)
	{
		return this.email.equalsIgnoreCase(enteredEmail) && this.password.equals(enteredPassword);
	}
	
	public void updateProfile(String newEmail, String newPassword)
	{
		this.email = newEmail;
		this.password = newPassword;
	}
	
	public void viewDashboard()
	{
		ViewDashboard dashboard = new ViewDashboard();

        switch (role.toLowerCase())
        {
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
}
