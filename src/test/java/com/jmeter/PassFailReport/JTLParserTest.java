package com.jmeter.PassFailReport;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jmeter.PassFailReport.JTLParser;

public class JTLParserTest {
	private static final String failMsg = "This is a failure test";
	private JTLParser parser = new JTLParser();
	private File jtlFile = null;

	@Before
    public void setup() throws IOException {
		jtlFile = getFile();		
	}
	
	@After
	public void tearDown() throws Exception {
		if (jtlFile != null) {
			jtlFile = null;
		}
	}
	
	@Test
	public void test() {
		String testResult = parser.getTestResult(jtlFile);
		assertNotNull(testResult);
		assertTrue(testResult.contains(failMsg));
		//fail("Not yet implemented");
	}

	private File getFile() {
		URL url = this.getClass().getResource("/jtlFile.jtl");
		return new File(url.getFile());
	}

}
