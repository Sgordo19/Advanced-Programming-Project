package models;


/*
import java.util.Random;
import java.util.Scanner;
*/

public class Package {

	private double weight;
    private double length;
    private double width;
    private double height;
    private Address destination;

    public Package() {
        this.weight = 0.0;
        this.length = 0.0;
        this.width = 0.0;
        this.height = 0.0;
        this.destination = new Address();
    }

    public Package(double weight, double length, double width, double height, Address destination) {
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

	// Display package info
    @Override
	public String toString() {
		return "Package [weight=" + weight + ", length=" + length + ", width=" + width + ", height=" + height
				+ ", destination=" + destination + "]";
	}
    ///shipment covers all of this

	/* Input methods
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
		input.nextLine(); // clear buffer

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

	public void mark_delivered(Package pkg) {
		if (pkg.isP_status() == false) {
			System.out.println("Package " + pkg.getPackage_id() + " is delivered");
		} else {
			System.out.println("Package " + pkg.getPackage_id() + " is not delivered");
		}
	}
	*/

}