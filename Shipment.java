package ga;

import java.sql.*;

public class Shipment {
	public String trackingNumber;
	public Customer sender;
	public Customer recipient;
	public Package pkg;
	public Type packageType;
	public double distance;
	public double cost;
	public Status status; 
	public String creationDate;
	public String deliveryDate;
	private static final String PREFIX = "PKG";
	
	
    
    public Shipment() {
        this.trackingNumber = generateTrackingNumber(0);
        this.sender = new Customer();
        this.recipient = new Customer();
        this.pkg = new Package();             
        this.packageType = Type.STANDARD;        
        this.distance = 0.0;
        this.cost = 0.0;
        this.status = Status.PENDING;            
        this.creationDate = generateCreationDate();
        this.deliveryDate = generateDeliveryDate();
    }
    public Shipment(String trackingNumber, Customer sender, Customer recipient,
                    Package pkg,Type packageType, double distance,
                    double cost, Status status, String creationDate, String deliveryDate) 
    {
        this.trackingNumber = trackingNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.pkg = pkg;
        this.packageType = packageType;
        this.distance = distance;
        this.cost = cost;
        this.status = status;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
    }
    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }

    public Customer getRecipient() {
        return recipient;
    }

    public void setRecipient(Customer recipient) {
        this.recipient = recipient;
    }
    
    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }


    public Type getPackageType() {
        return packageType;
    }

    public void setPackageType(Type packageType) {
        this.packageType = packageType;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    //toString() method
    @Override
    public String toString() {
        return "Shipment {" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", package=" + pkg +
                ", distance=" + distance +
                ", cost=" + cost +
                ", status=" + status +
                ", creationDate='" + creationDate + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                '}';
    }
    //auto increment tracking number using database
    public static int getNextTrackingSequence() {
        int nextSeq = 1; 

        String url = "jdbc:mysql://localhost:3306/test_ap";
        String user = "root";
        String pass = "";

        String query = "SELECT MAX(tracking_seq) FROM shipment";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                nextSeq = rs.getInt(1) + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nextSeq;
    }
    
    public static String generateTrackingNumber(int seq)
    {
        String date = java.time.LocalDate.now().toString().replace("-", "");
        
        String formattedSeq = String.format("%06d", seq); // zero-padded

        return PREFIX + "-" + date + "-" + formattedSeq;
        
    }
    public String generateCreationDate()
    {
        String cDate = java.time.LocalDate.now().toString();
       
        
        return cDate;
    }
    public String generateDeliveryDate()
    {
    	String dDate = java.time.LocalDate.now().plusWeeks(1).toString();
    	
    	return dDate;
    }
    public double calculateDistance(int z)
    {
    	for (Zone zone : Zone.values()) 
    	{
    		if (zone.getZoneNumber() == z) 
    		{
    			return zone.getBaseDistance();
    		}
    	}
    	return 0.0;
    }
    public double calculateCost(double w, double d, String m)
    {
    	double mp = 0;
    	
    	for (Type type : Type.values())
    	{
    		if(type.getDescription() == m )
    		{
    			mp = type.getSurchargeMultiplier();
    		}
    		
    	} 
         
        double baseCost = w * d * mp;

        return Math.round(baseCost * 100.0) / 100.0; 
    }
    public void updateStatus()
    {
    	
    }
    public void showStatusHistory()
    {
    	
    }
    
}

