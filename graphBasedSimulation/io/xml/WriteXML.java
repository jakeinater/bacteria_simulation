package io.xml;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.io.File;

import core.*;
import junctions.*;


public class WriteXML {

	public static void write(Graph g, String filename) {
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			//root element
			Element rootElement = doc.createElement("graphml");
			doc.appendChild(rootElement);
			
			Attr xmlns = doc.createAttribute("xmlns");
			xmlns.setValue("http://graphml.graphdrawing.org/xmlns");
			rootElement.setAttributeNode(xmlns);
			
			Attr xsi = doc.createAttribute("xmlns:xsi");
			xsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
			rootElement.setAttributeNode(xsi);

			Attr schemaLocation = doc.createAttribute("xsi:schemaLocation");
			schemaLocation.setValue("http://graphml.graphdrawing.org/xmlns\r\n" + 
					"     http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");
			rootElement.setAttributeNode(schemaLocation);
		
			//set key elements
			Element key1 = doc.createElement("key");
			rootElement.appendChild(key1);
			
			key1.setAttribute("id", "d2");
			key1.setAttribute("for", "edge");
			key1.setAttribute("attr.name", "weight");
			key1.setAttribute("attr.type", "double");

			//Y
			Element key2 = doc.createElement("key");
			rootElement.appendChild(key2);
	
			key2.setAttribute("id", "d1");
			key2.setAttribute("for", "node");
			key2.setAttribute("attr.name", "y");
			key2.setAttribute("attr.type", "double");

			//X
			Element key3 = doc.createElement("key");
			rootElement.appendChild(key3);

			key3.setAttribute("id", "d0");
			key3.setAttribute("for", "node");
			key3.setAttribute("attr.name", "y");
			key3.setAttribute("attr.type", "double");
		
			//graph element
			Element graph = doc.createElement("graph");
			rootElement.appendChild(graph);
			graph.setAttribute("edgedefault", "directed");
			
			//nodes and edges
			Junction[] arr = g.getJunctions();
			Junction cur;
			
			for (int i = 0; i < arr.length; i++) 
			{
				cur = arr[i];
				
				Element node = doc.createElement("node");
				graph.appendChild(node);
				node.setAttribute("id", Integer.toString(cur.getID()));
				
				Element dataX = doc.createElement("data");
				Element dataY = doc.createElement("data");
				node.appendChild(dataX);
				node.appendChild(dataY);
				dataX.setAttribute("key", "d0");
				dataY.setAttribute("key", "d1");
				dataX.appendChild(doc.createTextNode(Double.toString(cur.getX())));
				dataY.appendChild(doc.createTextNode(Double.toString(cur.getY())));
		
				//method that appends the edge XML elements to graph element
				cur.addEdgesXML(doc, graph, "d1");
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("./out/" + filename + ".xml"));
			transformer.transform(source, result);
		
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
		
		} catch (TransformerException te) {
			System.out.println(te.getMessage());
		} catch (ParserConfigurationException pce) {
			System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		}
	}
	
}
