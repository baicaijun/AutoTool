package com.browser.debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;



public class Property {
	
	
	public static String DefaultPath = "";
	public static String fxprofilepath = "";

	private static Properties setting = new Properties();
	
	public static void SetUp()
	{
		
		//get current jar file path
		try {
			DefaultPath = Property.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			DefaultPath = new File(".").getAbsolutePath();
			if(!Property.class.getProtectionDomain().getCodeSource().getLocation().getFile().toString().contains(".jar"))
			{
				DefaultPath = "C:/QA/AutoTool";
			}
			File fxprofile = new File(DefaultPath, "/conf/fxprofile");
			fxprofilepath = fxprofile.getAbsolutePath();
			System.setProperty("webdriver.firefox.driver", new File(DefaultPath, "/bin/webdriver.xpi").getAbsolutePath());
			System.setProperty("webdriver.chrome.driver", new File(DefaultPath, "/bin/chromedriver.exe").getAbsolutePath());
			System.setProperty("webdriver.ie.driver", new File(DefaultPath, "/bin/IEDriverServer.exe").getAbsolutePath());			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//load properties
		try {
			setting.load(new BufferedReader(new FileReader(new File(DefaultPath, "/conf/setting.conf"))));
			
			AutoTool.CucumberDirectoryPath = setting.getProperty("workspace", "");	

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
