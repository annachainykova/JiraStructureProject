package hillelauto;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.util.concurrent.TimeUnit;

public class WebDriverTestBase {
    protected WebDriver driver;
    //protected APIClient client;

    static{
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
    }

    @BeforeTest (alwaysRun = true)
    public void setUp() {
        driver = new ChromeDriver(new ChromeOptions().addArguments("--start-maximized", "--incognito"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        Tools.setDriver(driver);
    }

    @AfterTest(alwaysRun = true)
    public void finish() {
        driver.close();

    }

//    @BeforeTest (alwaysRun = true)
//    public void setUpTestRail() {
//        client = new APIClient(JiraVars.URLTestRail);
//        client.setUser(JiraVars.usernameTestRail);
//        client.setPassword(JiraVars.passwordTestRail);
//    }
}
