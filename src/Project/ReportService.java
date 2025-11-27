package Project;

import java.util.*;
import java.text.SimpleDateFormat;

public class ReportService {

private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

// ===================== Shipment Report =====================
public static Report<Shipment> generateShipmentReport(Date from, Date to) {
    Report<Shipment> report = new Report<>();
    report.setReport_id(0);
    report.setType("Shipment");
    report.setDate_from(from);
    report.setDate_to(to);
    report.setGenerated_at(new Date());

    Calendar cal = Calendar.getInstance();
    cal.setTime(from);

    while (!cal.getTime().after(to)) {
        Date current = cal.getTime();
        List<Shipment> shipments = ShipmentDAO.getShipmentsByDate(current);
        shipments.forEach(report::addEntry);
        cal.add(Calendar.DATE, 1);
    }

    return report;
}

// ===================== Delivery Performance Report =====================
public static Report<String> generateDeliveryPerformanceReport(Date from, Date to) {
    Report<String> report = new Report<>();
    report.setReport_id(0);
    report.setType("Delivery Performance");
    report.setDate_from(from);
    report.setDate_to(to);
    report.setGenerated_at(new Date());

    List<Shipment> allShipments = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    cal.setTime(from);
    while (!cal.getTime().after(to)) {
        allShipments.addAll(ShipmentDAO.getShipmentsByDate(cal.getTime()));
        cal.add(Calendar.DATE, 1);
    }

    int onTime = 0;
    int delayed = 0;

    for (Shipment s : allShipments) {
        try {
            Date expected = sdf.parse(s.getDeliveryDate());
            Date actual = sdf.parse(s.getDeliveryDate());
            if (!actual.after(expected)) onTime++;
            else delayed++;
        } catch (Exception e) {
            delayed++;
        }
    }

    report.addEntry("Total Shipments: " + allShipments.size());
    report.addEntry("On-time Deliveries: " + onTime);
    report.addEntry("Delayed Deliveries: " + delayed);

    return report;
}

// ===================== Revenue Report =====================
public static Report<String> generateRevenueReport(Date from, Date to) {
    Report<String> report = new Report<>();
    report.setReport_id(0);
    report.setType("Revenue");
    report.setDate_from(from);
    report.setDate_to(to);
    report.setGenerated_at(new Date());

    List<Shipment> allShipments = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    cal.setTime(from);
    while (!cal.getTime().after(to)) {
        allShipments.addAll(ShipmentDAO.getShipmentsByDate(cal.getTime()));
        cal.add(Calendar.DATE, 1);
    }

    double totalRevenue = allShipments.stream().mapToDouble(Shipment::getCost).sum();
    report.addEntry("Total Shipments: " + allShipments.size());
    report.addEntry("Total Revenue: $" + String.format("%.2f", totalRevenue));

    return report;
}

// ===================== Vehicle Utilization Report =====================
public static Report<String> generateVehicleUtilizationReport() {
    Report<String> report = new Report<>();
    report.setReport_id(0);
    report.setType("Vehicle Utilization");
    report.setDate_from(new Date());
    report.setDate_to(new Date());
    report.setGenerated_at(new Date());

    List<Vehicle> vehicles = VehicleDAO.getAllVehicles();

    for (Vehicle v : vehicles) {
        double totalWeight = VehicleDAO.getTotalWeightForVehicle(v.getVehicle_id());
        int totalQty = VehicleDAO.getTotalQuantityForVehicle(v.getVehicle_id());
        double weightPercent = (v.getMax_weight() > 0) ? (totalWeight / v.getMax_weight() * 100) : 0;
        double qtyPercent = (v.getMax_quantity() > 0) ? (totalQty / (double) v.getMax_quantity() * 100) : 0;

        report.addEntry("Vehicle #" + v.getVehicle_id());
        report.addEntry("  Packages: " + totalQty + " / " + v.getMax_quantity() + " (" + String.format("%.2f", qtyPercent) + "%)");
        report.addEntry("  Weight: " + totalWeight + " / " + v.getMax_weight() + " (" + String.format("%.2f", weightPercent) + "%)");
    }

    return report;
}

}
