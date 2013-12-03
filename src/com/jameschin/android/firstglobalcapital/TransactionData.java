package com.jameschin.android.firstglobalcapital;

public class TransactionData {
	private String type;
	private String description;
	private String id;
	private Double amount;
	
	public TransactionData(String t, String d, String i, Double a) {
		this.type = t;
		this.description = d;
		this.id = i;
		this.amount = a;
	}
	
	public void setType(String t) {this.type = t;}
	public void setDescription(String d) {this.description = d;}
	public void setId(String i) {this.id = i;}
	public void setAmount(Double a) {this.amount = a;}
	public String getType() {return type;}
	public String getDescription() {return description;}
	public String getId() {return id;}
	public Double getAmount() {return amount;}
}
