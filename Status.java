package Project;

public enum Status {
    PENDING("Pending", "Shipment created but not processed"),
    ASSIGNED("Assigned", "Assigned to vehicle/driver"),
    IN_TRANSIT("In Transit", "Package is out for delivery"),
    DELIVERED("Delivered", "Successfully delivered to recipient"),
    CANCELLED("Cancelled", "Shipment was cancelled");
    
    private final String displayName;
    private final String description;
    
    private Status(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    //convert from string to enum
    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.displayName.equalsIgnoreCase(status) || s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null; // or throw exception if invalid
    }
}
