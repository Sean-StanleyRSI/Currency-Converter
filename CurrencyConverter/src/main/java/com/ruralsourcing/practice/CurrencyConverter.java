package com.ruralsourcing.practice;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CurrencyConverter 
							{
	public double currencyAmount;
	public Currency currencyType;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	static Currency[] currencies = getCurrencies();

	private static Currency[] getCurrencies() {
		Currency[] newCurrencies = null;
		
		try {
			File fXmlFile = new File("C:/Users/sean.stanley/git/Currency-Converter/CurrencyConverter/currency.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
					
			doc.getDocumentElement().normalize();
					
			NodeList nList = doc.getElementsByTagName("currency");

			newCurrencies = new Currency[nList.getLength()];
	
			for (int temp = 0; temp < nList.getLength(); temp++) {
	
				Node nNode = nList.item(temp);
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					String textRate = eElement.getElementsByTagName("rate").item(0).getTextContent();
					double rate = Double.parseDouble(textRate);
					newCurrencies[temp] = new Currency(eElement.getElementsByTagName("name").item(0).getTextContent(), rate, 
							eElement.getElementsByTagName("symbol").item(0).getTextContent(), Integer.parseInt(eElement.getAttribute("id")));
				}
			
			}
		}
		catch (Exception e) {
		e.printStackTrace();
	    }
		
		return newCurrencies;
	}
	
	private static void saveCurrencies() {
		try {
			 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
 
            Document document = documentBuilder.newDocument();
 
            // root element
            Element root = document.createElement("currencies");
            document.appendChild(root);
            
            for (Currency each : currencies) {
	            // currency element
	            Element currency = document.createElement("currency");
	 
	            root.appendChild(currency);
	 
	            // set an attribute to currency element
	            Attr attr = document.createAttribute("id");
	            attr.setValue(Integer.toString(each.getId()));
	            currency.setAttributeNode(attr);
	 
	            // name element
	            Element name = document.createElement("name");
	            name.appendChild(document.createTextNode(each.getName()));
	            currency.appendChild(name);
	 
	            // rate element
	            Element rate = document.createElement("rate");
	            rate.appendChild(document.createTextNode(Double.toString(each.getRate())));
	            currency.appendChild(rate);
	 
	            // symbol elements
	            Element symbol = document.createElement("symbol");
	            symbol.appendChild(document.createTextNode(each.getSymbol()));
	            currency.appendChild(symbol);
            }
 
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("/Users/sean.stanley/Practice/CurrencyConverter/currency.xml"));
 
            transformer.transform(domSource, streamResult);
 
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
	}
	
	private String[] getNames() {
		String[] names = new String[currencies.length];
		for (int i = 0; i < currencies.length; i++) {
			names[i] = currencies[i].getName();
		}
		return names;
	}
	
	private void updateIds() {
		for (int i = 0; i < currencies.length; i++) {
			currencies[i].setId(i + 1);
		}
	}
	
	CurrencyConverter() {
		this(0.0, currencies[0]);	    
	}

	CurrencyConverter(double amount) {
		this(amount, currencies[0]);
	}

	CurrencyConverter(Currency type) {
		this(0.0, type);
	}
	
	CurrencyConverter(double amount, Currency type) {
		this.currencyAmount = amount;
		this.currencyType = type;
	}

	private Double getCurrencyTypeConversion() {
		return this.currencyType.getRate();
	}
	
	private Currency getCurrencyType() {
		return this.currencyType;
	}

	private double getCurrencyAmount() {
		return currencyAmount;
	}

	private void setCurrencyType(Scanner scanner, CurrencyConverter current) {
		System.out.println("Please select new currency type:");
		current.currencyType = chooseCurrencyType(scanner, current);
	}

	private Currency chooseCurrencyType(Scanner scanner, CurrencyConverter current) {
		String[] choiceArray = getNames();
		for (int i = 1; i <= choiceArray.length; i++) {
			System.out.println("Enter " + i + " to select " + choiceArray[i - 1]);
		}
		
		if(scanner.hasNextInt()) {
			int type = scanner.nextInt() - 1;
			if(type >= 0 && type < choiceArray.length) {
				return currencies[type];
			}
			else {
				System.out.println("Invalid input. There is no currency of that type.");
			}
		}
		else {
			System.out.println("Invalid input. There is no currency of that type.");
		}
		return current.currencyType;
	}

	private void setCurrencyAmount(Scanner scanner) {
		System.out.println("Please enter new currency amount:");
		if(scanner.hasNextDouble()) {
			double amount = Double.parseDouble(df2.format(scanner.nextDouble()));
			currencyAmount = amount;
		}
		else {
			System.out.println("Invalid input. Amount must be a number with or without a decimal point.");
		}
	}
	
	private void addCurrencyAmount(Scanner scanner, CurrencyConverter current) {
		System.out.println("Please enter currency type for the currency amount to be added:");
		Currency addedCurrencyType = chooseCurrencyType(scanner, current);
		System.out.println("Please enter currency amount to be added of type " + addedCurrencyType.getName() + ":");
		if(scanner.hasNextDouble()) {
			double amount = scanner.nextDouble();
			currencyAmount += singleConvert(amount, addedCurrencyType.getRate(), current.getCurrencyType());
		}
		else {
			System.out.println("Invalid input. Amount must be a number with or without a decimal point.");
		}
	}

	private Currency getConvertCurrencySelection(Scanner scanner, CurrencyConverter current) {
		System.out.println("Please enter currency type to convert to:");
		return chooseCurrencyType(scanner, current);
	}
	
	public static void menuConvert(CurrencyConverter current, Currency currency) {
		current.currencyAmount = singleConvert(current.getCurrencyAmount(), current.getCurrencyTypeConversion(), currency);
		current.currencyType = currency;
	}
	
	public static double singleConvert(double amount, double conversion, Currency currency) {
		return Double.parseDouble(df2.format(amount / conversion * currency.getRate()));
	}

	private void addNewCurrencySelection(Scanner scanner) {
		System.out.println("Please enter the name of the new currency to add:");
		scanner.nextLine();
		
		if(scanner.hasNext()) {
			String key = scanner.nextLine();
			System.out.println("Please enter the US Dollar to " + key + " conversion rate:");
			if (scanner.hasNextDouble()) {
				double value = scanner.nextDouble();
				currencies = ArrayUtils.add(currencies, new Currency(key, value, " ", currencies.length + 1));
				System.out.println("Currency " + key + " has been added with a US Dollar to " + key + " exchange rate of " + value);
			}
			else {
				System.out.println("Invalid input. Conversion Rate must be a number with or without a decimal point.");
			}
		}
	}
	
	private void removeCurrencySelection(Scanner scanner, CurrencyConverter current) {
		System.out.println("Choose a currency to remove:");
		Currency choice = current.chooseCurrencyType(scanner, current);
		
		if (Arrays.asList(currencies).contains(choice) && !choice.equals(current.getCurrencyType()) && !choice.equals(currencies[0])) {
			currencies = ArrayUtils.remove(currencies, choice.getId() - 1);
			updateIds();
			System.out.println(choice + " has been removed from the currency selection list.");
		}
		else {
			System.out.println("Invalid input. You cannot remove the currently in use Currency Type or the US_Dollar Currency Type or you failed to correctly select an option.");
		}
	}
	
	private void updateCurrencyRate(Scanner scanner, CurrencyConverter current) {
		Currency key = chooseCurrencyType(scanner, current);
		
		if (!key.equals(currencies[0])) {
			System.out.println("Please enter the updated US Dollar to " + key.getName() + " conversion rate:");
			if (scanner.hasNextDouble()) {
				double value = scanner.nextDouble();
				key.setRate(value);;
				System.out.println("Currency " + key.getName() + " has been updated with a US Dollar to " + key.getName() + " exchange rate of " + value);
			}
			else {
				System.out.println("Invalid input. Conversion Rate must be a number with or without a decimal point.");
			}
		}
		else {
			System.out.println("Invalid input. Conversion Rate for US_Dollar can not be changed.");
		}
		
	}
	
	private static void mainMenuBlurb(CurrencyConverter session) {
		System.out.println("Welcome to the Currency Converter, your current status is: Amount " + session.getCurrencyAmount() + ", Type " + session.getCurrencyType().getName());
		System.out.println("Enter 1 to set Currency Amount in the current Currency Type");
		System.out.println("Enter 2 to add an amount of a chosen Currency Type to the current Amount");
		System.out.println("Enter 3 to set currency type");
		System.out.println("Enter 4 to convert current amount and type to another currency");
		System.out.println("Enter 5 to add a new currency type and exchange rate");
		System.out.println("Enter 6 to remove an existing currency and exchange rate");
		System.out.println("Enter 7 to update an existing currencies' exchange rate");
		System.out.println("Enter 8 to exit program");
	}
	
	public static void startInstance() {
		CurrencyConverter session = new CurrencyConverter();
		mainMenuBlurb(session);
	    
	    while (true) {
	    	int choice = 0;
		    Scanner scanner = new Scanner(System.in);
	    	if (scanner.hasNextInt()) {
	    		choice = scanner.nextInt();
	    	}
		    switch (choice) {
	        case 1:
	            // Set currency amount in current currency
	        	session.setCurrencyAmount(scanner);
	        	mainMenuBlurb(session);
	            break;
	        case 2:
	            // Add an amount of a chosen Currency Type to the current Amount
	        	session.addCurrencyAmount(scanner, session);
	        	mainMenuBlurb(session);
	            break;
	        case 3:
	            // Set currency type
	        	session.setCurrencyType(scanner, session);
	        	mainMenuBlurb(session);
	            break;
	        case 4:
	            // Convert current currency amount and type
	        	menuConvert(session, session.getConvertCurrencySelection(scanner, session));
	        	mainMenuBlurb(session);
	            break;
	        case 5:
	            // Add new currency type and exchange rate
	        	session.addNewCurrencySelection(scanner);
	        	mainMenuBlurb(session);
	            break;
	        case 6:
	            // Remove a currency type and exchange rate
	        	session.removeCurrencySelection(scanner, session);
	        	mainMenuBlurb(session);
	            break;
	        case 7:
	            // Update existing currency type exchange rate
	        	session.updateCurrencyRate(scanner, session);
	        	mainMenuBlurb(session);
	            break;
	        case 8:
	            // Quit
	        	System.out.println("Exiting program, have a nice day.");
	        	saveCurrencies();
			    scanner.close();
	            return;
	        default:
	            // The user input an unexpected choice.
	        	System.out.println("Not a valid menu option, please try again.");
		    }
	    }
	}

	public static void main(String[] args) {
		startInstance();
	}
}