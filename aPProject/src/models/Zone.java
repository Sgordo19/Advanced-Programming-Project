package models;

public enum Zone {
    ZONE_1(1, 5.0, "Local Area"),
    ZONE_2(2, 15.0, "Metro Area"),
    ZONE_3(3, 25.0, "Regional"),
    ZONE_4(4, 40.0, "International");
    
    private final int zoneNumber;
    private final double baseDistance;
    private final String description;
    
    Zone(int zoneNumber, double baseDistance, String description) {
        this.zoneNumber = zoneNumber;
        this.baseDistance = baseDistance;
        this.description = description;
    }
    
    public int getZoneNumber() {
        return zoneNumber;
    }
    
    public double getBaseDistance() {
        return baseDistance;
    }
    
    public String getDescription() {
        return description;
    }
    public static Zone getZoneById(int id) {
        for (Zone z : values()) {
            if (z.getZoneNumber() == id) return z;
        }
        return null;
    }

}