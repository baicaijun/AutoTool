package com.browser.debug;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.DefaultComboBoxModel;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.browser.page.PageElements;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import java.awt.Component;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class AutoTool {

	static AutoTool window;
	private JFrame frame;
	private JTextField elementTag;
	private JTextField url;
	private JTextField sParam;
	private JTextField Attribute_CssValue_Field;
	private JTextField elementField;
	private JTextField verificationField;
	private JTextField pageNameField;
	
	private JTextPane logTextPane;
	
	private WebDriver oWebDriver;
	private String sBrowserType;
	private String sCommand;	

	private HashMap<String, PageNode> pageNodeMap  = new LinkedHashMap<String, PageNode>();
	private File file = null;
	//final static String currentDirectoryPath = "C:\\QA\\Cucumber\\Projects\\Web - GreenDot & Affinity\\features\\pages";
	static String CucumberDirectoryPath = "C:\\azheng-QA-Workspace\\QA\\Cucumber\\Projects\\Web - GreenDot & Affinity\\features\\pages";
	final static String ATFDirectoryPath = "C:\\QA\\ATF\\projects\\";
	public JScrollPane elementsScrollPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup actionButtonGroup = new ButtonGroup();
	
	private JComboBox browserType;	
	private JComboBox commandName;	
	private JComboBox commandBox;
	private JComboBox assertComboBox;
	
	private JRadioButton rdbtnNavigation;
	private JRadioButton rdbtnElementOperation;
	private JRadioButton rdbtnElementVerification;
	private JRadioButton rdbtnPageVerification;
	private JRadioButton rdbtnBrowserActions;
	
	private JLabel lblElementName;
	private JLabel lblExpectedtext;
	private JLabel lblAttributecssvalue;
	private JLabel lblAsserttype;
	private JLabel lblPagename;
	
	//Jtree
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;	
    
    public static String PageName = "";
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new AutoTool();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AutoTool() {
		
		Property.SetUp();		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 779, 733);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		browserType = new JComboBox();
		browserType.setModel(new DefaultComboBoxModel(new String[] { "Chrome","Firefox", "IE"}));
		browserType.setBounds(18, 61, 71, 22);
		frame.getContentPane().add(browserType);
		
		final JButton btnStart = new JButton("Start");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Thread t = null;
					if(btnStart.getText().equals("Start"))
					{
						sBrowserType = browserType.getSelectedItem().toString();
						System.out.println(sBrowserType);
						oWebDriver = new Driver().StartWebDriver(sBrowserType);		
						oWebDriver.manage().timeouts().pageLoadTimeout(5000, TimeUnit.MILLISECONDS);
						btnStart.setText("Stop");
						//Set a page reload frequency
						 t = new Thread(){
							
							 @Override
							 public void run()
							 {
								 while(!Thread.interrupted())
								 {									 
									 try {
										if(oWebDriver==null || !Driver.Reload)
										{
											break;
										}										
										Thread.sleep(15*60*1000);
										oWebDriver.navigate().refresh();
										System.out.println("reload page");
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 
								 }

							 }
	
						};
						t.start();
					}
					
					else if(btnStart.getText().equals("Stop"))
					{
						try {
								if(oWebDriver!=null)
								{
									oWebDriver.quit();
								}
						} catch (Exception e1) {							
							logTextPane.setText(logTextPane.getText() + "Browser may already be closed" + "\n");
						}	
						
						btnStart.setText("Start");						
					}
					else
					{
						//TODO nothing
					}
					
					
				} catch (Exception e1) {
					e1.printStackTrace();
					logTextPane.setText(logTextPane.getText() + "faile to start webdriver" + "\n");
					logTextPane.setText(logTextPane.getText() + e1.toString() + "\n");
				}
			}
		});
		btnStart.setBounds(118, 61, 71, 23);
		frame.getContentPane().add(btnStart);
		
		elementTag = new JTextField();
		elementTag.setBounds(10, 164, 316, 21);
		frame.getContentPane().add(elementTag);
		elementTag.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String responseText;
				sCommand = commandName.getSelectedItem().toString();
				String param = sParam.getText();
				System.out.println(sCommand);
				System.out.println(param);
				
				if("".equalsIgnoreCase(elementTag.getText()))
				{
					//responseText = new DebugRemoteDriver(remoteAddress,sessionid).sendCommand(sCommand, param);	
					responseText = new DebugRemoteDriver(oWebDriver).sendCommand(sCommand, param);
					System.out.println(responseText);
					logTextPane.setText(logTextPane.getText()+responseText+"\n");
					
				}
				else
				{
					//responseText = new DebugWebElement(elementTag.getText(),new DebugRemoteDriver(remoteAddress,sessionid)).sendCommand(sCommand, param);				
					responseText = new DebugWebElement(elementTag.getText(),oWebDriver).sendCommand(sCommand, param);
					System.out.println(responseText);
					logTextPane.setText(logTextPane.getText()+responseText+"\n");
				}
					System.gc();
			}
		});
		btnSend.setBounds(336, 196, 86, 23);
		frame.getContentPane().add(btnSend);
		
		commandName = new JComboBox();
		Commands commands = new Commands();
		//commandName.setModel(new DefaultComboBoxModel(commands.getAllCommands()));
		commandName.setModel(new DefaultComboBoxModel(new String[] {"click", "clear", "getCurrentUrl","getTitle", "sendKeys", "mouseover","isDisplayed", "isEnabled", "isSelected", "getText", "getTagName","getElementSelector", "getAlertText", "acceptAlert", "dismissAlert", "execute", "getCssValue", "getAttribute", "getWindowHandle", "getWindowHandles","switchToWindow", "getElementInfo","addNewElement","updateElement"}));
		//List<String> commands =  Arrays.asList(DebugWebElement.ElementKeyword);
		//commands.addAll(Arrays.asList(DebugRemoteDriver.WebDriverKeyword));		
		//commandName.setModel(new DefaultComboBoxModel(commands.toArray()));
		commandName.setToolTipText("");
		commandName.setBounds(66, 196, 87, 22);
		frame.getContentPane().add(commandName);
		
		JButton btnHighlight = new JButton("Highlight");
		btnHighlight.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(oWebDriver!=null)
				{
					try {
						window.frame.setVisible(false);
						DebugWebElement debugelement = new DebugWebElement(elementTag.getText(),oWebDriver);						
						if(!debugelement.oWebElement.isDisplayed())
							logTextPane.setText(logTextPane.getText() + "Element is hidden" + "\n");
						else
							debugelement.highlightMe();
					} catch (NoSuchElementException e1) {						
						logTextPane.setText(logTextPane.getText() + "Element not Found" + "\n");
						e1.printStackTrace();						
					} catch (Exception e1) {						
						logTextPane.setText(logTextPane.getText() + "Unknow error" + "\n");
						e1.printStackTrace();						
					} finally{
						window.frame.setVisible(true);
					}
				}
			}
		});
		btnHighlight.setBounds(336, 163, 86, 23);
		frame.getContentPane().add(btnHighlight);
		
		JLabel lblElementTag = new JLabel("Element Tag");
		lblElementTag.setBounds(11, 149, 91, 14);
		frame.getContentPane().add(lblElementTag);
		
		JLabel lblCommand = new JLabel("Command");
		lblCommand.setBounds(10, 196, 59, 22);
		frame.getContentPane().add(lblCommand);
		
		sParam = new JTextField();
		sParam.setBounds(161, 196, 164, 21);
		frame.getContentPane().add(sParam);
		sParam.setColumns(10);
		
		url = new JTextField();
		url.setBounds(10, 106, 316, 20);
		frame.getContentPane().add(url);
		url.setColumns(10);
		
		//nav to desired url
		JButton btnGoto = new JButton("goTo");
		btnGoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					URL sUrl = new URL(url.getText());
					System.out.println(sUrl);
					oWebDriver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
					oWebDriver.get(sUrl.toString());
				} catch (MalformedURLException e) {	
					logTextPane.setText(logTextPane.getText() + "Wrong url form" + "\n");
					e.printStackTrace();
				} catch (Exception e) {	
					e.printStackTrace();
				}						
				
			}
		});
		btnGoto.setBounds(336, 105, 86, 23);
		frame.getContentPane().add(btnGoto);
		
		final JTree tree = new JTree();

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				
				if(selectionNode!=null)
				{		
					String sPageUrl = "";
					
					if(selectionNode.isLeaf())
					{
						sPageUrl = pageNodeMap.get(selectionNode.getParent().toString()).url;
						pageNameField.setText(selectionNode.getParent().toString());
						elementField.setText(selectionNode.toString().trim());
						elementTag.setText(pageNodeMap.get(selectionNode.getParent().toString()).elementsMap.get(selectionNode.toString().trim()));
						
						ElementHelper.setElementName(selectionNode.toString().trim());
						AutoTool.PageName = selectionNode.getParent().toString();
					}
					else if(!selectionNode.isRoot())
					{
						sPageUrl = pageNodeMap.get(selectionNode.toString()).url;
						pageNameField.setText(selectionNode.toString());
						elementField.setText("");
						elementTag.setText("");
						AutoTool.PageName = selectionNode.toString();
					}
					url.setText(sPageUrl);
						
				}

			}
		});		

		rootNode = new DefaultMutableTreeNode("Web Elements");
		treeModel = new DefaultTreeModel(rootNode);
		tree.setModel(treeModel);
		
		elementsScrollPane = new JScrollPane(tree);
		elementsScrollPane.setBounds(453, 69, 250, 369);
		frame.getContentPane().add(elementsScrollPane);
		
		logTextPane = new JTextPane();
		logTextPane.setBounds(66, 386, 6, 20);
		
		JScrollPane logScrollPane = new JScrollPane(logTextPane);
		logScrollPane.setBounds(5, 230, 430, 180);
		frame.getContentPane().add(logScrollPane);
		
		JLabel lblNewLabel = new JLabel("Action");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(30, 421, 59, 23);
		frame.getContentPane().add(lblNewLabel);

		commandBox = new JComboBox();
		commandBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String command = commandBox.getSelectedItem().toString();
				if(command.matches("(visit|on)"))
				{
					pageNameField.setEditable(true);	
				}
				else
				{
					pageNameField.setEditable(false);
				}	

				if(Arrays.asList(StatementMaps.sElementOperation).contains(command)||Arrays.asList(StatementMaps.sElementVerification).contains(command)||command.matches("(doescontain|doesnotcontain)"))
				{
					
					elementField.setEditable(true);	
				}
				else
				{
					elementField.setEditable(false);
				}	
				
				if(command.matches("(fill|fillIfExist|select|Attribute|CssValue|ElementText|ElementValue|movetowindow|close_and_movebacktowindow|doesContainText|doesNotContainText)"))
				{
					verificationField.setEditable(true);	
				}
				else
				{
					verificationField.setEditable(false);
				}	
				
				if(command.matches("(Attribute|CssValue)"))
				{
					Attribute_CssValue_Field.setEditable(true);	
				}
				else
				{
					Attribute_CssValue_Field.setEditable(false);
				}	
				
				if(command.matches("(Attribute|CssValue|ElementText|ElementValue)"))
				{
					assertComboBox.setEditable(true);	
				}
				else
				{
					assertComboBox.setEditable(false);
				}	
				
			}
		});

		commandBox.setModel(new DefaultComboBoxModel(StatementMaps.sNavigation));
		commandBox.setBounds(15, 446, 113, 22);
		frame.getContentPane().add(commandBox);
		
		lblElementName = new JLabel("ElementName");
		lblElementName.setHorizontalAlignment(SwingConstants.CENTER);
		lblElementName.setBounds(129, 421, 105, 23);
		frame.getContentPane().add(lblElementName);
		
		elementField = new JTextField();
		elementField.setBounds(136, 446, 123, 20);
		frame.getContentPane().add(elementField);
		elementField.setColumns(10);
		
		lblExpectedtext = new JLabel("Expected Text");
		lblExpectedtext.setHorizontalAlignment(SwingConstants.CENTER);
		lblExpectedtext.setBounds(264, 421, 86, 23);
		frame.getContentPane().add(lblExpectedtext);
		
		verificationField = new JTextField();
		verificationField.setBounds(270, 446, 90, 20);
		frame.getContentPane().add(verificationField);
		verificationField.setColumns(10);
		verificationField.setEditable(false);
		
		JScrollPane cucumberScrollPane = new JScrollPane((Component) null);
		cucumberScrollPane.setBounds(15, 524, 389, 155);
		frame.getContentPane().add(cucumberScrollPane);
		
		final JTextPane cucumberTextPane = new JTextPane();
		cucumberScrollPane.setViewportView(cucumberTextPane);

		JButton btnGenerate = new JButton("generate step");
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//new functions
				
				HashMap<String, String> command = new HashMap<String,String>();
				
				command.put("Action", commandBox.getSelectedItem().toString());
				if(pageNameField.isEditable()&&commandBox.getSelectedItem().toString().matches("(visit|on)"))
				{
					command.put("PageName", pageNameField.getText());
				}
				
				if(elementField.isEditable())
				{
					command.put("ElementName", elementField.getText());
				}

				if(verificationField.isEditable()&&commandBox.getSelectedItem().toString().matches("(fill|fillIfExist|select|Attribute|CssValue|ElementText|ElementValue|movetowindow|close_and_movebacktowindow|doesContainText|doesNotContainText)"))
				{
					command.put("ExpectedText", verificationField.getText());
				}
				
				if(Attribute_CssValue_Field.isEditable()&&commandBox.getSelectedItem().toString().matches("(Attribute|CssValue)"))
				{
					command.put("Attribute_CssValue", Attribute_CssValue_Field.getText());
				}

				command.put("AssertType", assertComboBox.getSelectedItem().toString());				
				
				String statement = new StatementMaps().getStatement(command);
				//System.out.println(statement);
				cucumberTextPane.setText(cucumberTextPane.getText()+ statement + "\n");
				
			}
		});
		btnGenerate.setBounds(372, 446, 115, 23);
		frame.getContentPane().add(btnGenerate);
		
		rdbtnNavigation = new JRadioButton("Navigation");
		rdbtnNavigation.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(rdbtnNavigation.isSelected())
				{
					commandBox.setModel(new DefaultComboBoxModel(StatementMaps.sNavigation));
					commandBox.revalidate();
					commandBox.repaint();
					//lblElementName.set
				}
				
				if(commandBox.getSelectedItem().toString().matches("(on|visit)"))
				{
					if(elementField!=null) elementField.setEditable(false);
					if(pageNameField!=null) pageNameField.setEditable(true);
					if(verificationField!=null) verificationField.setEditable(false);	
					if(Attribute_CssValue_Field!=null) Attribute_CssValue_Field.setEditable(false);
					if(assertComboBox!=null) assertComboBox.setEditable(false);	
				}

			}
		});

		rdbtnNavigation.setSelected(true);
		actionButtonGroup.add(rdbtnNavigation);
		rdbtnNavigation.setBounds(429, 514, 109, 23);
		frame.getContentPane().add(rdbtnNavigation);
		
		rdbtnElementOperation = new JRadioButton("Element Operation");
		rdbtnElementOperation.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(rdbtnElementOperation.isSelected())
				{
					commandBox.setModel(new DefaultComboBoxModel(StatementMaps.sElementOperation));
					commandBox.revalidate();
					commandBox.repaint();	
					if(commandBox.getSelectedItem().toString().matches("(fill|fillIfExist|select|Attribute|CssValue|ElementText|ElementValue|movetowindow|close_and_movebacktowindow)"))
					{
						if(elementField!=null) elementField.setEditable(false);
						if(pageNameField!=null) pageNameField.setEditable(true);
						if(verificationField!=null) verificationField.setEditable(false);	
						if(Attribute_CssValue_Field!=null) Attribute_CssValue_Field.setEditable(false);
						if(assertComboBox!=null) assertComboBox.setEditable(false);	
					}
					
					if(commandBox.getSelectedItem().toString().matches("(click|clickIfExist|mouseover)"))
					{
						if(elementField!=null) elementField.setEditable(true);
						if(pageNameField!=null) pageNameField.setEditable(false);
						if(verificationField!=null) verificationField.setEditable(false);	
						if(Attribute_CssValue_Field!=null) Attribute_CssValue_Field.setEditable(false);
						if(assertComboBox!=null) assertComboBox.setEditable(false);	
					}
	
				}

			}
		});

		actionButtonGroup.add(rdbtnElementOperation);
		rdbtnElementOperation.setBounds(429, 540, 145, 23);
		frame.getContentPane().add(rdbtnElementOperation);

		rdbtnElementVerification = new JRadioButton("Element Verification");
		rdbtnElementVerification.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(rdbtnElementVerification.isSelected())
				{
					commandBox.setModel(new DefaultComboBoxModel(StatementMaps.sElementVerification));
					commandBox.revalidate();
					commandBox.repaint();	
					if(commandBox.getSelectedItem().toString().matches("(fill|fillIfExist|select|Attribute|CssValue|ElementText|ElementValue|movetowindow|close_and_movebacktowindow)"))
					{
						verificationField.setEditable(true);	
					}
					else
					{
						verificationField.setEditable(false);
					}						

				}
			}
		});

		actionButtonGroup.add(rdbtnElementVerification);
		rdbtnElementVerification.setBounds(429, 566, 151, 23);
		frame.getContentPane().add(rdbtnElementVerification);
		
		rdbtnPageVerification = new JRadioButton("Page Verification");
		rdbtnPageVerification.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(rdbtnPageVerification.isSelected())
				{
					commandBox.setModel(new DefaultComboBoxModel(StatementMaps.sPageVerification));
					commandBox.revalidate();
					commandBox.repaint();
					
					if(commandBox.getSelectedItem().toString().matches("(doescontain|doesnotcontain)"))
					{
						elementField.setEditable(true);
						pageNameField.setEditable(false);
						verificationField.setEditable(false);	
						Attribute_CssValue_Field.setEditable(false);
						assertComboBox.setEditable(false);	
					}

				}
				
			}
		});
		actionButtonGroup.add(rdbtnPageVerification);
		rdbtnPageVerification.setBounds(429, 592, 130, 23);
		frame.getContentPane().add(rdbtnPageVerification);
		
		rdbtnBrowserActions = new JRadioButton("Browser Actions");
		actionButtonGroup.add(rdbtnBrowserActions);
		rdbtnBrowserActions.setBounds(429, 618, 130, 23);
		frame.getContentPane().add(rdbtnBrowserActions);
		rdbtnBrowserActions.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(rdbtnBrowserActions.isSelected())
				{
					commandBox.setModel(new DefaultComboBoxModel(StatementMaps.sBrowserOperation));
					commandBox.revalidate();
					commandBox.repaint();
					
					if(commandBox.getSelectedItem().toString().matches("(open_new_window)"))
					{
						elementField.setEditable(false);
						pageNameField.setEditable(false);
						verificationField.setEditable(false);	
						Attribute_CssValue_Field.setEditable(false);
						assertComboBox.setEditable(false);	
					}
				}
				
			}
		});
		
		final JComboBox customerTypeBox = new JComboBox();
		customerTypeBox.setModel(new DefaultComboBoxModel(new String[] {"Approved", "Declined", "NASDecline", "RiskCodeDecline", "NASWithKBA", "Negative Match", "Prison Address", "Deceased", "OFAC Approved", "OFAC NAS", "OFAC Failed"}));
		customerTypeBox.setBounds(609, 509, 130, 21);
		frame.getContentPane().add(customerTypeBox);
		
		final JComboBox addressTypeBox = new JComboBox();
		addressTypeBox.setModel(new DefaultComboBoxModel(new String[] {"Approved_Address", "Premises_Partial_Address", "Street_Partial_Address", "Invalid_Address", "Interaction_Required_Address", "Multiple_Address", "PO_Box_Address", "General_Delivery_Address", "Rural_Route_Address", "Highway_Contract_Route_Address"}));
		addressTypeBox.setBounds(610, 558, 145, 21);
		frame.getContentPane().add(addressTypeBox);
		
		JButton btnFill = new JButton("Fill");
		btnFill.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				
					try {
						window.frame.setVisible(false);
						if(oWebDriver!=null)
						{
							new PageElements(oWebDriver).fillCustomerInfo(oWebDriver.getCurrentUrl(), customerTypeBox.getSelectedItem().toString(), addressTypeBox.getSelectedItem().toString());
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					} finally{
						window.frame.setVisible(true);
					}
				
				
			}
		});
		btnFill.setBounds(624, 591, 71, 23);
		frame.getContentPane().add(btnFill);
		
		JLabel lblCustomerType = new JLabel("Customer Type");
		lblCustomerType.setBounds(609, 488, 94, 14);
		frame.getContentPane().add(lblCustomerType);
		
		JLabel lblAddressType = new JLabel("Address Type");
		lblAddressType.setBounds(609, 538, 94, 14);
		frame.getContentPane().add(lblAddressType);
		
		JButton btnRefreshButton = new JButton("refresh");
		btnRefreshButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				
				if(selectionNode!=null&&selectionNode.isLeaf())
				{		
					tree.setModel(treeModel);
					DefaultMutableTreeNode node = null;
					file = pageNodeMap.get(selectionNode.getParent().toString()).pageFile;
					
					PageNode pageNode = new PageNode(file);						 
					pageNodeMap.put(pageNode.pageName, pageNode);	

					node = new DefaultMutableTreeNode(pageNode.pageName);

					
					Iterator<Entry<String, String>> key = pageNode.elementsMap.entrySet().iterator();
					
					while(key.hasNext())
					{
						java.util.Map.Entry entry = (java.util.Map.Entry)key.next();
						node.add(new DefaultMutableTreeNode(entry.getKey()));
					 
					}

					treeModel.reload(node);
					tree.revalidate();
					tree.repaint();
			        //Make sure the user can see the lovely new node.
			        tree.scrollPathToVisible(new TreePath(node.getPath()));
				}
				else if(selectionNode!=null&&!selectionNode.equals(rootNode))
				{
					tree.setModel(treeModel);
					DefaultMutableTreeNode node = null;
					file = pageNodeMap.get(selectionNode.toString()).pageFile;
					
					PageNode pageNode = new PageNode(file);						 
					pageNodeMap.put(pageNode.pageName, pageNode);	

					node = new DefaultMutableTreeNode(pageNode.pageName);

					
					Iterator<Entry<String, String>> key = pageNode.elementsMap.entrySet().iterator();
					
					while(key.hasNext())
					{
						java.util.Map.Entry entry = (java.util.Map.Entry)key.next();
						node.add(new DefaultMutableTreeNode(entry.getKey()));
					 
					}

					treeModel.reload(node);
					tree.revalidate();
					tree.repaint();
			        //Make sure the user can see the lovely new node.
			        tree.scrollPathToVisible(new TreePath(node.getPath()));
				}
				

			}
		});
		btnRefreshButton.setBounds(670, 41, 86, 23);
		frame.getContentPane().add(btnRefreshButton);
		
		JButton btnAddButton = new JButton("Add");
		btnAddButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				JFileChooser fc;
				
				fc = new JFileChooser(CucumberDirectoryPath);

				fc.setVisible(true);				
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) 	
				{
					file = fc.getSelectedFile();
					CucumberDirectoryPath = file.getAbsolutePath();
				}
				
				if(file!=null&&returnVal == JFileChooser.APPROVE_OPTION)
				{
					tree.setModel(treeModel);
					DefaultMutableTreeNode node = null;
					
					PageNode pageNode;	
					pageNode = new PageNode(file);
					pageNodeMap.put(pageNode.pageName, pageNode);	
	
					node = new DefaultMutableTreeNode(pageNode.pageName);
					
					
					Iterator<Entry<String, String>> key = pageNode.elementsMap.entrySet().iterator();
					
					while(key.hasNext())
					{
						java.util.Map.Entry entry = (java.util.Map.Entry)key.next();
						node.add(new DefaultMutableTreeNode(entry.getKey()));
						//TODO for further using 
						DefaultMutableTreeNode elementnode = new DefaultMutableTreeNode();
						elementnode.setUserObject(entry.getClass());
					 
					}
					//rootNode.add(node_1);
					treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
					
					tree.revalidate();
					tree.repaint();
			        //Make sure the user can see the lovely new node.
			        tree.scrollPathToVisible(new TreePath(node.getPath()));
				}
				
			}
		});

		
		btnAddButton.setBounds(453, 41, 91, 23);
		frame.getContentPane().add(btnAddButton);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				 TreePath currentSelection = tree.getSelectionPath();
			        if (currentSelection != null) {
			        	
			            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
			                         (currentSelection.getLastPathComponent());
			            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
			            if (parent != null && parent.equals(rootNode)) {
			                treeModel.removeNodeFromParent(currentNode);
			                return;
			            }
			            else if (parent != null &&!parent.equals(rootNode))
			            {
			                treeModel.removeNodeFromParent(parent);
			                return;
			            }
			        } 

			}
		});
		btnRemove.setBounds(560, 41, 91, 23);
		frame.getContentPane().add(btnRemove);		

		
		lblAttributecssvalue = new JLabel("Attribute/CssValue");
		lblAttributecssvalue.setHorizontalAlignment(SwingConstants.CENTER);
		lblAttributecssvalue.setBounds(138, 468, 105, 23);
		frame.getContentPane().add(lblAttributecssvalue);

		Attribute_CssValue_Field = new JTextField();
		Attribute_CssValue_Field.setBounds(136, 491, 123, 22);
		Attribute_CssValue_Field.setEditable(false);
		frame.getContentPane().add(Attribute_CssValue_Field);
		Attribute_CssValue_Field.setColumns(10);
		
		lblAsserttype = new JLabel("AssertType");
		lblAsserttype.setHorizontalAlignment(SwingConstants.CENTER);
		lblAsserttype.setBounds(268, 468, 105, 23);
		frame.getContentPane().add(lblAsserttype);
		
		assertComboBox = new JComboBox();
		assertComboBox.setModel(new DefaultComboBoxModel(new String[] {"equals", "contains", "matches", "doesnotequal", "doesnotcontain", "doesnotmatch"}));
		assertComboBox.setBounds(270, 491, 113, 22);
		frame.getContentPane().add(assertComboBox);
		
		lblPagename = new JLabel("PageName");
		lblPagename.setHorizontalAlignment(SwingConstants.CENTER);
		lblPagename.setBounds(18, 468, 105, 23);
		frame.getContentPane().add(lblPagename);
		
		pageNameField = new JTextField();
		pageNameField.setColumns(10);
		pageNameField.setBounds(15, 491, 113, 22);
		frame.getContentPane().add(pageNameField);
		
		JButton btnInspect = new JButton("Inspect");
		btnInspect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnInspect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!(oWebDriver instanceof FirefoxDriver))
				{
					logTextPane.setText(logTextPane.getText() + "Inspect only works for firefox" + "\n");
					return;
				}
				
				Actions action = new Actions(oWebDriver);
				//action.sendKeys(Keys.CONTROL, Keys.SHIFT + "c").perform();		
				
				String selector = null;
				try {
					//this line to trigger firepath inspect mode					
					oWebDriver.manage().ime().deactivate();
					Utils.sleepFor(1);
					//this line to get selector once element is inspected
					selector = oWebDriver.manage().ime().getActiveEngine();
					
					//oWebDriver.getTitle();
					//selector = oWebDriver.getPageSource();
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(e.toString().contains("Please make sure firepath is open"))		
						logTextPane.setText(logTextPane.getText() + "Please make sure firepath is open" + "\n");
				}
				elementTag.setText(selector);
			}
		});
		btnInspect.setBounds(226, 61, 71, 23);
		frame.getContentPane().add(btnInspect);
		
		final JCheckBox chckbxReload = new JCheckBox("Reload");
		chckbxReload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(chckbxReload.isSelected())
				{
					Driver.Reload = true;					
				}
				else
				{
					Driver.Reload = false;						
				}
			}
		});
		chckbxReload.setBounds(325, 61, 97, 23);
		frame.getContentPane().add(chckbxReload);
		

	}
}

