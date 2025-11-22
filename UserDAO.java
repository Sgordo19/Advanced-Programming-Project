package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
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
	        System.out.println("User saved: " + u.getUserID() + " | Rows affected: " + rowsAffected);
	        return rowsAffected > 0;

	    } catch (SQLException ex) {
	        System.err.println("SQL Error: " + ex.getMessage());
	        ex.printStackTrace();
	        return false;
	    }
	}
	
	// Login method
	public static User loginUser(String email, String password) {
	    String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
	    
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
	                    //u = new Manager();
	                    break;
	                case "clerk":
	                   // u = new Clerk();
	                    break;
	                case "driver":
	                    //u = new Driver();
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

	            return u;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // login failed
	}




}
