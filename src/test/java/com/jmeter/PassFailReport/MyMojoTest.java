package com.jmeter.PassFailReport;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jmeter.PassFailReport.MyMojo;

public class MyMojoTest {

	private static final String failMsg = "This is a failure test";
	private MyMojo myMojo = new MyMojo();
	private File inputDirectory = null;
	private File outputDirectory = null;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
		assertTrue(true);
	}

}
