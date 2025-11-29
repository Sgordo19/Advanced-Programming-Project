package dbFactories;

import models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
	private static final Logger logger = LogManager.getLogger(InvoiceDAO.class);
	
	private static final String URL = "jdbc:mysql://localhost:3306/test_ap";
    private static final String USER = "root";
    private static final String PASS = "";

    public static boolean updateInvoiceStatus(String trackingNumber, String newStatus) {
        String sql = "UPDATE invoices SET iStatus = ? WHERE trackingNumber = ?";
        logger.info("Updating invoice status for trackingNumber={} to {}", trackingNumber, newStatus);
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)){

        	 // First update - status
            ps.setString(1, newStatus); 
            ps.setString(2, trackingNumber);
            int rowsAffected1 = ps.executeUpdate();
            
            logger.info("Invoice update result for trackingNumber={}: rowsAffected={}", trackingNumber, rowsAffected1);
            return rowsAffected1 > 0;

        } catch (SQLException e) {
        	logger.error("Failed to update invoice status for trackingNumber={}: {}", trackingNumber, e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateInvoiceStatusAndBalance(String newStatus, double newBalance, String invoiceNumber) {
    	String sql = "UPDATE invoices SET iStatus = ?, balance = ? WHERE invoiceNum = ?";
    	 logger.info("Updating invoice {} with status={} and balance={}", invoiceNumber, newStatus, newBalance);
    	 
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, newStatus); 
            ps.setDouble(2, newBalance);
            ps.setString(3, invoiceNumber);
            int rowsAffected1 = ps.executeUpdate();
            
            logger.info("Invoice update result for invoiceNum={}: rowsAffected={}", invoiceNumber, rowsAffected1);
            return rowsAffected1 > 0;

        } catch (SQLException e) {
        	logger.error("Failed to update invoice {}: {}", invoiceNumber, e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices";
        logger.info("Fetching all invoices from database");

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
            logger.info("Fetched {} invoices", invoices.size());
            
        } catch (Exception e) {
        	logger.error("Error fetching invoices: {}", e.getMessage(), e);
            e.printStackTrace();
        }

        return invoices;
    }
    
    
    		
	public static Invoice getInvoiceByInvoiceNumber(String invoiceNumber)
    {
    	String sql = "SELECT * FROM invoices WHERE invoiceNum = ?";
    	logger.info("Fetching invoice by invoiceNum={}", invoiceNumber);
    	
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
                 logger.info("Invoice {} fetched successfully", invoiceNumber);
                 return i;
             } else {
                 logger.warn("No invoice found with invoiceNum={}", invoiceNumber);
             }
        }catch (SQLException e) {
        	logger.error("Error fetching invoice {}: {}", invoiceNumber, e.getMessage(), e);
            e.printStackTrace();
        }
    	return null;
    }
	
	public static Invoice getInvoiceBytracking(String trackingNumber)
    {
    	String sql = "SELECT * FROM invoices WHERE trackingNumber = ?";
    	logger.info("Fetching invoice by trackingNumber={}", trackingNumber);
    	
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
                 logger.info("Invoice with trackingNumber {} fetched successfully", trackingNumber);
                 return i;
             }else {
                 logger.warn("No invoice found with trackingNumber={}", trackingNumber);
             }
        }catch (SQLException e) {
        	logger.error("Error fetching invoice with trackingNumber {}: {}", trackingNumber, e.getMessage(), e);
            e.printStackTrace();
        }
    	return null;
    }
}