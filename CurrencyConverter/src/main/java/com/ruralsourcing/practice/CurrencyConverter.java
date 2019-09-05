package com.ruralsourcing.practice;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

public class CurrencyConverter extends JPanel implements ActionListener{
	public double currencyAmount;
	public Currency currencyType;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	static Currency[] currencies = getCurrencies();
	JButton saveButton;
	JButton solve;
	JTextArea log;
	JFileChooser fc;
	JTextField tf;
	private static String dbURL = "jdbc:derby://localhost:1527/C:/Users/sean.stanley/MyDB";
	private static String tableName = "CURRENCIES";
	private static Connection conn = null;
    private static Statement stmt = null;

    private static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
    
    private static void getCurrencies2()
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                int id = results.getInt(1);
                String restName = results.getString(2);
                String cityName = results.getString(3);
                System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
    
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
            StreamResult streamResult = new StreamResult(new File("C:/Users/sean.stanley/git/Currency-Converter/CurrencyConverter/currency.xml"));
 
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
		super(new BorderLayout());
		
		this.currencyAmount = amount;
		this.currencyType = type;
		
		setSize(400,400);
		fc = new JFileChooser();

		log = new JTextArea(12, 45);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("CURRENCIES");
		menuBar.add(fileMenu);
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		fileMenu.add(saveButton);
		
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter Code");
		tf = new JTextField(10);
		solve = new JButton("Solve");
		solve.addActionListener(this);
		panel.add(label);
		panel.add(tf);
		panel.add(solve);
		
		add(BorderLayout.PAGE_END, panel);
		add(BorderLayout.PAGE_START, menuBar);
		add(BorderLayout.CENTER, logScrollPane);
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

	private void setCurrencyType(CurrencyConverter current) {
		current.currencyType = chooseCurrencyType(current, "Please select new currency type:" + "\n");
	}

	private Currency chooseCurrencyType(CurrencyConverter current, String start) {
		String[] choiceArray = getNames();
		String message = start;
		for (int i = 1; i <= choiceArray.length; i++) {
			message += ("Enter " + i + " to select " + choiceArray[i - 1] + "\n");
		}
		
		try {
			int type = Integer.parseInt(JOptionPane.showInputDialog(message)) - 1;
			if(type >= 0 && type < choiceArray.length) {
				return currencies[type];
			}
			else {
				log.append("Invalid input. There is no currency of that type." + "\n");
			}
		}
		catch(NumberFormatException e) {
			log.append("Invalid input. There is no currency of that type." + "\n");
		}
		return current.currencyType;
	}

	private void setCurrencyAmount(CurrencyConverter session) {
		try {
		Double amount = Double.parseDouble(JOptionPane.showInputDialog("Please enter new currency amount:"));
		currencyAmount = amount;
		}
		catch(NumberFormatException e) {
			log.append("Invalid input. Amount must be a number with or without a decimal point." + "\n");
		}
	}
	
	private void addCurrencyAmount(CurrencyConverter current) {
		Currency addedCurrencyType = chooseCurrencyType(current, "Please enter currency type for the currency amount to be added:" + "\n");
		try {
			double amount = Double.parseDouble(JOptionPane.showInputDialog("Please enter currency amount to be added of type " + addedCurrencyType.getName() + ":"));
			currencyAmount += singleConvert(amount, addedCurrencyType.getRate(), current.getCurrencyType());
		}
		catch(NumberFormatException e){
			log.append("Invalid input. Amount must be a number with or without a decimal point." + "\n");
		}
	}

	private Currency getConvertCurrencySelection(CurrencyConverter current) {
		return chooseCurrencyType(current, "Please enter currency type to convert to:" + "\n");
	}
	
	public static void menuConvert(CurrencyConverter current, Currency currency) {
		current.currencyAmount = singleConvert(current.getCurrencyAmount(), current.getCurrencyTypeConversion(), currency);
		current.currencyType = currency;
	}
	
	public static double singleConvert(double amount, double conversion, Currency currency) {
		return Double.parseDouble(df2.format(amount / conversion * currency.getRate()));
	}

	private void addNewCurrencySelection(CurrencyConverter session) {
		String key = JOptionPane.showInputDialog("Please enter the name of the new currency to add:");
		
		try {
			double value = Double.parseDouble(JOptionPane.showInputDialog("Please enter the US Dollar to " + key + " conversion rate:"));
			currencies = ArrayUtils.add(currencies, new Currency(key, value, " ", currencies.length + 1));
			log.append("Currency " + key + " has been added with a US Dollar to " + key + " exchange rate of " + value + "\n");
		}
		catch(NumberFormatException e) {
			log.append("Invalid input. Conversion Rate must be a number with or without a decimal point." + "\n");
		}
		
	}
	
