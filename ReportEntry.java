package Project;

public class ReportEntry {

	private int entry_id;
	private String metric_name;
	private double metric_value;

	// Constructor
	public ReportEntry(int entry_id, String metric_name, double metric_value) {
		this.entry_id = entry_id;
		this.metric_name = metric_name;
		this.metric_value = metric_value;
	}

	// Getters + Setters
	public int getEntry_id() {
		return entry_id;
	}

	public void setEntry_id(int entry_id) {
		this.entry_id = entry_id;
	}

	public String getMetric_name() {
		return metric_name;
	}

	public void setMetric_name(String metric_name) {
		this.metric_name = metric_name;
	}

	public double getMetric_value() {
		return metric_value;
	}

	public void setMetric_value(double metric_value) {
		this.metric_value = metric_value;
	}
}
