package com.browser.debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class Customer {
	
	
	public static String FirstName = "Gordon";
	public static String LastName = "James";
	public static String HomePhone = "6267753700";
	public static String CellPhone = "6267753701";
	public static String DOB = "01011975";
	public static String SSN = "";
	public static String Email = "";
	public static String Pin = "3663";
	
	public static String[] sCustomerType ;//= {"Approved","Declined","NASDecline","RiskCodeDecline","NASWithKBA","Negative Match","Prison Address","Deceased","OFAC Approved","OFAC NAS"};
	
	public static HashMap<String, String> ssnMaps = new HashMap<String, String>();
	
	public static String[] sAdddressType;
	//QAS Addresses
	//Approved Address
	public final static String[] Approved_Address = {"Approved_Address","3465 E. Foothill Blvd","STE 200","Pasadena","CA","91107"};
	//Unverified Addresses
	public final static String[] Premises_Partial_Address = {"Premises_Partial_Address","3465 E. Foothill Blvd","","Pasadena","CA","91107"};
	public final static String[] Street_Partial_Address = {"Street_Partial_Address","100 Eliot Rd","","Arling","MA","02474"};
	public final static String[] Invalid_Address = {"Invalid_Address","32 nowhere dr","9999","Zzyyxx","CA","71234"};
	public final static String[] Interaction_Required_Address = {"Interaction_Required_Address","605 E Hunting","","Monrovia ","CA","91016"};
	public final static String[] Multiple_Address = {"Multiple_Address","4440 Image","","Dallas","TX","75211"};
	
	//get NPNR card
	public final static String[] PO_Box_Address  = {"PO_Box_Address","P.O. Box 1187","","Monrovia","CA","91017"};
	public final static String[] General_Delivery_Address   = {"General_Delivery_Address","General Delivery","","Yosemite National Park","CA","95389"};
	//Addresses without QAS
	public final static String[] Rural_Route_Address = {"Rural_Route_Address","RR 5 Box 453","","Butler","MO","64730"};
	public final static String[] Highway_Contract_Route_Address = {"Highway_Contract_Route_Address","HC 1 Box 21","","Gillett","TX","78116"};
	private Properties setting = new Properties();
	
	
	
	public Customer()
	{
		
		init();
		//getCustomerType();

	}
	
	public Customer(String customerType)
	{
		
		getSSN(customerType);

	}
	
	private void init()
	
	{

		ssnMaps.put("Approved", "10");
		ssnMaps.put("Declined", "2101");
		ssnMaps.put("NASDecline", "2101");
		ssnMaps.put("RiskCodeDecline", "2121");
		ssnMaps.put("NASWithKBA", "420");
		ssnMaps.put("Negative Match", "610111111");
		ssnMaps.put("Prison Address", "451");
		ssnMaps.put("Deceased", "550");
		ssnMaps.put("OFAC Approved", "401");
		ssnMaps.put("OFAC NAS", "440");
		ssnMaps.put("OFAC Failed", "421");				
				
	
	}
	
	public void getCustomerType()
	{
		Iterator<Entry<String, String>> key = ssnMaps.entrySet().iterator();
		for(int i=0;i<ssnMaps.size();i++)
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)key.next();
			sCustomerType[i] = (String) entry.getKey();
			
		}
	}
	
	public void getSSN(String customerType)
	{
		try {
			setting.load(new BufferedReader(new FileReader(new File(Property.DefaultPath , "/conf/setting.conf"))));
			Email = setting.getProperty("email", "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		SSN = ssnMaps.get(customerType) + Utils.getRandomNumeric(9 - ssnMaps.get(customerType).length());
		Email = !Email.equals("") ? Email : "PREP_" + Utils.getRandomNumeric(18) + "@greendotcorp.com";
		
	}
	
	
	public String[] getAddress(String sAddressType){
		String[] sAddress;
		if(Approved_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = Approved_Address;
		
		else if(Premises_Partial_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = Premises_Partial_Address;
		
		else if(Street_Partial_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = Street_Partial_Address;
		
		else if(Invalid_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = Invalid_Address;
		
		else if(Interaction_Required_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = Interaction_Required_Address;
		
		else if(Multiple_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = Multiple_Address;
		
		else if(PO_Box_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = PO_Box_Address;
		
		else if(General_Delivery_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = General_Delivery_Address;
		
		else if(Rural_Route_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = Rural_Route_Address;
		
		else if(Highway_Contract_Route_Address[0].equalsIgnoreCase(sAddressType))
			sAddress = Highway_Contract_Route_Address;
		
		else
			sAddress = null;
			//throw new Exception("not implemented address");
		return sAddress;
	}
	
}
