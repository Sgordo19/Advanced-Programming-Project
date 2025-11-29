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

public class UserDAO {
	private static final Logger logger = LogManager.getLogger(UserDAO.class);
	
	private static final String URL = "jdbc:mysql://localhost:3306/test_ap";
    private static final String USER = "root";
    private static final String PASS = "";
    
	public static boolean saveUser(User u) {
	    

	    String sql = """
	        INSERT INTO users (
	            user_seq,
	            user_id,
	            first_name,
	            last_name,
	            email,
	            password,
	            role,
	            address,
	            phone_number
	        ) VALUES (?,?,?,?,?,?,?,?,?)
	    """;
	    
	    logger.info("Attempting to save user: {}", u.getEmail());

	    try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	    	
	    	int seq =  User.getNextUserSequence();
	    	String role = u.getRole();
	        ps.setInt(1, seq);
	        	
	        // Full user ID stored in User object
	        ps.setString(2, User.generateUserID(seq, role));

	        ps.setString(3, u.getFirstName() != null ? u.getFirstName() : "");
	        ps.setString(4, u.getLastName() != null ? u.getLastName() : "");
	        ps.setString(5, u.getEmail() != null ? u.getEmail() : "");
	        ps.setString(6, u.getPassword() != null ? u.getPassword() : "");
	        ps.setString(7, u.getRole() != null ? u.getRole() : "");
	        ps.setString(8, u.getAddress() != null ? u.getAddress().toString() : "");
	        ps.setString(9, u.getPhoneNumber() != null ? u.getPhoneNumber() : "");

	        int rowsAffected = ps.executeUpdate();
	        logger.info("User saved: {} | Rows affected: {}", u.getUserID(), rowsAffected);
	        System.out.println("User saved: " + u.getUserID() + " | Rows affected: " + rowsAffected);
	        return rowsAffected > 0;

	    } catch (SQLException ex) {
	    	logger.error("Failed to save user {}: {}", u.getEmail(), ex.getMessage(), ex);
	        System.err.println("SQL Error: " + ex.getMessage());
	        ex.printStackTrace();
	        return false;
	    }
	}
	
	// Login method
	public static User loginUser(String email, String password) {
	    String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
	    
	    logger.info("Login attempt for email: {}", email);
	    
	    try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, email);
	        ps.setString(2, password);

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            String role = rs.getString("role");

	            User u = new User();
	            switch (role.toLowerCase()) {
	                case "customer":
	                    u = new Customer(); 
	                    break;
	                case "manager":
	                    u = new Manager();
	                    break;
	                case "clerk":
	                    u = new Clerk();
	                    break;
	                case "driver":
	                    u = new Driver();
	                    break;
	            }

	            // Populate common fields
	            u.setUserID(rs.getString("user_id"));
	            u.setFirstName(rs.getString("first_name"));
	            u.setLastName(rs.getString("last_name"));
	            u.setEmail(rs.getString("email"));
	            u.setPassword(rs.getString("password"));
	            u.setRole(role);

	            Address addr = new Address();
	            addr.setAddress(rs.getString("address"));
	            u.setAddress(addr);

	            u.setPhoneNumber(rs.getString("phone_number"));
	            logger.info("Login successful for user: {}", u.getUserID());

	            return u;
	        }else {
	        	logger.warn("Login failed for email: {}", email);
	        }
	    }catch (SQLException e) {
	    	logger.error("Login attempt failed due to SQL error for email {}: {}", email, e.getMessage(), e);
	    }catch (Exception e) {
	    	logger.error("Unexpected error during login for email {}: {}", email, e.getMessage(), e);
	        e.printStackTrace();
	    }
	    return null; // login failed
	}

	public static List<User> getAllDrivers() {
	    List<User> drivers = new ArrayList<>();
	    String sql = "SELECT * FROM users WHERE role = 'Driver'";
	    
	    logger.info("Fetching all drivers from database");

	    try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            User driver = new User();
	            driver.setUserID(rs.getString("user_id")); 
	            driver.setFirstName(rs.getString("first_name"));
	            driver.setLastName(rs.getString("last_name"));
	            driver.setRole(rs.getString("role")); 
	            drivers.add(driver);
	        }
	        
	        logger.info("Fetched {} drivers", drivers.size());

	    } catch (SQLException e) {
	    	logger.error("Error fetching drivers: {}", e.getMessage(), e);
	        e.printStackTrace();
	    }

	    return drivers;
	}



}