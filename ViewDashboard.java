package Project;

public class ViewDashboard {
	public void displayCustomerDashboard(Customer customer) {
		System.out.println("Customer Dashboard for " + customer.getFirstName());

	}

	public void displayClerkDashboard(Clerk clerk) {
		System.out.println("Clerk Dashboard for " + clerk.getFirstName());
	}

	public void displayDriverDashboard(Driver driver) {
		System.out.println("Driver Dashboard for " + driver.getFirstName());
	}

	public void displayManagerDashboard(Manager manager) {
		System.out.println("Manager Dashboard for " + manager.getFirstName());
	}

}
