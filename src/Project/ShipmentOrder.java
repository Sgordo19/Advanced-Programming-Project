package Project;

public class ShipmentOrder {
	private int orderNum;
	private Customer senderInfo;
	private String recipientName;
	private String recipientAdd;
	private Package aPackage;
	private String status;
	private double cost;
	private int distance;

	// Default Constructor
	ShipmentOrder() {
		orderNum = 0;
		senderInfo = null;
		recipientName = "";
		recipientAdd = "";
		aPackage = null;
		status = "";
		cost = 0.0;
		distance = 0;
	}

	// primary constructor
	ShipmentOrder(int orderNum, Customer senderInfo, String recipientName, String recipientAdd, Package aPackage,
			String status, double cost, int distance) {
		this.orderNum = orderNum;
		this.senderInfo = senderInfo;
		this.recipientName = recipientName;
		this.recipientAdd = recipientAdd;
		this.aPackage = aPackage;
		this.status = status;
		this.cost = cost;
		this.distance = distance;
	}

	// copy constructor
	ShipmentOrder(ShipmentOrder obj) {
		this.orderNum = obj.orderNum;
		this.senderInfo = obj.senderInfo;
		this.recipientName = obj.recipientName;
		this.recipientAdd = obj.recipientAdd;
		this.aPackage = obj.aPackage;
		this.status = obj.status;
		this.cost = obj.cost;
		this.distance = obj.distance;
	}

	// getters and setters
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public Customer getSenderInfo() {
		return senderInfo;
	}

	public void setSenderInfo(Customer senderInfo) {
		this.senderInfo = senderInfo;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getRecipientAdd() {
		return recipientAdd;
	}

	public void setRecipientAdd(String recipientAdd) {
		this.recipientAdd = recipientAdd;
	}

	public Package getaPackage() {
		return aPackage;
	}

	public void setaPackage(Package aPackage) {
		this.aPackage = aPackage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	// method to generate distance
	public int generateDistance(int zone) {

		return distance;
	}

	// method to generate cost
	public double generateCost(String type, double weight, int distance) {

		return cost;
	}

}
