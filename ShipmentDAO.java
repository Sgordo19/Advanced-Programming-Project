package ga;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShipmentDAO {

    public static boolean saveShipment(Shipment s) {
        String url = "jdbc:mysql://localhost:3306/test_ap";
        String user = "root";
        String pass = "";
        
        // Updated SQL to match your actual table structure
        String sql = """
            INSERT INTO shipment (
                tracking_seq,
                tracking_number,
                sender_id,
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

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Saving shipment: " + s.getTrackingNumber());
            
            ps.setInt(1, Shipment.getNextTrackingSequence());
            ps.setString(2, s.getTrackingNumber());

            
           // Sender
            ps.setString(3, s.getSender().getCID());
            

            // Recipient
            ps.setString(4, s.getRecipient().getName() != null ? s.getRecipient().getName() : "");
            ps.setString(5, s.getRecipient().getAddress().getAddress() != null ? s.getRecipient().getAddress().getAddress() : "");
            ps.setString(6, s.getRecipient().getPhone() != null ? s.getRecipient().getPhone() : "");

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
}