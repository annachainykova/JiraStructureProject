package hillelauto;

import hillelauto.Jira.JiraVars;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    public static String timestampForUser() {
        return new SimpleDateFormat("yyMMddHHmmss").format(new Date());
    }

    public static String hashOfFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(new File(filePath));
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
    }

    public static void downloadFileFromURL(URL website) throws IOException {
        InputStream in = website.openStream();
        Files.copy(in, Paths.get(JiraVars.attachmentFileLocation + JiraVars.downloadedFileName)
                , StandardCopyOption.REPLACE_EXISTING);
        in.close();

    }

    public static void captureScreen(String methodName) throws AWTException, IOException {
        String fileName = "E://screenshotFailure.png";
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = new Robot().createScreenCapture(screenRect);
        ImageIO.write(capture, "png", new File(fileName));
    }

    public static List<String> lineOfFile(String path ) throws IOException {
        return  Files.readAllLines(Paths.get(path));
    }
}
