package models;

import dbFactories.*;
import view.*;
import controller.*;

import java.util.Date;

public class Invoice extends ShipmentOrder {
	private int invoiceNum;
	private String status;
	private double total; 
	private String trackingNumber;
	private double balance;
	private Date created;
	
	public Invoice() {
		super();
		invoiceNum= 0;
		status="Unpaid";
		total=0.0;
	}
	
	//getters and setters 
	public int getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(int invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	
	
	

}
