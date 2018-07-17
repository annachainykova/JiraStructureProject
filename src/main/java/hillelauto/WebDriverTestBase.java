package hillelauto;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WebDriverTestBase {
    protected WebDriver driver;
    private boolean localDriver = false;
    private String remoteAddress = "http://192.168.229.2:9515";

    //protected APIClient client;

    static{
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
    }

    @BeforeTest (alwaysRun = true)
    public void setUp() throws MalformedURLException {
        driver = localDriver ? new ChromeDriver(new ChromeOptions().addArguments("--start-maximized", "--incognito"))
        : new RemoteWebDriver(new URL(remoteAddress), DesiredCapabilities.chrome());
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
