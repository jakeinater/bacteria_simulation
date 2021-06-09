package io.xml;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.io.File;
import java.io.IOException;

import core.*;
import junctions.*;
import utils.*;


public class WriteXML {

	public static void write(Graph g, String filename) {
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			//root element
			Element rootElement = doc.createElement("graphml");
			rootElement.appendChild(rootElement);
			
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
			
			Attr d2 = doc.createAttribute("id");
			d2.setNodeValue("d2");
			key1.setAttributeNode(d2);
			
			Attr forEdge = doc.createAttribute("for");
			forEdge.setNodeValue("edge");
			key1.setAttributeNode(forEdge);

			Attr nameWeight = doc.createAttribute("attr.name");
			nameWeight.setNodeValue("weight");
			key1.setAttributeNode(nameWeight);

			Attr typeDouble = doc.createAttribute("attr.type");
			typeDouble.setNodeValue("double");
			key1.setAttributeNode(typeDouble);

			//Y
			Element key2 = doc.createElement("key");
			rootElement.appendChild(key2);
			
			Attr d1 = doc.createAttribute("id");
			d1.setNodeValue("d1");
			key2.setAttributeNode(d1);
			
			Attr forNode = doc.createAttribute("for");
			forNode.setNodeValue("node");
			key2.setAttributeNode(forNode);

			Attr nameY = doc.createAttribute("attr.name");
			nameY.setNodeValue("y");
			key2.setAttributeNode(nameY);

			key2.setAttributeNode(typeDouble);

			//X
			Element key3 = doc.createElement("key");
			rootElement.appendChild(key3);
			
			Attr d0 = doc.createAttribute("id");
			d0.setNodeValue("d0");
			key3.setAttributeNode(d0);
			
			key3.setAttributeNode(forNode);

			Attr nameX = doc.createAttribute("attr.name");
			nameX.setNodeValue("x");
			key3.setAttributeNode(nameX);

			key3.setAttributeNode(typeDouble);

			//graph element
			Element graph = doc.createElement("graph");
			rootElement.appendChild(graph);
			Attr edgedefault = doc.createAttribute("edgedefault");
			edgedefault.setNodeValue("directed");
			graph.setAttributeNode(edgedefault);

			
			//nodes and edges
			Junction[] arr = g.getJunctions();
			Junction cur;
			
			for (int i = 0; i < arr.length; i++) 
			{
				cur = arr[i];
				
				Element node = doc.createElement("node");
				graph.appendChild(node);
				Attr id = doc.createAttribute("id");
				id.setNodeValue(Integer.toString(cur.getID()));
				node.setAttributeNode(id);
				
				Element dataX = doc.createElement("data");
				Element dataY = doc.createElement("data");
				node.appendChild(dataX);
				node.appendChild(dataY);
				dataX.setAttributeNode(d0);
				dataY.setAttributeNode(d1);
				dataX.appendChild(doc.createTextNode(Double.toString(cur.getX())));
				dataY.appendChild(doc.createTextNode(Double.toString(cur.getY())));
		
				//method that appends the edge XML elements to graph element
				cur.addEdgesXML(doc, graph, d2);
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
