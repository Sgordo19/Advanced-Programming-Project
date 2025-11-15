package Project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report {

    private int report_id;
    private Date date_from;
    private Date date_to;
    private Date generated_at;
    private ReportType type;
    private List<ReportEntry> entries;

    // Primary Constructor
    public Report(int report_id, Date date_from, Date date_to, ReportType type) {
        this.report_id = report_id;
        this.date_from = date_from;
        this.date_to = date_to;
        this.generated_at = new Date();
        this.type = type;
        this.entries = new ArrayList<>();
    }

    // Getters and Setters
    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public Date getDate_from() {
        return date_from;
    }

    public void setDate_from(Date date_from) {
        this.date_from = date_from;
    }

    public Date getDate_to() {
        return date_to;
    }

    public void setDate_to(Date date_to) {
        this.date_to = date_to;
    }

    public Date getGenerated_at() {
        return generated_at;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public List<ReportEntry> getEntries() {
        return entries;
    }

    // Functional Methods (Matches UML)
    public void addEntry(ReportEntry entry) {
        entries.add(entry);
        System.out.println("Entry added to Report #" + report_id);
    }

    public void printReport() {
        System.out.println("\n==== REPORT #" + report_id + " ====");
        System.out.println("Type: " + type.getName());
        System.out.println("Generated At: " + generated_at);
        System.out.println("Date Range: " + date_from + " to " + date_to);
        System.out.println("Entries: ");

        for (ReportEntry entry : entries) {
            System.out.println("- " + entry.getMetric_name() + ": " + entry.getMetric_value());
        }
    }
}
