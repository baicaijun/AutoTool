package com.browser.debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class PageNode {
	
	public File pageFile;	
	public String pageName;
	public String url;
	public HashMap<String,String> elementsMap = new LinkedHashMap<String,String>();
	public ArrayList<ElementNode> _elements = new ArrayList<ElementNode>();
	//public ArrayList<ElementNode> elements = new ArrayList<ElementNode>();
	
	public PageNode(File file)
	{
		//Constructor
		init(file);
	}
	

	
	public void init(File file)
	{
		this.pageFile = file;
		try {
			getElementsMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPageName()
	{
		return this.pageName;
	}
	
	public HashMap<String, String> getElementsMap() throws IOException{
		
		FileReader fr = null;
		try {
			fr = new FileReader(pageFile);
			//fw = new FileWriter("C:/out/tempPage.txt");  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        BufferedReader bufr = new BufferedReader(fr);   
        
        String sElementName = null;
        String sElementTag = null;
       
        String sline = "";
        String[] line = null;

        
        while(((sline!=null&&sline.contains("addElement")&&!sline.trim().startsWith("#"))||(sline=bufr.readLine()) != null))
        {
			if(sline.contains("https:")&&!sline.trim().startsWith("#"))
			{
				line = sline.split("=");
				pageName = line[0].trim();
				url = line[1].split("\"")[1].trim();
			}
        	
        	if(sline.contains("addElement")&&!sline.trim().startsWith("#"))
        	{
        		
	        	StringBuilder strBuilder = new StringBuilder(sline.replace("\n", "").trim());
        	
	        	
	            while((sline=bufr.readLine()) != null&&!sline.trim().startsWith("#")&&!sline.contains("addElement"))
	            {
	            	strBuilder.append(sline.replace("\n", "").trim());
                }
	            
	            
	            String elementline = strBuilder.toString();
	            if(elementline.contains(":desktop"))
				{
					//get Element Name
					line = elementline.split("\"");
					sElementName = line[1];
					
					//to get Element Locator
					line = elementline.split("GdElement.new");					
					
					sElementTag = "";					
					sElementTag = line[1].split(":desktop")[1].split("\"")[1].trim();
					
//					if(elementline.contains("desktopcss"))
//						sElementTag = "css=" + sElementTag;
//					else
//						sElementTag = "xpath=" + sElementTag;
					
					ElementNode node = new ElementNode(sElementName, sElementTag);
					
					elementsMap.put(sElementName, sElementTag);
				}
        	}
        	
        }        

        bufr.close();
        fr.close();
		return elementsMap;        
        
	}

	
	public ArrayList<ElementNode> getElements() throws IOException{
		
		FileReader fr = null;
		try {
			fr = new FileReader(pageFile);
			//fw = new FileWriter("C:/out/tempPage.txt");  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        BufferedReader bufr = new BufferedReader(fr);   
      
        ElementNode element = null;
       
        String sline = "";
        String[] line = null;

        
        while(((sline!=null&&sline.contains("addElement")&&!sline.trim().startsWith("#"))||(sline=bufr.readLine()) != null))
        {
			if(sline.contains("https:")&&!sline.trim().startsWith("#"))
			{
				line = sline.split("=");
				pageName = line[0].trim();
				url = line[1].split("\"")[1].trim();
			}
        	
        	if(sline.contains("addElement")&&!sline.trim().startsWith("#"))
        	{
        		
	        	StringBuilder strBuilder = new StringBuilder(sline.replace("\n", "").trim());
        	
	        	
	            while((sline=bufr.readLine()) != null&&!sline.trim().startsWith("#")&&!sline.contains("addElement"))
	            {
	            	strBuilder.append(sline.replace("\n", "").trim());
                }
	            
	            
	            String elementline = strBuilder.toString();
	            if(elementline.contains(":desktop"))
				{
					//get Element Name
					line = elementline.split("\"");
					element = new ElementNode();
					element.setName(line[1]);
					
					//to get Element Locator
					line = elementline.split("GdElement.new");				
					element.setSelector(line[1].split(":desktop")[1].split("\"")[1].trim());	
										
					//elements.add(element);
					_elements.add(element);
					
				}
        	}
        	
        }        

        bufr.close();
        fr.close();
		return _elements;        
        
	}
	
	class MyTreeModelListener implements TreeModelListener {
	    public void treeNodesChanged(TreeModelEvent e) {
	        DefaultMutableTreeNode node;
	        node = (DefaultMutableTreeNode)
	                 (e.getTreePath().getLastPathComponent());

	        /*
	         * If the event lists children, then the changed
	         * node is the child of the node we have already
	         * gotten.  Otherwise, the changed node and the
	         * specified node are the same.
	         */
	        try {
	            int index = e.getChildIndices()[0];
	            node = (DefaultMutableTreeNode)
	                   (node.getChildAt(index));
	        } catch (NullPointerException exc) {}

	        System.out.println("The user has finished editing the node.");
	        System.out.println("New value: " + node.getUserObject());
	    }
	    public void treeNodesInserted(TreeModelEvent e) {
	    }
	    public void treeNodesRemoved(TreeModelEvent e) {
	    }
	    public void treeStructureChanged(TreeModelEvent e) {
	    }
	}
}
