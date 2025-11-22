package Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO 
{

    // LOGIN: Authenticate
    public User authenticate(String email, String password) 
    {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return mapRowToUser(rs);
                }
            }

        } catch (SQLException e) 
        {
            System.out.println("Error during authentication: " + e.getMessage());
        }
        return null;
    }

    // PUBLIC SAVE METHODS
    public void saveCustomer(Customer customer) 
    { 
    	saveUser(customer, "Customer"); 
    }
    public void saveClerk(Clerk clerk)
    {
    	saveUser(clerk, "Clerk"); 
    }
    public void saveDriver(Driver driver) 
    {
    	saveUser(driver, "Driver"); 
    }
    public void saveManager(Manager manager)
    {
    	saveUser(manager, "Manager"); 
    }


    // ----------------------------------------
    //  GENERIC SAVE METHOD FOR ALL USER TYPES
    // ----------------------------------------
    private void saveUser(User user, String role)
    {

        String sql = "INSERT INTO users " + "(firstName, lastName, email, password, role, address, phoneNumber, assignedVehicle) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) 
        {

            // Common fields
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, role);

            // Address + Phone — only Customer has these
            if (user instanceof Customer cust) 
            {
                ps.setString(6, cust.getAddress());
                ps.setString(7, cust.getPhoneNumber());
            } else 
            {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }

            // Assigned Vehicle — only Driver has this
            if (user instanceof Driver drv) 
            {
                ps.setString(8, drv.getAssignedVehicle());
            } else 
            {
                ps.setNull(8, Types.VARCHAR);
            }

            ps.executeUpdate();

            // GET AUTO ID
            try (ResultSet keys = ps.getGeneratedKeys()) 
            {
                if (keys.next())
                {
                    user.setUserID(keys.getInt(1));
                }
            }

        } catch (SQLException e) 
        {
            System.out.println("Error saving " + role + ": " + e.getMessage());
        }
    }


    // ----------------------------------------
    //  FIND ALL USERS — MANAGER VIEW
    // ----------------------------------------
    public List<User> findAllUsers() 
    {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) 
        {

            while (rs.next())
            {
                User user = mapRowToUser(rs);
                if (user != null) list.add(user);
            }

        } catch (SQLException e) 
        {
            System.out.println("Error loading users: " + e.getMessage());
        }

        return list;
    }


    // ----------------------------------------
    //  MAP DATABASE ROW → USER SUBCLASS
    // ----------------------------------------
    private User mapRowToUser(ResultSet rs) throws SQLException 
    {

        int userID = rs.getInt("userID");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");

        String address = safeGet(rs, "address");
        String phoneNumber = safeGet(rs, "phoneNumber");
        String assignedVehicle = safeGet(rs, "assignedVehicle");

        if (role == null) role = "";

        return switch (role.toLowerCase()) 
        		{

            case "customer" -> new Customer(
                    userID, firstName, lastName, email, password,
                    address, phoneNumber
            );

            case "clerk" -> new Clerk(
                    userID, firstName, lastName, email, password
            );

            case "driver" -> new Driver(
                    userID, firstName, lastName, email, password,
                    assignedVehicle
            );

            case "manager" -> new Manager(
                    userID, firstName, lastName, email, password
            );

            default -> 
            {
                System.out.println("Unknown role for userID " + userID + ": " + role);
                yield null;
            }
        };
    }


    // SAFE GETTER
    private String safeGet(ResultSet rs, String column) 
    {
        try 
        {
            String value = rs.getString(column);
            return value != null ? value : "";
        } catch (SQLException e) 
        {
            return "";
        }
    }
}
