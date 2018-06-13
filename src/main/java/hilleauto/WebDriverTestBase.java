package hilleauto;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.util.concurrent.TimeUnit;

public class WebDriverTestBase {
    protected WebDriver driver;

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

}
