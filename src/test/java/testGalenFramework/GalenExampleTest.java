package testGalenFramework;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;

public class GalenExampleTest {

	private WebDriver driver;

    @BeforeTest
    public void setUp()
    {
    	String chromePath = System.getProperty("user.dir") + "//drivers//chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromePath);
        driver = new ChromeDriver();
        //Set the browser size for desktop
        driver.manage().window().setSize(new Dimension(1200, 800));
        //Go to swtestacademy.com
        driver.get("https://www.swtestacademy.com/");
        
    }
    
    @Test
    public void homePageLayoutTest()
    {
    	try {
    		Random rand = new Random(); 
    		int value = rand.nextInt(50);
    		//Create a layoutReport object
            //checkLayout function checks the layout and returns a LayoutReport object
            LayoutReport layoutReport = Galen.checkLayout(driver, System.getProperty("user.dir") + "\\homepage.gspec", Arrays.asList("desktop"));
     
            //Create a tests list
            List<GalenTestInfo> tests = new LinkedList<GalenTestInfo>();
     
            //Create a GalenTestInfo object
            GalenTestInfo test = GalenTestInfo.fromString(value + " homepage layout");
     
            //Get layoutReport and assign to test object
            test.getReport().layout(layoutReport, "check homepage layout");
     
            //Add test object to the tests list
            tests.add(test);
     
            //Create a htmlReportBuilder object
            HtmlReportBuilder htmlReportBuilder = new HtmlReportBuilder();
     
            //Create a report under /target folder based on tests list
            htmlReportBuilder.build(tests, "target");
     
            //If layoutReport has errors Assert Fail
            if (layoutReport.errors() > 0)
            {
                Assert.fail("Layout test failed");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


    @AfterTest
    public void tearDown()
    {
        driver.quit();
    }
}
