package de.itdesign.application;

//importing all dependent libraries
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import org.w3c.dom.Attr;
import javax.xml.transform.OutputKeys;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

public class XMLCalculator {
	
	public static void findSolution(Element eElement , String DATA_FILE, String OUTPUT_FILE  ) {
		
		// local variable declaration and initialization
		String nameOperation = eElement.getAttribute("name");
		String attribOperation = eElement.getAttribute("attrib");
		String filterOperation = eElement.getAttribute("filter"); 
		double totalPopulation=0;
		int totalCount=0;
		double totalArea=0;
		// using decimal format to get two digits precision
		DecimalFormat df=new DecimalFormat("#.00");
		
		// Creating a list to store all matches when the name attribute is "for"
        List<Double> arrList = new ArrayList<Double>();
        
        // Creating a list to store all matches when the name attribute is "future"
        List<Double> arrListFuture = new ArrayList<Double>();
		try {
			//***************************************************** Reading the data.xml************************************************
			
			//creating a constructor of file class and parsing an XML file  
			File file = new File(DATA_FILE);
			
			//an instance of factory that gives a document builder  
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    
		    // creating an instance of builder to parse the specified xml file  
		    DocumentBuilder db = dbf.newDocumentBuilder();  
		    Document doc = db.parse(file);
		    
		    // to ensure no empty nodes are present we normalize
		    doc.getDocumentElement().normalize();
		    NodeList nodeList = doc.getElementsByTagName("city");  
		    
		    // Iterating over all nodes
		    for (int itr = 0; itr < nodeList.getLength(); itr++) {
		    	Node node = nodeList.item(itr);
		    	if (node.getNodeType() == Node.ELEMENT_NODE) {
		    		Element dElement = (Element) node;  
		    		String nameAttribute = dElement.getAttribute("name");
		    		
		    		
		    		if(nameOperation.equals("important")) {
		    			
		    			if(nameAttribute.matches(filterOperation)) {
		    				//performing average calculation
		    				double populationAttribute = Double.parseDouble(dElement.getAttribute(attribOperation));
		    				totalPopulation= totalPopulation+populationAttribute;
		    				totalCount++;
		    				
		    			}
		    		}
		    		
		    		else if(nameOperation.equals("information")) {
		    			if(nameAttribute.matches(filterOperation)) {
		    				//performing sum 
		    				double areaAttribute = Double.parseDouble(dElement.getElementsByTagName(attribOperation).item(0).getTextContent());
		    				totalArea = totalArea+areaAttribute;
		    				
		    			}
		    			
		    			
		    		}
		    		
		    		else if(nameOperation.equals("for")) {
		    			if(nameAttribute.matches(filterOperation)) {
		    				//finding min
		    				arrList.add(Double.parseDouble(dElement.getElementsByTagName(attribOperation).item(0).getTextContent()));
		    				
		    			}
		    			
		    		}
		    		
		    		else if(nameOperation.equals("future")) {
		    			if(nameAttribute.matches(filterOperation)) {
		    				//finding max
		    				arrListFuture.add(Double.parseDouble(dElement.getAttribute(attribOperation)));
		    				
		    			}
		    			
		    		}
		    		
		    		
		    		
		    	}
		    }
		    //************************************** Writing the results to the output.xml**********************************************
		    
		    // Invoking a constructor of file class and parsing an XML file
		    File xmlFile = new File(OUTPUT_FILE);
		    
		    // creatíng an instance of factory that gives a document builder 
		    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		    
		    // creating an instance of builder to parse the specified xml file  
		    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		    Document document = documentBuilder.parse(xmlFile); 
		    Element documentElement = document.getDocumentElement();
		    
		    if(nameOperation.equals("important")) {
		    	Attr nameAttribute = document.createAttribute("name");
		    	nameAttribute.setValue(nameOperation);
		    	
		    	Element nodeElement = document.createElement("result");
				nodeElement.setAttributeNode(nameAttribute);
				df.setRoundingMode(RoundingMode.UP);
				nodeElement.appendChild(document.createTextNode(String.valueOf(df.format(totalPopulation/totalCount))));
				documentElement.appendChild(nodeElement);
		    }
		    
		    else if(nameOperation.equals("information")) {
		    	Attr nameAttribute = document.createAttribute("name");
		    	nameAttribute.setValue(nameOperation);
		    	
		    	Element nodeElement = document.createElement("result");
				nodeElement.setAttributeNode(nameAttribute);
				df.setRoundingMode(RoundingMode.DOWN);
				nodeElement.appendChild(document.createTextNode(String.valueOf(df.format(totalArea))));
				documentElement.appendChild(nodeElement);
		    }
		    
		    else if(nameOperation.equals("for")) {
		    	Attr nameAttribute = document.createAttribute("name");
		    	nameAttribute.setValue(nameOperation);
		    	
		    	Element nodeElement = document.createElement("result");
				nodeElement.setAttributeNode(nameAttribute);
				
				// finding minimum value in list
				Double minValue=arrList.stream().reduce(Double::min).get();								
				df.setRoundingMode(RoundingMode.DOWN);
				nodeElement.appendChild(document.createTextNode(String.valueOf(df.format(minValue))));
				documentElement.appendChild(nodeElement);
		    }
		    
		    else if(nameOperation.equals("future")) {
		    	Attr nameAttribute = document.createAttribute("name");
		    	nameAttribute.setValue(nameOperation);
		    	
		    	Element nodeElement = document.createElement("result");
				nodeElement.setAttributeNode(nameAttribute);
				
				//finding maximumvalue in list
				Double maxValue=arrListFuture.stream().reduce(Double::max).get();								
				df.setRoundingMode(RoundingMode.CEILING);
				nodeElement.appendChild(document.createTextNode(String.valueOf(df.format(maxValue))));
				documentElement.appendChild(nodeElement);
		    }
		    
		    
			//document.replaceChild(documentElement, documentElement);
			Transformer tFormer =TransformerFactory.newInstance().newTransformer();
			//to omit the xml declaration
			tFormer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			//for indentation in the generated xml file
			tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
			tFormer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(xmlFile);
			tFormer.transform(source, result);
		    
		    
		   
		}
		catch (Exception e) {
			e.printStackTrace(); 
		}
		
	}

