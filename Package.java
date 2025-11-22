package Project;

import java.util.Random;
import java.util.Scanner;

public class Package {

    private int package_id;
    private int p_weight;
    private int p_quantity;
    private int package_type;
    private int zone;
    private String destination;
    private boolean p_status;

    private Scanner input = new Scanner(System.in);

    // ============================
    // Constructors
    // ============================

    public Package() {
        package_id = 0;
        p_weight = 0;
        p_quantity = 0;
        destination = "";
        p_status = false;
        package_type = 0;
        zone = 0;
    }

    public Package(int package_id, int p_weight, int p_quantity, int zone, String destination,
                   boolean p_status, int package_type) {

        this.package_id = package_id;
        this.p_weight = p_weight;
        this.p_quantity = p_quantity;
        this.zone = zone;
        this.destination = destination;
        this.p_status = p_status;
        this.package_type = package_type;
    }
    
    public Package( Package obj)
    {
        this.package_id = obj.package_id;
        this.p_weight = obj.p_weight;
        this.p_quantity = obj.p_quantity;
        this.zone =obj.zone;
        this.destination = obj.destination;
        this.p_status = obj.p_status;
        this.package_type = obj.package_type;
    }

    // ============================
    // Getters and setters
    // ============================

    public int getPackage_id() {
		return package_id;
	}

	public void setPackage_id(int package_id) {
		this.package_id = package_id;
	}

	public int getP_weight() {
		return p_weight;
	}

	public void setP_weight(int p_weight) {
		this.p_weight = p_weight;
	}

	public int getP_quantity() {
		return p_quantity;
	}

	public void setP_quantity(int p_quantity) {
		this.p_quantity = p_quantity;
	}

	public int getPackage_type() {
		return package_type;
	}

	public void setPackage_type(int package_type) {
		this.package_type = package_type;
	}

	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isP_status() {
		return p_status;
	}

	public void setP_status(boolean p_status) {
		this.p_status = p_status;
	}

	public Scanner getInput() {
		return input;
	}

	public void setInput(Scanner input) {
		this.input = input;
	}


    // Display package info
    @Override
    public String toString() {
        return "Package ID: " + package_id +
               ", Weight: " + p_weight +
               ", Quantity: " + p_quantity +
               ", Zone: " + zone +
               ", Destination: " + destination +
               ", Type: " + package_type +
               ", Status: " + p_status;
    }



	// Input methods
    public void readPackageDetails() {

        Random rand = new Random();
        setPackage_id(rand.nextInt(999999) + 1);

        // Package Weight
        int weigh;
        do {
            System.out.println("Enter package weight (positive integer): ");
            while (!input.hasNextInt()) {
                System.out.println("Invalid input! Enter a positive integer.");
                input.next();
            }
            weigh = input.nextInt();
        } while (weigh <= 0);
        setP_weight(weigh);

        // Package Quantity
        int quan;
        do {
            System.out.println("Enter package quantity (positive integer): ");
            while (!input.hasNextInt()) {
                System.out.println("Invalid input! Enter a positive integer.");
                input.next();
            }
            quan = input.nextInt();
        } while (quan <= 0);
        setP_quantity(quan);

        input.nextLine(); // clear buffer
        
        // Route
        int zon = 0;

        do {
            System.out.println("Enter package Zone: 1 - 4");

            while (!input.hasNextInt()) {  
                System.out.println("Invalid input! Enter an integer from 1 to 4.");
                input.next(); // discard invalid input
            }

            zon = input.nextInt();

            if (zon < 1 || zon > 4) {
                System.out.println("Route must be between 1 and 4!");
            }

        } while (zon < 1 || zon > 4);

        setZone(zon);
        input.nextLine();  // clear buffer
        
        // Destination
        String dest;
        do {
            System.out.println("Enter destination: ");
            dest = input.nextLine().trim();
            if (dest.isEmpty())
                System.out.println("Destination cannot be empty.");
        } while (dest.isEmpty());
        setDestination(dest);

        // Package Type
        choosePackageType();
    }

    // ============================
    // Package type menu
    // ============================

    public void choosePackageType() {

        System.out.println("Package Types:");
        System.out.println("1. Document");
        System.out.println("2. Small");
        System.out.println("3. Medium");
        System.out.println("4. Large");
        System.out.println("5. Fragile");

        int type;
        do {
            System.out.println("Enter type (1–5): ");
            while (!input.hasNextInt()) {
                System.out.println("Invalid input. Enter a number (1–5).");
                input.next();
            }
            type = input.nextInt();

            if (type < 1 || type > 5)
                System.out.println("Invalid type. Try again.");
        } while (type < 1 || type > 5);

        setPackage_type(type);
    }

    // Assign to vehicle
    public boolean assignToVehicle(Vehicle veh) {

        if (veh.check_capacity(getP_weight(), getP_quantity())) {
            veh.assignPackage(this);
            System.out.println("Package " + package_id + " assigned to Vehicle " + veh.getVehicle_id());
            setP_status(true);
            veh.setV_status(true);
            return true;
        }

        System.out.println("Package could NOT be assigned. Vehicle is full.");
        return false;
               
    }
    
    public void mark_delivered( Package pkg)
    {
    	if (pkg.isP_status() == false)
    	{
    		System.out.println("Package " + pkg.getPackage_id() + " is delivered");
    	}
    	else
    	{
    		System.out.println("Package " + pkg.getPackage_id() + " is not delivered");
    	}
    }
    

}
