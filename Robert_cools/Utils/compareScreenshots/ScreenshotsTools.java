package hillelauto.Robert_cools.Utils.compareScreenshots;

import hillelauto.Robert_cools.Configs.TestConfigVariables;
import hillelauto.Robert_cools.Utils.Listeners.Retry;
import hillelauto.Robert_cools.Utils.sendMail.SendAttachmentInEmail;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;

import static hillelauto.Robert_cools.Utils.helpers.EnvironmentHelper.*;


public class ScreenshotsTools implements TestConfigVariables {
    private static boolean failureCount;
    private static String currentScreen = "bin/CurrentScreen.jpg";
    private static File currentScr;
    private static File expectedScr;

    public static int getPercentageMatching(File fileA, File fileB) {
        int percentage = 0;
        try {
            // take buffer data from both image files //
            BufferedImage biA = ImageIO.read(fileA);
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            System.out.println("Expected screen size is " + sizeA);

            BufferedImage biB = ImageIO.read(fileB);
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            System.out.println("Current screen size is " + sizeB);
            int count = 0;
            // compare data-buffer objects
            if (sizeA == sizeB) {
                for (int i = 0; i < sizeA; i++) {
                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count++;
                    }
                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size ");
            }
        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
        }
        System.out.println("Images are matched on " + percentage + "%");
        if (percentage > 96) Retry.setCompareCounter(false);
        return percentage;
    }

    public static File getExpectedScreenshot(String service, String page) {

        String screenshotFile = makePathForScreen(service, page);

        if (!isFileExists(screenshotFile)) {
            System.out.println("File doesn't exists ::" + screenshotFile);
            copyFile(currentScr, screenshotFile);
        }
        File expectedScreenshots = new File(screenshotFile);
        return expectedScreenshots;
    }

    private static String makePathForScreen(String service, String page) {
        String currentOS = getCurrentTypeOS().contains("win") ? OS_WIN : OS_MAC;
        makeDirecoryForScreenshots(service);
        return getScreenShotFolder() + service + page + "Screenshot" + currentOS;

    }

    public static File getCurrentScreenshot() {
        // Take Screenshot
        File scrFile = takeScreenshot();
        System.out.println("ScreenShot is taken ");
        copyFile(scrFile, currentScreen);
        return scrFile;
    }

    public void doPagesComparing(String servicePage, String screenShots) {

        currentScr = getCurrentScreenshot();
        expectedScr = getExpectedScreenshot(screenShots, servicePage);

        int percents = getPercentageMatching(currentScr, expectedScr);
        if (percents < 96) {
            failureCount = true;
            if (failureCount && Retry.getCompareCounter()) {
                SendAttachmentInEmail.addAttachments(servicePage, percents, currentScr);
                failureCount = false;
                Retry.setCompareCounter(false);
            }
            Assert.assertFalse(percents < 96, "Comparing failed!");
        }
    }
}



