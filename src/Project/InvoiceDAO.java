package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    public static boolean updateInvoiceStatusAndBalance(String newStatus, double newBalance, String invoiceNumber) {
    	String sql = "UPDATE invoices SET iStatus = ?, balance = ? WHERE invoiceNum = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, newStatus); 
            ps.setDouble(2, newBalance);
            ps.setString(3, invoiceNumber);
            int rowsAffected1 = ps.executeUpdate();

            return rowsAffected1 > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Invoice i = new Invoice();
                i.setInvoiceNum(rs.getInt("invoiceNum"));
                i.setStatus(rs.getString("iStatus"));
                i.setTotal(rs.getDouble("total"));
                i.setTrackingNumber(rs.getString("trackingNumber"));
                i.setBalance(rs.getDouble("balance"));
                invoices.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invoices;
    }
    
    
    		
	public static Invoice getInvoiceByInvoiceNumber(String invoiceNumber)
    {
    	String sql = "SELECT * FROM invoices WHERE invoiceNum = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
        		
        	 ps.setString(1, invoiceNumber);
             ResultSet rs = ps.executeQuery();
             
             if (rs.next()) {
                 Invoice i = new Invoice();
                 
                 i.setInvoiceNum(rs.getInt("invoiceNum"));
                 i.setStatus(rs.getString("iStatus"));
                 i.setTotal(rs.getDouble("total"));
                 i.setTrackingNumber(rs.getString("trackingNumber"));
                 i.setBalance(rs.getDouble("balance"));
                 return i;
             }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    	return null;
    }
	
	public static Invoice getInvoiceBytracking(String trackingNumber)
    {
    	String sql = "SELECT * FROM invoices WHERE trackingNumber = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
        		
        	 ps.setString(1, trackingNumber);
             ResultSet rs = ps.executeQuery();
             
             if (rs.next()) {
                 Invoice i = new Invoice();
                 
                 i.setInvoiceNum(rs.getInt("invoiceNum"));
                 i.setStatus(rs.getString("iStatus"));
                 i.setTotal(rs.getDouble("total"));
                 i.setTrackingNumber(rs.getString("trackingNumber"));
                 i.setBalance(rs.getDouble("balance"));
                 return i;
             }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    	return null;
    }
}
