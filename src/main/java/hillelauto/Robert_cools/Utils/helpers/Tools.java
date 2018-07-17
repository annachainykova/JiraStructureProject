package hillelauto.Robert_cools.Utils.helpers;

import Configs.TestConfigVariables;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;

public class Tools implements TestConfigVariables {

    private static final int pause = 30;
    private static final int defaultWaitTime = 15;
    private static WebDriver driver;

    static {
        System.setProperty("java.awt.headless", "true");
    }

    public static void setDriver(WebDriver driver) {
        Tools.driver = driver;
    }

    public static void sleep(int timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
        }
    }

    // Elements
    public static WebElement findElement(By bySelector) {
        return driver.findElement(bySelector);
    }

    public static boolean isAbsentElement(By bySelector) {
        return driver.findElements(bySelector).isEmpty();
    }

    // Waiters
    public static void waitForElementClickable(WebElement element) {
        new WebDriverWait(driver, defaultWaitTime).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForElementClickable(By bySelector) {
        new WebDriverWait(driver, defaultWaitTime).until(ExpectedConditions.elementToBeClickable(bySelector));
    }

    public static void waitForElementClickable(WebElement element, int timeout) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForElementDisplayed(WebElement element) {
        new WebDriverWait(driver, defaultWaitTime).until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForElementDisplayed(WebElement element, int timeout) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForElementDisplayed(By bySelector) {
        new WebDriverWait(driver, defaultWaitTime).until(ExpectedConditions.visibilityOfElementLocated(bySelector));
    }

    public static void waitForElementDisplayed(By bySelector, int timeout) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(bySelector));
    }

    public static void waitForTextInElement(WebElement element, String text) {
        new WebDriverWait(driver, defaultWaitTime).until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public static void waitForElementInvisible(By bySelector, int timeout) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.invisibilityOfElementLocated(bySelector));
    }

    public static void fluentWaitForElementDisplayed(By bySelector, int timeout) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(600))
                .pollingEvery(Duration.ofSeconds(timeout))
                .ignoring(NoSuchElementException.class);

        wait.until((webDriver) -> {
            webDriver.navigate().refresh();
            return driver.findElement(bySelector);
        });
    }

    public static void fluentWaitForElementDisplayed(By bySelector, WebElement refreshElement) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(600))
                .pollingEvery(Duration.ofSeconds(10))
                .ignoring(NoSuchElementException.class);

        wait.until((webDriver) -> {
            clickOn(refreshElement);
            return driver.findElement(bySelector);
        });
    }

    public static void fluentWaitForElementDisplayed(WebElement element, By bySelector) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(600))
                .pollingEvery(Duration.ofSeconds(10))
                .ignoring(NoSuchElementException.class);

        wait.until((webDriver) -> {
            clickOn(bySelector);
            return element;
        });
    }

    public static void fluentWaitForElementDisappeared(By bySelector) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(600))
                .pollingEvery(Duration.ofSeconds(10))
                .ignoring(NoSuchElementException.class);

        wait.until((webDriver) -> {
            webDriver.navigate().refresh();
            return driver.findElements(bySelector).isEmpty();
        });
    }

    public static void fluentWaitForElementDisappeared(By bySelector, int timeout) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(600))
                .pollingEvery(Duration.ofSeconds(timeout))
                .ignoring(NoSuchElementException.class);

        wait.until((webDriver) -> {
            webDriver.navigate().refresh();
            return driver.findElements(bySelector).isEmpty();
        });
    }

    public static void fluentWaitForElementDisappeared(By bySelector, WebElement refreshElement) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(600))
                .pollingEvery(Duration.ofSeconds(10))
                .ignoring(NoSuchElementException.class);

        wait.until((webDriver) -> {
            clickOn(refreshElement);
            return driver.findElements(bySelector).isEmpty();
        });
    }

    public static void waitForPageToLoadJSBased() {
        Wait<WebDriver> wait = new WebDriverWait(driver, pause);
        wait.until(driver1 -> {
            System.out.println("Current Window State       : " + String.valueOf(((JavascriptExecutor) driver1).executeScript("return document.readyState")));
            return String.valueOf(((JavascriptExecutor) driver1).executeScript("return document.readyState")).equals("complete");
        });
    }

    public static void switchTab(int tab) {
        ArrayList<String> tabs2 = new ArrayList<>(driver.getWindowHandles());
        if (tabs2.size() < 2) sleep(2);
        driver.switchTo().window(tabs2.get(tab));
    }

    public static void backToMainTab() {
        driver.close();
        switchTab(0);
    }

    public static void refreshPage() {
        driver.navigate().refresh();
    }

    public static void backPage() {
        driver.navigate().back();
    }

    public static void openPageWith(String s) {
        discardAllert();
        driver.get(s);
    }

    public static void discardAllert() {
        if (isAlertPresent()) {
            driver.switchTo().alert().accept();
        }
        sleep(2);
    }

    public static boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
        }
    }

    public static void scrollDown() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0,250)", "");
    }

    // Dynamic feature
    public static void createDynamicName(String firstPartName) {
        DynamicVariableFeature.setDynamicUserName(firstPartName + timeStamp());
    }

    public static void createDynamicName(String firstPartName, String lastPartName) {
        DynamicVariableFeature.setDynamicUserName(firstPartName + timeStamp() + lastPartName);
    }

    public static void createDynamicSelector(String firstPartSelector, String closePartSelector) {
        DynamicVariableFeature.setDynamicUserSelector(firstPartSelector + getDynamicName() + closePartSelector);
    }

    public static String getDynamicName() {
        return DynamicVariableFeature.getDynamicUserName();
    }

    public static String getDynamicUserNameSelector() {
        return DynamicVariableFeature.getDynamicUserSelector();
    }

    // Clickers
    public static void click(By bySelector) {
        findElement(bySelector).click();
    }

    public static void clickOn(WebElement button) {
        waitForElementClickable(button);
        button.click();
    }

    public static void clickOn(By bySelector) {
        waitForElementClickable(bySelector);
        findElement(bySelector).click();
    }

    public static void badClickOn(WebElement button, int sleepTimeout) {
        sleep(sleepTimeout);
        waitForElementClickable(button);
        button.click();
    }

    public static void badClickOn(By bySelector, int sleepTimeout) {
        waitForElementClickable(bySelector);
        sleep(sleepTimeout);
        findElement(bySelector).click();
    }

    public static void clickOnInvisibleElement(WebElement element) {
        String script = "var object = arguments[0];"
                + "var theEvent = document.createEvent(\"MouseEvent\");"
                + "theEvent.initMouseEvent(\"click\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
                + "object.dispatchEvent(theEvent);";
        ((JavascriptExecutor) driver).executeScript(script, element);
    }

    public static void contextClick(WebElement element) {
        waitForElementClickable(element);
        new Actions(driver).moveToElement(element).contextClick().build().perform();
    }

    public static void doubleClick(WebElement element) {
        waitForElementClickable(element);
        new Actions(driver).moveToElement(element).doubleClick().build().perform();
    }

    //Field
    public static void fillInTheFieldWith(WebElement some_element, CharSequence charSequence) {
        waitForElementDisplayed(some_element);
        some_element.sendKeys(charSequence);
    }

    public static void fillInTheFieldWith(By bySelector, CharSequence charSequence) {
        waitForElementDisplayed(bySelector);
        findElement(bySelector).sendKeys(charSequence);
    }

    public static void clearTheField(WebElement some_element) {
        waitForElementDisplayed(some_element);
        some_element.clear();
    }

    //Frames
    public static void switchToFrame(By bySelector) {
        driver.switchTo().frame(driver.findElement(bySelector));
    }

    public static void switchToFrame(WebElement webElement) {
        driver.switchTo().frame(webElement);
    }

    public static void backToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    //Others
    public static File takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }

    public static String timeStamp() {
        return Long.toString(Calendar.getInstance().getTime().getTime());
    }
}
