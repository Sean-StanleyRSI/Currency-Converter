package com.ruralsourcing.practice;

public class Currency {
	private String name;
	private double conversionRate;
	private String symbol;
	private Integer id;
	
	Currency(String name, double rate, String symbol, Integer id) {
		this.name = name;
		this.conversionRate = rate;
		this.symbol = symbol;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Currency Name: " + this.name + " Currency Conversion Rate: " + this.conversionRate + " Currency Symbol: " + this.symbol;
	}
	
	String getName() {
		return this.name;
	}
	
	double getRate() {
		return this.conversionRate;
	}
	
	String getSymbol() {
		return this.symbol;
	}
	
	int getId() {
		return this.id;
	}
	
	void setName(String newName) {
		this.name = newName;
	}
	
	void setRate(double newRate) {
		this.conversionRate = newRate;
	}
	
	void setSymbol(String newSymbol) {
		this.symbol = newSymbol;
	}
	
	void setId(int newId) {
		this.id = newId;
	}
}
