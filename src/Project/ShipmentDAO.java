package Project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/test_ap";
    private static final String USER = "root";
    private static final String PASS = "";

    public static boolean saveShipment(Shipment s) {
        // SQL to match table structure
        String sql = """
            INSERT INTO shipments (
                tracking_seq,
                tracking_number,
                userID,
                recipient_name,
                recipient_address,
                recipient_phone,
                weight,
                length,
                width,
                height,
                type,
                destination_zone,
                distance,
                cost,
                status,
                creation_date,
                delivery_date
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Saving shipment: " + s.getTrackingNumber());
            
            ps.setInt(1, Shipment.getNextTrackingSequence());
            ps.setString(2, s.getTrackingNumber());

            
           // Sender
            ps.setString(3, s.getSender().getUserID());
            

            // Recipient
            ps.setString(4, s.getRecipient().getName() != null ? s.getRecipient().getName() : "");
            ps.setString(5, s.getRecipient().getAddress().getAddress() != null ? s.getRecipient().getAddress().getAddress() : "");
            ps.setString(6, s.getRecipient().getPhoneNumber() != null ? s.getRecipient().getPhoneNumber() : "");

            // Package
            ps.setDouble(7, s.getPkg().getWeight());
            ps.setDouble(8, s.getPkg().getLength());
            ps.setDouble(9, s.getPkg().getWidth());
            ps.setDouble(10, s.getPkg().getHeight());

            // Type
            ps.setString(11, s.getPackageType() != null ? s.getPackageType().toString() : "");

            // Destination zone (using recipient zone)
            ps.setInt(12, s.getRecipient().getAddress().getZone().getZoneNumber());

            ps.setDouble(13, s.getDistance());
            ps.setDouble(14, s.getCost());
            ps.setString(15, s.getStatus() != null ? s.getStatus().toString() : "");
            ps.setString(16, s.getCreationDate() != null ? s.getCreationDate() : "");
            ps.setString(17, s.getDeliveryDate() != null ? s.getDeliveryDate() : "");

            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.err.println("SQL Error: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    public static List<Shipment> getShipmentsByCustomer(String userID) {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM shipments WHERE userID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Shipment s = new Shipment();
                s.setTrackingNumber(rs.getString("tracking_number"));
                s.setStatus(Status.valueOf(rs.getString("status")));
                shipments.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shipments;
    }
    // Assign vehicle to shipment
    public static boolean assignVehicleToShipment(String trackingNumber, int vehicleId) {
        String sql;

        if (vehicleId == 0) {
            // UNASSIGN shipment
            sql = "UPDATE shipments SET assigned_vehicle = NULL, status = 'PENDING' WHERE tracking_number = ?";
        } else {
            // ASSIGN shipment
            sql = "UPDATE shipments SET assigned_vehicle = ?, status = 'ASSIGNED' WHERE tracking_number = ?";
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (vehicleId == 0) {
                ps.setString(1, trackingNumber);
            } else {
                ps.setInt(1, vehicleId);
                ps.setString(2, trackingNumber);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Shipment> getShipmentsByDate(java.util.Date start) {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM shipments WHERE creation_date = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Convert java.util.Date -> java.sql.Date
            java.sql.Date sqlStart = new java.sql.Date(start.getTime());
            ps.setDate(1, sqlStart);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) { // use while instead of if to get all results
                Shipment s = new Shipment();
                s.setTrackingNumber(rs.getString("tracking_number"));

                // Sender
                Customer sender = new Customer();
                sender.setUserID(rs.getString("userID"));
                s.setSender(sender);

                // Recipient
                Customer recipient = new Customer();
                recipient.setName(rs.getString("recipient_name"));
                recipient.setPhoneNumber(rs.getString("recipient_phone"));
                Address addr = new Address();
                addr.setAddress(rs.getString("recipient_address"));
                recipient.setAddress(addr);
                s.setRecipient(recipient);

                // Package
                Package pkg = new Package();
                pkg.setWeight(rs.getDouble("weight"));
                pkg.setLength(rs.getDouble("length"));
                pkg.setWidth(rs.getDouble("width"));
                pkg.setHeight(rs.getDouble("height"));
                s.setPkg(pkg);

                // Type
                s.setPackageType(Type.fromDescription(rs.getString("type")));

                // Status
                s.setStatus(Status.fromString(rs.getString("status")));

                s.setDistance(rs.getDouble("distance"));
                s.setCost(rs.getDouble("cost"));
                s.setCreationDate(rs.getString("creation_date"));
                s.setDeliveryDate(rs.getString("delivery_date"));
                s.setAssignedVehicleId(rs.getInt("assigned_vehicle"));
                shipments.add(s);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return shipments;
    }



    
    public static List<Shipment> getPendingShipments() {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM shipments WHERE status IN ('PENDING', 'ASSIGNED')";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Shipment s = new Shipment();
                s.setTrackingNumber(rs.getString("tracking_number"));

                // Status
                try {
                    s.setStatus(Status.valueOf(rs.getString("status")));
                } catch (Exception e) {
                    s.setStatus(Status.PENDING);
                }

                // Load minimal weight
                Package pkg = new Package();
                pkg.setWeight(rs.getDouble("weight"));
                s.setPkg(pkg);

                shipments.add(s);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return shipments;
    }

 
    // Fetch shipment by tracking number
    public static Shipment getShipmentByTrackingNumber(String trackingNumber) {
        String sql = "SELECT * FROM shipments WHERE tracking_number = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trackingNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Shipment s = new Shipment();
                s.setTrackingNumber(rs.getString("tracking_number"));

                // Sender
                Customer sender = new Customer();
                sender.setUserID(rs.getString("userID"));
                s.setSender(sender);

                // Recipient
                Customer recipient = new Customer();
                recipient.setName(rs.getString("recipient_name"));
                recipient.setPhoneNumber(rs.getString("recipient_phone"));
                Address addr = new Address();
                addr.setAddress(rs.getString("recipient_address"));
                recipient.setAddress(addr);
                s.setRecipient(recipient);

                // Package
                Package pkg = new Package();
                pkg.setWeight(rs.getDouble("weight"));
                pkg.setLength(rs.getDouble("length"));
                pkg.setWidth(rs.getDouble("width"));
                pkg.setHeight(rs.getDouble("height"));
                s.setPkg(pkg);

                // Type
                s.setPackageType(Type.fromDescription(rs.getString("type")));

                // Status
                s.setStatus(Status.fromString(rs.getString("status")));

                s.setDistance(rs.getDouble("distance"));
                s.setCost(rs.getDouble("cost"));
                s.setCreationDate(rs.getString("creation_date"));
                s.setDeliveryDate(rs.getString("delivery_date"));
                s.setAssignedVehicleId(rs.getInt("assigned_vehicle"));
                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static List<Shipment> getShipmentsByVehicle(int vehicleId) {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM shipments WHERE assigned_vehicle = ? AND status IN ('ASSIGNED')";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Shipment s = new Shipment();
                s.setTrackingNumber(rs.getString("tracking_number"));

                // Recipient info
                Customer recipient = new Customer();
                recipient.setName(rs.getString("recipient_name"));
                Address addr = new Address();
                addr.setAddress(rs.getString("recipient_address"));
                recipient.setAddress(addr);
                s.setRecipient(recipient);

                // Package info
                Package pkg = new Package();
                pkg.setWeight(rs.getDouble("weight"));
                s.setPkg(pkg);

                // Status
                s.setStatus(Status.fromString(rs.getString("status")));

                shipments.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return shipments;
    }
    //update shipment status
    public static boolean updateShipmentStatus(String trackingNumber, Status newStatus) {
    	String cDate = java.time.LocalDate.now().toString();
        String sql = "UPDATE shipments SET status = ? WHERE tracking_number = ?";
        String sql2 = "UPDATE shipments SET actual_delivery_date = ? WHERE tracking_number = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
        	 PreparedStatement ps2 = conn.prepareStatement(sql2)){

        	 // First update - status
            ps.setString(1, newStatus.name()); 
            ps.setString(2, trackingNumber);
            int rowsAffected1 = ps.executeUpdate();

            // Second update - delivery date
            ps2.setString(1, cDate);
            ps2.setString(2, trackingNumber);
            int rowsAffected2 = ps2.executeUpdate();

            return rowsAffected1 > 0 || rowsAffected2 > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean canAssignShipmentToVehicle(Shipment s, Vehicle v) {
        int currentAssignedVehicle = s.getAssignedVehicleId();

        double currentWeight = VehicleDAO.getTotalWeightForVehicle(v.getVehicle_id());
        int currentQty = VehicleDAO.getTotalQuantityForVehicle(v.getVehicle_id());

        // If shipment already belongs to this vehicle → no capacity issue
        if (currentAssignedVehicle == v.getVehicle_id()) {
            return true;
        }

        double newTotalWeight = currentWeight + s.getPkg().getWeight();
        int newTotalQty = currentQty + 1;

        return newTotalWeight <= v.getMax_weight() &&
               newTotalQty <= v.getMax_quantity();
    }


}