package ga;

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

    @Override
    public String toString() {
        return "{Weight=" + weight + "kg, Destination=" + destination + "}";
    }
}
