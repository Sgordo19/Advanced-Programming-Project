package models;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private int vehicle_id;
    private int max_weight;
    private int max_quantity;
    private String driver_id;
    private String v_status;

    // Keep track of assigned shipments
    private List<Shipment> shipments;

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

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getV_status() {
        return v_status;
    }

    public void setV_status(String v_status) {
        this.v_status = v_status;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    // ------------------- CONSTRUCTORS -------------------
    public Vehicle() {
        vehicle_id = 0;
        max_weight = 0;
        max_quantity = 0;
        driver_id = "";
        v_status = "";
        shipments = new ArrayList<>();
    }

    public Vehicle(int vehicle_id, int max_weight, int max_quantity, String driver_id, String v_status) {
        this.vehicle_id = vehicle_id;
        this.max_weight = max_weight;
        this.max_quantity = max_quantity;
        this.driver_id = driver_id;
        this.v_status = v_status;
        this.shipments = new ArrayList<>();
    }


	 /** Check if a shipment can be added without exceeding weight or quantity */
    public boolean canAddShipment(Shipment shipment) {
        double totalWeight = shipment.getPkg().getWeight();
        int totalQuantity = 1;

		for (Shipment s : shipments) {
            totalWeight += s.getPkg().getWeight();
            totalQuantity += 1;
        }

        if (totalWeight >= max_weight) {
            System.out.println("Cannot add shipment: weight exceeds vehicle max capacity.");
            return false;
        }

        if (totalQuantity >= max_quantity) {
            System.out.println("Cannot add shipment: quantity exceeds vehicle max capacity.");
            return false;
        }

        return true;
    }

    /** Add a shipment to the vehicle if capacity allows */
    public boolean addShipment(Shipment shipment) {
        if (canAddShipment(shipment)) {
            shipments.add(shipment);
            System.out.println("Shipment " + shipment.getTrackingNumber() + " added to vehicle " + vehicle_id);
            return true;
        } else {
            System.out.println("Shipment " + shipment.getTrackingNumber() + " could not be added.");
            return false;
        }
    }
    /** Get total number of shipments assigned */
    public int getCurrentQuantity() {
        return shipments.size();
    }
}
