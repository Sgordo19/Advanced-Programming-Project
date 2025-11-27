package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvoiceDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/test_ap";
    private static final String USER = "root";
    private static final String PASS = "";

    public static boolean updateInvoiceStatus(String trackingNumber, String newStatus) {
        String sql = "UPDATE invoices SET iStatus = ? WHERE trackingNumber = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)){

        	 // First update - status
            ps.setString(1, newStatus); 
            ps.setString(2, trackingNumber);
            int rowsAffected1 = ps.executeUpdate();

            return rowsAffected1 > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