    public static void main(String[] args) {
        //Don't change this part
        if (args.length == 3) {
            //Path to the data file, e.g. data/data.xml
            final String DATA_FILE = args[0];
           
            //Path to the data file, e.g. operations/operations.xml
            final String OPERATIONS_FILE = args[1];
           
            //Path to the output file solution/output.xml
            final String OUTPUT_FILE = args[2];
         
            
            try {
            	
            	//****************************** Creating an output.xml file containing root element*******************************************
            	
            	// creatíng an instance of factory that gives a document builder 
            	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            	
            	// creating an instance of builder  
            	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
            	Document document = dBuilder.newDocument();
            	
            	// inserting root element
		        Element rootElement = document.createElement("results");
		        document.appendChild(rootElement);
		        
		        // write the content into xml file
		        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        DOMSource source = new DOMSource(document);
		        StreamResult solution = new StreamResult(new File(OUTPUT_FILE));
		        transformer.transform(source, solution);
            	
            	
            	//************************************ Reading the operations.xml file **************************************************************
            	
		        // Invoking a constructor of file class and parsing an XML file
                File file = new File(OPERATIONS_FILE);
                
                // creatíng an instance of factory that gives a document builder 
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
                
                // creating an instance of builder to parse the specified xml file  
                DocumentBuilder db = dbf.newDocumentBuilder(); 
                Document doc = db.parse(file);  
                
                // to ensure no empty nodes are present we normalize
                doc.getDocumentElement().normalize();  
                
                // nodeList contains all the nodes with name operation
                NodeList nodeList = doc.getElementsByTagName("operation");  
                
                // Iterating over all nodes
                for (int itr = 0; itr < nodeList.getLength(); itr++) {
                	Node node = nodeList.item(itr);
                	if (node.getNodeType() == Node.ELEMENT_NODE) {
                		Element eElement = (Element) node;  
                		//calling the function 
                		findSolution(eElement, DATA_FILE, OUTPUT_FILE);
                	}
                	                	
                }
                
            	
            }
            
            // Catches the raised exception, for example when we try to access the element with tagname that is not present in xml file
            catch (Exception e) {
            	e.printStackTrace();  
            }
            
            
        }
        else {
        	
            System.exit(1);
        }

    }

}
