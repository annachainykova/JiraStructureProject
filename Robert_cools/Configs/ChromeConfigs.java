package hillelauto.Robert_cools.Configs;


import hillelauto.Robert_cools.Utils.helpers.EnvironmentHelper;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import static hillelauto.Robert_cools.Utils.helpers.EnvironmentHelper.isMacOS;


public class ChromeConfigs {

    public static ChromeOptions createChromeOptions() {

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        String STRING_KEY = "webdriver.chrome.driver";
        String STRING_PATH = EnvironmentHelper.pathFix(isMacOS() ? "/usr/local/bin/chromedriver" : "bin/chromedriver");
        System.setProperty(STRING_KEY, STRING_PATH);
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("start-maximized");
        options.addArguments("--incognito");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-gpu ");
        //options.addArguments("--dns-prefetch-disable");
        //options.addArguments("chrome.switches","--disable-extensions");
        //options.addArguments("--disable-shared-workers");
        //options.addArguments("--kiosk");
        options.addArguments("--lang=en");
        //options.addArguments("--headless");

        return options;
    }

    public static WebDriver setDriverConfigs(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

        // below code is for window miximizing of chrome browser on mac mini
        Point targetPosition = new Point(0, 0);
        driver.manage().window().setPosition(targetPosition);

        Dimension targetSize = new Dimension(1920, 1080); //your screen resolution here
        driver.manage().window().setSize(targetSize);

        return driver;
    }

}
