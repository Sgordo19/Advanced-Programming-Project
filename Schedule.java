package Project;

public class Schedule {
	private int schedule_id;
	private boolean s_status;	
	public Schedulequeue route;
	private Vehicle vehicle;
	// public static time start_time;
	// public static time end_time;
	
	public Schedule(int id, int pg)
	{
		schedule_id = 0;
		s_status = false;
		this.vehicle= new Vehicle();
		this.route = new Schedulequeue();
	}
	
	public Schedule(int schedule_id, boolean s_status, Schedulequeue route, Vehicle vehicle)
	{
		this.schedule_id = schedule_id;
		this.s_status = s_status;
		this.route = route;
		this.vehicle = vehicle;
	}
	
	public Schedule(Schedule obj)
	{
		this.schedule_id = obj.schedule_id;
		this.s_status = obj.s_status;
		this.route = obj.route;
		this.vehicle = obj.vehicle;
	}

	public int getSchedule_id() {
		return schedule_id;
	}

	public void setSchedule_id(int schedule_id) {
		this.schedule_id = schedule_id;
	}

	public boolean isS_status() {
		return s_status;
	}

	public void setS_status(boolean s_status) {
		this.s_status = s_status;
	}

	public Schedulequeue getRoute() {
		return route;
	}

	public void setRoute(Schedulequeue route) {
		this.route = route;
	}
	

}
