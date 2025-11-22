package Project;

import java.util.Scanner;

public class Vehicle {

    Scanner input = new Scanner(System.in);
    private int vehicle_id;
    private int max_weight;
    private int max_quantity;
    private int current_weight;
    private int current_quantity;
    private boolean v_status;

    private Schedulequeue queue;  // queue for holding packages

    // ------------------- GETTERS & SETTERS -------------------
    public int getVehicle_id() {
        return vehicle_id;
    }
    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public int getMax_weight() {
        return max_weight;
    }
    public void setMax_weight(int max_weight) {
        this.max_weight = max_weight;
    }

    public int getMax_quantity() {
        return max_quantity;
    }
    public void setMax_quantity(int max_quantity) {
        this.max_quantity = max_quantity;
    }

    public int getCurrent_weight() {
        return current_weight;
    }
    public void setCurrent_weight(int current_weight) {
        this.current_weight = current_weight;
    }

    public int getCurrent_quantity() {
        return current_quantity;
    }
    public void setCurrent_quantity(int current_quantity) {
        this.current_quantity = current_quantity;
    }

    public boolean isV_status() {
        return v_status;
    }
    public void setV_status(boolean v_status) {
        this.v_status = v_status;
    }

    // ------------------- CONSTRUCTORS -------------------
    public Vehicle() {
        vehicle_id = 0;
        max_weight = 0;
        max_quantity = 0;
        current_weight = 0;
        current_quantity = 0;
        v_status = true;

        queue = new Schedulequeue();    // initialize queue
    }

    Vehicle(int vehicle_id, int max_weight, int max_quantity,
            int current_weight, int current_quantity, boolean v_status) {
        this.vehicle_id = vehicle_id;
        this.max_weight = max_weight;
        this.max_quantity = max_quantity;
        this.current_weight = current_weight;
        this.current_quantity = current_quantity;
        this.v_status = v_status;

        queue = new Schedulequeue();
    }

    private Vehicle(Vehicle obj) {
        this.vehicle_id = obj.vehicle_id;
        this.max_weight = obj.max_weight;
        this.max_quantity = obj.max_quantity;
        this.current_weight = obj.current_weight;
        this.current_quantity = obj.current_quantity;
        this.v_status = obj.v_status;

        queue = new Schedulequeue();
    }

    public Vehicle(int vehicle_id, int max_quantity, int max_weight, boolean v_status) {
        this.vehicle_id = vehicle_id;
        this.max_weight = max_weight;
        this.max_quantity = max_quantity;
        this.v_status = v_status;

        this.current_weight = 0;
        this.current_quantity = 0;

        queue = new Schedulequeue();
    }

    // ------------------- VALIDATION HELPERS -------------------
    private int getValidatedPositiveInt(String message) {
        int value;
        do {
            System.out.println(message);
            while (!input.hasNextInt()) {
                System.out.println("Invalid input! Enter a positive integer.");
                input.next();
            }
            value = input.nextInt();
        } while (value <= 0);
        return value;
    }

    // ------------------- VEHICLE DETAILS INPUT -------------------
    public void inputVehicleDetails() {
        this.vehicle_id     = getValidatedPositiveInt("Please enter the vehicle ID: ");
        this.max_weight     = getValidatedPositiveInt("Please enter the max weight: ");
        this.max_quantity   = getValidatedPositiveInt("Please enter the max quantity: ");
        this.v_status       = true;
    }
    



    // ------------------- CAPACITY CHECK -------------------
    public boolean check_capacity(int pweight, int pquantity) {

        int weight_result = current_weight + pweight;
        int quantity_result = current_quantity + pquantity;
        boolean result;

        if (weight_result <= max_weight && quantity_result <= max_quantity) {
            System.out.println("Load successfully added");

            current_weight += pweight;
            current_quantity += pquantity;
            result = true;
            
           
            return result;
        } else {
            System.out.println("Vehicle is full or package too heavy, please choose another");
            result = false;
            check_availability(result);
            return result;
        }
    }

    // ------------------- ASSIGN PACKAGE TO VEHICLE -------------------
    public void assignPackage(Package pkg) {

        if (check_capacity(pkg.getP_weight(), pkg.getP_quantity())) {
            queue.Enqueue(pkg);
            System.out.println("Package " + pkg.getPackage_id() +
                               " assigned to Vehicle " + vehicle_id);
        } else {
            System.out.println("Cannot assign package due to capacity.");
        }
    }

    // ------------------- REMOVE PACKAGE FROM QUEUE -------------------
    public Package removePackage() {
        if (queue.isEmpty()) {
            System.out.println("No packages to remove.");
            return null;
        }

        Scanner input = new Scanner(System.in);
        System.out.print("Enter the Package ID to remove: ");
        int idToRemove = input.nextInt();

        Schedulequeue temp = new Schedulequeue();
        Package removed = null;

        // Search through the queue
        while (!queue.isEmpty()) {
            Package pack = queue.Dequeue();  // dequeue inside the loop

            if (pack.getPackage_id() == idToRemove) {
                removed = pack;
                current_weight -= pack.getP_weight();
                current_quantity -= pack.getP_quantity();
                this.setV_status(false);  
            } else {
                temp.Enqueue(pack);
            }
        }

        // Restore the queue
        while (!temp.isEmpty()) {
            queue.Enqueue(temp.Dequeue());
        }

        if (removed != null) {
            System.out.println("Package ID " + idToRemove + " removed successfully."); /// Need to fixed in database 
        } else {
            System.out.println("Package ID " + idToRemove + " was not found on this vehicle.");
        }

        return removed;
    }

    public void assignPackageWithValidation(Package pkg, Scanner input) {

        System.out.println("\nEnter vehicle ID to assign the package: ");
        int selectedId = input.nextInt();

        if (selectedId == this.vehicle_id) {
            pkg.assignToVehicle(this);
            System.out.println("Package assigned successfully!");
        } else {
            System.out.println("Vehicle ID not found. Package NOT assigned.");
        }

        System.out.println("\n--- FINAL VEHICLE STATUS ---");
        System.out.println(this);
    }
    
    public boolean check_schedule() {
        if (queue.isEmpty()) {
            System.out.println("No packages scheduled for this vehicle.");
            return false;
        }

        System.out.println("\n--- VEHICLE " + vehicle_id + " SCHEDULED PACKAGES ---");

        queue.displayQueue();   //allows Schedulequeue handle printing

        return true;
    }
    
    public void check_availability( boolean result)
    {
    	if(result == false)
    	{
    		setV_status(false);
    	}
    }
    


}