	private void removeCurrencySelection(CurrencyConverter current) {
		Currency choice = current.chooseCurrencyType(current, "Choose a currency to remove:" + "\n");
		
		if (Arrays.asList(currencies).contains(choice) && !choice.equals(current.getCurrencyType()) && !choice.equals(currencies[0])) {
			currencies = ArrayUtils.remove(currencies, choice.getId() - 1);
			updateIds();
			log.append(choice + " has been removed from the currency selection list." + "\n");
		}
		else {
			log.append("Invalid input. You cannot remove the currently in use Currency Type or the US_Dollar Currency Type or you failed to correctly select an option." + "\n");
		}
	}
	
	private void updateCurrencyRate(CurrencyConverter current) {
		Currency key = chooseCurrencyType(current, "Choose currency for Conversion Rate update:" + "\n");
		
		if (!key.equals(currencies[0])) {
			log.append("Please enter the updated US Dollar to " + key.getName() + " conversion rate:");
			try {
				double value = Double.parseDouble(current.tf.getText());
				key.setRate(value);
				log.append("Currency " + key.getName() + " has been updated with a US Dollar to " + key.getName() + " exchange rate of " + value + "\n");
			} catch(NumberFormatException e) {
				log.append("Invalid input. Conversion Rate must be a number with or without a decimal point." + "\n");
			}
		}
		else {
			log.append("Invalid input. Conversion Rate for US_Dollar can not be changed." + "\n");
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
        //Handle Solve button action.
        if (arg0.getSource() == solve) {
			getMenuChoice(this);
            tf.setText("");
            log.setCaretPosition(log.getDocument().getLength());
        }
        else if (arg0.getSource() == saveButton) {
			saveCurrencies();
			log.append("Currencies have been saved." + "\n");
        }
	}
	
	private static void mainMenuBlurb(CurrencyConverter session) {
		session.log.append("Welcome to the Currency Converter, your current status is: Amount " + session.getCurrencyAmount() + ", Type " + session.getCurrencyType().getName() + "\n");
		session.log.append("Enter 1 to set Currency Amount in the current Currency Type" + "\n");
		session.log.append("Enter 2 to add an amount of a chosen Currency Type to the current Amount" + "\n");
		session.log.append("Enter 3 to set currency type" + "\n");
		session.log.append("Enter 4 to convert current amount and type to another currency" + "\n");
		session.log.append("Enter 5 to add a new currency type and exchange rate" + "\n");
		session.log.append("Enter 6 to remove an existing currency and exchange rate" + "\n");
		session.log.append("Enter 7 to update an existing currencies' exchange rate" + "\n");
	}
	
	public static void startInstance() {
		CurrencyConverter session = new CurrencyConverter();
		//Create and set up the window.
        JFrame frame = new JFrame("Currency Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(session);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
		mainMenuBlurb(session);
	}

	private static void getMenuChoice(CurrencyConverter session) {
		int choice;
		try {
			choice = Integer.parseInt(session.tf.getText());
		} catch(NumberFormatException e) {
			choice = 0;
		}
		session.tf.setText("");
		
		switch (choice) {
		case 1:
		    // Set currency amount in current currency
			session.setCurrencyAmount(session);
			mainMenuBlurb(session);
		    break;
		case 2:
		    // Add an amount of a chosen Currency Type to the current Amount
			session.addCurrencyAmount(session);
			mainMenuBlurb(session);
		    break;
		case 3:
		    // Set currency type
			session.setCurrencyType(session);
			mainMenuBlurb(session);
		    break;
		case 4:
		    // Convert current currency amount and type
			menuConvert(session, session.getConvertCurrencySelection(session));
			mainMenuBlurb(session);
		    break;
		case 5:
		    // Add new currency type and exchange rate
			session.addNewCurrencySelection(session);
			mainMenuBlurb(session);
		    break;
		case 6:
		    // Remove a currency type and exchange rate
			session.removeCurrencySelection(session);
			mainMenuBlurb(session);
		    break;
		case 7:
		    // Update existing currency type exchange rate
			session.updateCurrencyRate(session);
			mainMenuBlurb(session);
		    break;
		default:
		    // The user input an unexpected choice.
			session.log.append("Not a valid menu option, please try again." + "\n");
		}
	}

	public static void main(String[] args) {
		//startInstance();
		createConnection();
		getCurrencies2();
		shutdown();
	}
}