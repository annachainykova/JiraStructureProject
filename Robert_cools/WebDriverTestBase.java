package hillelauto.Robert_cools;

import hillelauto.Robert_cools.Configs.ChromeConfigs;
import hillelauto.Robert_cools.Utils.Listeners.Retry;
import hillelauto.Robert_cools.Utils.MappingReports;
import hillelauto.Robert_cools.Utils.compareScreenshots.ScreenshotsTools;
import hillelauto.Robert_cools.Utils.connectionToSkyfence.SkyfenceConnectionSettings;
import hillelauto.Robert_cools.Utils.connectionToSkyfence.SkyfenceSshVerification;
import hillelauto.Robert_cools.Utils.helpers.EnvironmentHelper;
import hillelauto.Robert_cools.Utils.helpers.Tools;
import hillelauto.Robert_cools.Utils.sendMail.SendAttachmentInEmail;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static hillelauto.Robert_cools.Utils.helpers.Tools.discardAllert;


public abstract class WebDriverTestBase {

    protected static WebDriver driver;
    private boolean localDriver = true;
    private String remoteAddress = "http://192.168.3.249:9515"; //chromedriver --whitelisted-ips='192.168.3.164'

    private void initChromeDriver() throws MalformedURLException {
        driver = localDriver ? new ChromeDriver(ChromeConfigs.createChromeOptions())
                : new RemoteWebDriver(new URL(remoteAddress), DesiredCapabilities.chrome());
    }

    @BeforeTest(alwaysRun = true)
    @Parameters(value = "SkyfenceName")
    public void setUp(@Optional("elena") String skyfenceName) throws MalformedURLException {
        initChromeDriver();

        driver = ChromeConfigs.setDriverConfigs(driver);
        Tools.setDriver(driver);
        SkyfenceConnectionSettings.setUpSkyfenceConnection(skyfenceName);
        EnvironmentHelper.setPathsForCurrentOS(localDriver);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }

    @BeforeMethod(groups = {"Services"})
    public void startLogging(Method m) {
        String testDescription = m.getAnnotation(Test.class).description();
        // Checks if test method is complete and has info for activity validation like mapping IDs
        if (testDescription.contains("|"))
            SkyfenceSshVerification.startActivityLogging();
    }

    @AfterMethod(groups = {"Services"})
    protected void saveLog(ITestResult t, ITestContext context) {
        String testDescription = t.getMethod().getDescription();
        // Checks if test method is complete and has info for activity validation like mapping IDs
        if (testDescription.contains("|"))
            SkyfenceSshVerification.saveActivityLog(testDescription, context.getName(), t.getName(), t.isSuccess());
    }

    @AfterTest(groups = "Services")
    protected void countSkipped(ITestContext context) {
        context.getSkippedTests().getAllMethods().stream().map(ITestNGMethod::getDescription).forEach(MappingReports::countSkipped);
    }

    @AfterSuite(groups = "Services")
    protected void makeReport(ITestContext context) {
        // Adds skipped test count to report
        FileUtils.deleteQuietly(new File("reports/report.html"));
        MappingReports.getReport(false, false);
    }

    // Below method sends the letter with screenshot in a case when it's webdriver exception
    @AfterMethod(alwaysRun = true)
    public void takeScreenShotOnFailure(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE && Retry.getCompareCounter()) {
            Throwable throwable = testResult.getThrowable();
            if (throwable instanceof WebDriverException) {
                try {
                    File currentScr = ScreenshotsTools.getCurrentScreenshot();
                    SendAttachmentInEmail.addAttachments(testResult.getName(), -1, currentScr);
                } catch (UnhandledAlertException e) {
                    discardAllert();
                }
                Retry.setCompareCounter(false);
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    protected void sendMailWithScreen(ITestContext context) {
        // Adds skipped test count to report
        SendAttachmentInEmail.sendMail();
    }

    @AfterSuite(groups = "Services")
    protected void deleteLogs() {
        SkyfenceSshVerification.deleteTempLogs();
    }

}






