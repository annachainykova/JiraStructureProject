package hilleauto;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
    private static WebDriver driver;

    public static void setDriver(WebDriver driver) {
        Tools.driver = driver;
    }

    public static WebElement clearAndFill(By selector, String data) {
        WebElement element = driver.findElement(selector);
        element.clear();
        element.sendKeys(data);

        return element;
    }

    public static String timestamp() {
        return new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date());
    }
}
