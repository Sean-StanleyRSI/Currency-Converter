package com.ruralsourcing.practice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "currency")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Currency implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double conversionRate;
	private String symbol;
	private Integer id;
	
	Currency() {
		super();
	}
	
	Currency(String name, double rate, String symbol, Integer id) {
		super();
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
