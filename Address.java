package ga;

public class Address {
    private String address;
    private Zone zone;

    public Address() {
        this.address = "";
        this.zone = Zone.ZONE_1; // default zone
    }

    public Address(String address, Zone zone) {
        this.address = address;
        this.zone = zone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "{Address='" + address + "', Zone=" + zone + "}";
    }
}
