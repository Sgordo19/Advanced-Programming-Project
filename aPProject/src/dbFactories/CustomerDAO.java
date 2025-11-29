package dbFactories;

import models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {
	private static final Logger logger = LogManager.getLogger(CustomerDAO.class);

	
    private static final String URL = "jdbc:mysql://localhost:3306/test_ap";
    private static final String USER = "root";
    private static final String PASS = "";

    // Get all customers (for testing - you can select one)
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT user_id, first_name, last_name, email, address, phone_number FROM users";
        logger.info("Fetching all customers from the database");
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setUserID(rs.getString("user_id"));
                customer.setName(rs.getString("first_name") + " " + rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                
                // Create address
                Address address = new Address();
                address.setAddress(rs.getString("address"));
                customer.setAddress(address);
                
                customers.add(customer);
                logger.info("Fetched {} customers", customers.size());
            }
        } catch (SQLException ex) {
        	logger.error("Error fetching customers: {}", ex.getMessage(), ex);
            System.err.println("Error fetching customers: " + ex.getMessage());
            ex.printStackTrace();
        }
        return customers;
    }

    // Get customer by CID
    public static Customer getCustomerByCID(String cid) {
        String sql = "SELECT user_id, first_name, last_name, email, address, phone_number FROM users WHERE user_id = ?";
        logger.info("Fetching customer with ID: {}", cid);
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cid);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setUserID(rs.getString("user_id"));
                customer.setName(rs.getString("first_name") + " " + rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                
                Address address = new Address();
                address.setAddress(rs.getString("address"));
                customer.setAddress(address);
                
                logger.info("Customer {} fetched successfully", cid);
                return customer;
            }else {
                logger.warn("No customer found with ID: {}", cid);
            }
        } catch (SQLException ex) {
        	logger.error("Error fetching customer {}: {}", cid, ex.getMessage(), ex);
            System.err.println("Error fetching customer: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    // Get first customer (for testing)
    public static Customer getFirstCustomer() {
        List<Customer> customers = getAllCustomers();
        return customers.isEmpty() ? null : customers.get(0);
    }
}