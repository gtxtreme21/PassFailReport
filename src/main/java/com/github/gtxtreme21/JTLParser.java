package com.github.gtxtreme21;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JTLParser {
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	
	public TestResult getTestResult(File jtlFile) {
//		StringBuilder resultSB = new StringBuilder();
		DocumentBuilder dBuilder;

//		String result = "";
		TestResult testResult = new TestResult(jtlFile.getName());
		try {
			
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(jtlFile);
			// normalize text representation
            doc.getDocumentElement().normalize();
//			NodeList httpSampleList = doc.getElementsByTagName("httpSample");
//			System.out.println("Total no of tests for file: " + jtlFile.getName() + ": " + httpSampleList.getLength());
            NodeList sampleList = doc.getElementsByTagName("testResults").item(0).getChildNodes();

			for (int temp = 0; temp < sampleList.getLength(); temp++) {	

				Node httpSampleNode = sampleList.item(temp);
//				if (httpSampleNode != null && "httpSample".equals(httpSampleNode.getNodeName()) 
//						&& httpSampleNode.getNodeType() == Node.ELEMENT_NODE) {
				if (httpSampleNode != null && httpSampleNode.getNodeName() != null
						&& httpSampleNode.getNodeName().toLowerCase().contains("sample")
						&& httpSampleNode.getNodeType() == Node.ELEMENT_NODE){
					
					addSamplerResult(testResult, httpSampleNode);
					
//					String testName = "";
////					System.out.println(httpSampleNode.getNodeName());
//					Node lb = httpSampleNode.getAttributes().getNamedItem("lb");
//					if (null != lb) {
//						testName = lb.getNodeValue();
//					}
//					
//					Node 
//				
//					NodeList assertionResultNodeList = ((Element) httpSampleNode).getElementsByTagName("assertionResult");
//					Node assertionResultNode = null;
//					if (null != assertionResultNodeList && 0 < assertionResultNodeList.getLength()) {
//						assertionResultNode = assertionResultNodeList.item(0);
//						
//						if (null != assertionResultNode && "assertionResult".equals(assertionResultNode.getNodeName()) 
//								&& assertionResultNode.hasChildNodes()) {
//							Element assertionResultElement = (Element)assertionResultNode;
//							if (null != assertionResultElement) {
//								NodeList failureMessageList = assertionResultElement.getElementsByTagName("failureMessage");
//			                    Element failureMessageElement = (Element)failureMessageList.item(0);
//			                    if (null != failureMessageElement) {
//				                    String failureMsg = failureMessageElement.getNodeValue();
//				                    failureMsg = StringUtils.isBlank(failureMsg)? failureMessageElement.getTextContent(): failureMsg;  
////				                    System.out.println("failureMessageElement: " + failureMsg);
//									if (!StringUtils.isBlank(failureMsg)) {
//										result = jtlFile.getName() + ":" + testName + "<b><font size='3' color='red'> (failed): </font></b>" + failureMsg;
//									} else {
//										result = jtlFile.getName() + ":" + testName + "<b><font size='3' color='green'> (passed)</font></b>";
//									}
//									
//									if (!StringUtils.isBlank(result)) {
//										resultSB.append("<li>"+result + "</li>\r\n");
//									}
//			                    }
//							}
//						}
//					}
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
//		result += resultSB.toString();
		return testResult;
	}
	
	private void addSamplerResult(TestResult testResult, Node samplerNode){
		NamedNodeMap attributes = samplerNode.getAttributes();
		
		Node lb = attributes.getNamedItem("lb");
		String samplerName = "";
		if (null != lb) {
			samplerName = lb.getNodeValue();
		}
		
		Node t = attributes.getNamedItem("t");
		long time = 0L;
		if(null != t && t.getNodeValue() != null && t.getNodeValue().matches("[0-9]+")){
			time = Long.valueOf(t.getNodeValue());
		}
		
		Node s = attributes.getNamedItem("s");
		boolean success = false;
		List<String> failureAssertions = new ArrayList<String>();
		if(null != s && s.getNodeValue() != null && s.getNodeValue().toUpperCase().equals("TRUE")){
			success = true;
		}
		else{
			NodeList childNodes = samplerNode.getChildNodes();
			for(int i = 0; i < childNodes.getLength(); i++){
				Node node = childNodes.item(i);
				if(node != null && node.getNodeType() == Node.ELEMENT_NODE
						&& "assertionResult".equals(node.getNodeName())){
					
					failureAssertions.add(createAssertionString((Element)node));
					
				}
			}
		}
		
		testResult.addSamplerResult(samplerName, time, success, null, failureAssertions);
	}
	
	private String createAssertionString(Element assertionElement){
		StringBuilder sb = new StringBuilder();
		
		boolean failed = false;
		
		NodeList name = assertionElement.getElementsByTagName("name");
		if(name != null && name.getLength() > 0){
			sb.append(name.item(0).getTextContent());
		}
		NodeList failure = assertionElement.getElementsByTagName("failure");
		if(failure != null && failure.getLength() > 0 && "true".equalsIgnoreCase(failure.item(0).getTextContent())){
			failed = true;
		}
		NodeList error = assertionElement.getElementsByTagName("error");
		if(error != null && error.getLength() > 0 && "true".equalsIgnoreCase(error.item(0).getTextContent())){
			failed = true;
		}
		
		if(!failed){
			sb.append(" (passed)");
		}
		else{
			sb.append(" (failed)");
			
			NodeList failureMessage = assertionElement.getElementsByTagName("failureMessage");
			if(failureMessage != null && failureMessage.getLength() > 0){
				String str = failureMessage.item(0).getTextContent();
				if(str != null && str.trim().length() > 0){
					sb.append(" - ").append(str);
				}
			}
		}
		
		return sb.toString();
	}
}
