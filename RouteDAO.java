package Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/test_ap";
    private static final String USER = "root";
    private static final String PASS = "";

    // Get all routes
    public static List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT * FROM routes";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Route r = new Route();
                r.setRoute_id(rs.getInt("route_id"));
                r.setRoute_name(rs.getString("route_name"));
                r.setStart_location(rs.getString("start_location"));
                r.setEnd_location(rs.getString("end_location"));
                r.setVehicle_id(rs.getInt("vehicle_id"));
                r.setDriver_id(rs.getString("driver_id"));
                routes.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }

    // Assign vehicle to route
    public static boolean assignVehicleToRoute(int vehicleId, int routeId) {
        String sql;
        boolean updatingDriver = false;
        String driverId = null;

        if (vehicleId > 0) {
            Vehicle vehicle = VehicleDAO.getVehicleById(vehicleId);
            if (vehicle == null) return false;

            driverId = vehicle.getDriver_id();

            // Check how many routes this vehicle is already assigned to
            String countSql = "SELECT COUNT(*) AS route_count FROM routes WHERE vehicle_id = ?";
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement psCount = conn.prepareStatement(countSql)) {

                psCount.setInt(1, vehicleId);
                ResultSet rs = psCount.executeQuery();
                if (rs.next() && rs.getInt("route_count") >= 2) {
                    // Vehicle already assigned to 2 routes
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            sql = "UPDATE routes SET vehicle_id = ?, driver_id = ? WHERE route_id = ?";
            updatingDriver = true;

        } else {
            // Remove vehicle from route
            sql = "UPDATE routes SET vehicle_id = NULL, driver_id = NULL WHERE route_id = ?";
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (updatingDriver) {
                ps.setInt(1, vehicleId);
                ps.setString(2, driverId);  // 
                ps.setInt(3, routeId);
            } else {
                ps.setInt(1, routeId);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Assign driver to route
    public static boolean assignDriverToRoute(String driverId, int routeId) {
        Route route = getRouteById(routeId);
        if (route == null) return false;

        String sql = "UPDATE routes SET driver_id = ? WHERE route_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driverId);
            ps.setInt(2, routeId);
            int updated = ps.executeUpdate();

            // If vehicle assigned, also update vehicle driver
            if (route.getVehicle_id() != 0) {
                VehicleDAO.assignDriverToVehicle(driverId, route.getVehicle_id());
            }

            return updated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get route by ID
    public static Route getRouteById(int routeId) {
        String sql = "SELECT * FROM routes WHERE route_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, routeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Route r = new Route();
                r.setRoute_id(rs.getInt("route_id"));
                r.setRoute_name(rs.getString("route_name"));
                r.setStart_location(rs.getString("start_location"));
                r.setEnd_location(rs.getString("end_location"));
                r.setVehicle_id(rs.getInt("vehicle_id"));
                r.setDriver_id(rs.getString("driver_id"));
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update driver for all routes using a vehicle
    public static void updateDriverForVehicleRoutes(int vehicleId, String driverId) {
        String sql = "UPDATE routes SET driver_id = ? WHERE vehicle_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, driverId);
            ps.setInt(2, vehicleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // Get all routes assigned to a specific driver
    public static List<Route> getRoutesByDriver(String driverId) {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT * FROM routes WHERE driver_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driverId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Route r = new Route();
                r.setRoute_id(rs.getInt("route_id"));
                r.setRoute_name(rs.getString("route_name"));
                r.setStart_location(rs.getString("start_location"));
                r.setEnd_location(rs.getString("end_location"));
                r.setVehicle_id(rs.getInt("vehicle_id"));
                r.setDriver_id(rs.getString("driver_id"));
                routes.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }
}
