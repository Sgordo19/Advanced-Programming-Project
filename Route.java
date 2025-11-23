package Project;

public class Route {
    private int route_id;
    private String route_name;
    private String start_location;
    private String end_location;
    private int vehicle_id; 
    private String driver_id;  

    // Constructors
    public Route() {
        this.vehicle_id = 0;
        this.driver_id = "";
    }

    public Route(int route_id, String route_name, String start_location, String end_location,
                 int vehicle_id, String driver_id) {
        this.route_id = route_id;
        this.route_name = route_name;
        this.start_location = start_location;
        this.end_location = end_location;
        this.vehicle_id = vehicle_id;
        this.driver_id = driver_id;
    }

    // Getters & Setters
    public int getRoute_id() { return route_id; }
    public void setRoute_id(int route_id) { this.route_id = route_id; }

    public String getRoute_name() { return route_name; }
    public void setRoute_name(String route_name) { this.route_name = route_name; }

    public String getStart_location() { return start_location; }
    public void setStart_location(String start_location) { this.start_location = start_location; }

    public String getEnd_location() { return end_location; }
    public void setEnd_location(String end_location) { this.end_location = end_location; }

    public int getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(int vehicle_id) { this.vehicle_id = vehicle_id; }

    public String getDriver_id() { return driver_id; }
    public void setDriver_id(String driver_id) { this.driver_id = driver_id; }

    // Assign vehicle to route
    public void assignVehicle(int vehicleId) {
        this.vehicle_id = vehicleId;
    }

    // Assign driver to route
    public void assignDriver(String driverId) {
        this.driver_id = driverId;
    }
}
