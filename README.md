# Currency-Converter
This is a project designed to allow simple conversion of currency amounts based on known conversion rates.  
Currently is it run through a Java console and allows the addition and removal of currencies with a record of the currency names and rates stored in XML.  
  
To run the project it is recommended to use the 'Git Repositories' perspective in Eclipse to clone this repository with the url from the GitHub <>Code tab or here: https://github.com/Sean-StanleyRSI/Currency-Converter.git  
Once the project is in your Eclipse you will need to update lines 36 and 114 in the getCurrencies and saveCurrencies methods of CurrencyConverter.java to the correct path for your saved directory and path of the currency.xml file before running the program with the green play/run button.  
  
The program will run in the Eclipse java console and receives typed input into the console depending on the chosen options. After any number of conversions or currency additions/removals/updates have finished and the program is exited correctly the current Currencies records of names and conversion rates will be stored locally in the currency.xml file.  
  
For those interested in looking at the code you can either have it in your Eclipse as written above or use the <>Code tab on GitHub to navigate into the CurrencyConverter project. The CurrencyConverter.java file is the most significant of the files with the Currency.java file only consisting of the currency object creation, sets, gets, and toString.
