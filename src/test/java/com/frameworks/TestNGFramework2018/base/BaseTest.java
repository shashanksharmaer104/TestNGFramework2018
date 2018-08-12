package com.frameworks.TestNGFramework2018.base;

import java.io.File;
import java.lang.reflect.Method;
import java.util.UUID;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.KlovReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.frameworks.TestNGFramework2018.logs.Log;

public class BaseTest {

	public static WebDriver driver = null;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	private static KlovReporter klov;
	private String driversPath = System.getProperty("user.dir") + "//drivers//";
	private String uuid = null;
	private File file = null;
	
	@BeforeSuite
	public void beforeSuite() {
		file = this.createExtentFolderDir();
		System.out.println("----------Suite Started------------");
		DOMConfigurator.configure("log4j.xml");
		uuid = UUID.randomUUID().toString().replace("-", "");
		
	}

	@BeforeClass
	public void beforeClass() {
		Log.startLog(this.getClass().getName());
		System.out.println("----------Class Started------------ " + this.getClass().getName());
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		System.out.println("----------Before Method------------ ");
		System.out.println(method.getName());
		this.setUpExtentReport();
		test = extent.createTest(method.getName());
	}

	@BeforeTest
	@Parameters({ "browser" })
	public void beforeTest(String browser) {
		this.openBrowser(browser);
	}

	@AfterTest
	public void afterTest() {
		this.killBrowser();
		extent.flush();
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		try {
			System.out.println("----------After Method------------ ");
			
			if(result.getStatus() == ITestResult.FAILURE)
			{
				test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" Test case FAILED due to below issues:", ExtentColor.RED));
				test.fail(result.getThrowable());
			}
			else if(result.getStatus() == ITestResult.SUCCESS)
			{
				test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
			}
			else
			{
				test.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" Test Case SKIPPED", ExtentColor.ORANGE));
				test.skip(result.getThrowable());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void afterClass() {
		Log.endLog(this.getClass().getName());
		System.out.println("----------Class Ended------------ " + this.getClass().getName());
	}

	@AfterSuite
	public void afterSuite() {
		System.out.println("----------Suite Ended------------");
	}

	public void openBrowser(String browser) {
		try {
			String chromePath = driversPath + "chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", chromePath);
	        driver = new ChromeDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUpExtentReport() {
		htmlReporter = new ExtentHtmlReporter(file + "//" + uuid +".html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        
        extent.setSystemInfo("OS", "Win 10");
        extent.setSystemInfo("Host Name", "Shashank");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User Name", "Shashank Sharma");
        
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("AutomationTesting Title");
        htmlReporter.config().setReportName("AutomationTesting Name");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.DARK);
        
        klov = new KlovReporter();
        klov.initMongoDbConnection("localhost", 27017);
        klov.setProjectName("TestNG Framework");
        klov.setReportName("Shashank Test");
        klov.setKlovUrl("http://localhost");
        extent.attachReporter(htmlReporter, klov);
	}
	
	public void killBrowser() {
		try {
	        driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getTodayDate() {
		return java.time.LocalDate.now().toString();
	}
	
	public File createExtentFolderDir() {
		File file = new File(System.getProperty("user.dir") 
				+ "/extent-reports/" + this.getTodayDate());
		System.out.println(file);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.err.println("Failed to create directory!");
            }
        }

       /* File files = new File("C:\\Directory2\\Sub2\\Sub-Sub2");
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Multiple directories are created!");
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }*/
        return file;
	}
}
