package com.github.gtxtreme21;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.commons.lang.StringUtils;

public class JTLParser {
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	
	public String getTestResult(File jtlFile) {
		StringBuilder resultSB = new StringBuilder();
		DocumentBuilder dBuilder;
		String result = "";
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(jtlFile);
			// normalize text representation
            doc.getDocumentElement().normalize();
			NodeList httpSampleList = doc.getElementsByTagName("httpSample");
//			System.out.println("Total no of tests for file: " + jtlFile.getName() + ": " + httpSampleList.getLength());

			for (int temp = 0; temp < httpSampleList.getLength(); temp++) {	

				Node httpSampleNode = httpSampleList.item(temp);
				if (httpSampleNode != null && "httpSample".equals(httpSampleNode.getNodeName()) 
						&& httpSampleNode.getNodeType() == Node.ELEMENT_NODE) {
					String testName = "";
//					System.out.println(httpSampleNode.getNodeName());
					Node lb = httpSampleNode.getAttributes().getNamedItem("lb");
					if (null != lb) {
						testName = lb.getNodeValue();
					}
				
					NodeList assertionResultNodeList = ((Element) httpSampleNode).getElementsByTagName("assertionResult");
					Node assertionResultNode = null;
					if (null != assertionResultNodeList && 0 < assertionResultNodeList.getLength()) {
						assertionResultNode = assertionResultNodeList.item(0);
						
						if (null != assertionResultNode && "assertionResult".equals(assertionResultNode.getNodeName()) 
								&& assertionResultNode.hasChildNodes()) {
							Element assertionResultElement = (Element)assertionResultNode;
							if (null != assertionResultElement) {
								NodeList failureMessageList = assertionResultElement.getElementsByTagName("failureMessage");
			                    Element failureMessageElement = (Element)failureMessageList.item(0);
			                    if (null != failureMessageElement) {
				                    String failureMsg = failureMessageElement.getNodeValue();
				                    failureMsg = StringUtils.isBlank(failureMsg)? failureMessageElement.getTextContent(): failureMsg;  
//				                    System.out.println("failureMessageElement: " + failureMsg);
									if (!StringUtils.isBlank(failureMsg)) {
										result = jtlFile.getName() + ":" + testName + "<b><font size='3' color='red'> (failed): </font></b>" + failureMsg;
									} else {
										result = jtlFile.getName() + ":" + testName + "<b><font size='3' color='green'> (passed)</font></b>";
									}
									
									if (!StringUtils.isBlank(result)) {
										resultSB.append("<li>"+result + "</li>\r\n");
									}
			                    }
							}
						}
					}
				}
//				if (!StringUtils.isBlank(resultSB.toString())) {
//					System.out.println("Result = "+resultSB.toString());
//				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result += resultSB.toString();
		return result;
	}
}
