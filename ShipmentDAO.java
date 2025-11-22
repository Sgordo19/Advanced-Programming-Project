package Project;

import java.sql.Connection;
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
}