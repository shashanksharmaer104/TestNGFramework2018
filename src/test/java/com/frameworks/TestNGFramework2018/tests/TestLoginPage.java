package com.frameworks.TestNGFramework2018.tests;

import org.testng.annotations.Test;

import com.frameworks.TestNGFramework2018.base.BaseTest;
import com.frameworks.TestNGFramework2018.logs.Log;

public class TestLoginPage extends BaseTest {

	@Test
	public void testLogin() {
		driver.get("http://www.google.com");
		
		Log.info("Test ..................");
		
		//test.log(Status.PASS, MarkupHelper.createLabel("Test Case Passed is passTest", ExtentColor.GREEN));
	}
	
}
