package Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/test_ap";
    private static final String USER = "root";
    private static final String PASS = "";

    // Get all vehicles
    public static List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setVehicle_id(rs.getInt("vehicle_id"));
                v.setDriver_id(rs.getString("driver_id")); 
                v.setV_status(rs.getString("status"));
                v.setMax_quantity(rs.getInt("max_package_capacity"));
                v.setMax_weight(rs.getInt("max_weight_capacity"));
                vehicles.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    // Get the vehicle a driver is currently assigned to
    public static Vehicle getVehicleByDriverId(String driverId) {
        String sql = "SELECT * FROM vehicles WHERE driver_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driverId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Vehicle v = new Vehicle();
                v.setVehicle_id(rs.getInt("vehicle_id"));
                v.setDriver_id(rs.getString("driver_id"));
                v.setV_status(rs.getString("status"));
                v.setMax_quantity(rs.getInt("max_package_capacity"));
                v.setMax_weight(rs.getInt("max_weight_capacity"));
                return v;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Assign or reassign a driver to a vehicle
    public static boolean assignDriverToVehicle(String driverId, int vehicleId) {
        String sql = "UPDATE vehicles SET driver_id = ? WHERE vehicle_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driverId);
            ps.setInt(2, vehicleId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Remove a driver from a vehicle
    public static boolean removeDriverFromVehicle(int vehicleId) {
        String sql = "UPDATE vehicles SET driver_id = NULL WHERE vehicle_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get vehicle by ID
    public static Vehicle getVehicleById(int vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Vehicle v = new Vehicle();
                v.setVehicle_id(rs.getInt("vehicle_id"));
                v.setDriver_id(rs.getString("driver_id"));
                v.setV_status(rs.getString("status"));
                v.setMax_quantity(rs.getInt("max_package_capacity"));
                v.setMax_weight(rs.getInt("max_weight_capacity"));
                return v;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static double getTotalWeightForVehicle(int vehicleId) {
        String sql = "SELECT SUM(weight) FROM shipments WHERE assigned_vehicle = ? AND status = 'ASSIGNED'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1); // returns 0.0 if null
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static int getTotalQuantityForVehicle(int vehicleId) {
        String sql = "SELECT COUNT(*) FROM shipments WHERE assigned_vehicle = ? AND status = 'ASSIGNED'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
