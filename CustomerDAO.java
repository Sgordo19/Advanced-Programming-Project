package ga;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/test_ap";
    private static final String USER = "root";
    private static final String PASS = "";

    // Get all customers (for testing - you can select one)
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT CID, firstName, lastName, email, address, phoneNumber FROM customer";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCID(rs.getString("CID"));
                customer.setName(rs.getString("firstName") + " " + rs.getString("lastName"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phoneNumber"));
                
                // Create address
                Destination address = new Destination();
                address.setAddress(rs.getString("address"));
                customer.setAddress(address);
                
                customers.add(customer);
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching customers: " + ex.getMessage());
            ex.printStackTrace();
        }
        return customers;
    }

    // Get customer by CID
    public static Customer getCustomerByCID(String cid) {
        String sql = "SELECT CID, firstName, lastName, email, address, phoneNumber FROM customer WHERE CID = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cid);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setCID(rs.getString("CID"));
                customer.setName(rs.getString("firstName") + " " + rs.getString("lastName"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phoneNumber"));
                
                Destination address = new Destination();
                address.setAddress(rs.getString("address"));
                customer.setAddress(address);
                
                return customer;
            }
        } catch (SQLException ex) {
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