package hillelauto.Robert_cools.Utils.helpers;


import hillelauto.Robert_cools.Configs.TestConfigVariables;
import hillelauto.Robert_cools.Utils.connectionToSkyfence.SkyfenceConnectionSettings;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EnvironmentHelper implements TestConfigVariables {

    private static String overridesPath;
    private static String screenShotFolder;
    private static String screenshotsPath;
    private static String gitUpdateScriptPath;
    private static String makeOverridesScriptPath;
    private static String docFilePath;
    private static String txtFilePath;


    private static String testFile;
    private static boolean localDriver;

    public static void setPathsForCurrentOS(boolean isLocalDriver) {
        localDriver = isLocalDriver;
        if (isWinOS() && isLocalDriver) {
            screenshotsPath = "D:/tests/screenshots/";
            overridesPath = "D:/Override/1/";   //  <<<<< For Artem
        } else if (isMacOS() || isWinRemoteToMacOS()) {
            screenshotsPath = "/Users/skyfence/tests/";
            overridesPath = "/Users/skyfence/repos/overrides/";
            gitUpdateScriptPath = "/Users/skyfence/repos/gitUpdate.sh";
            makeOverridesScriptPath = "/Users/skyfence/repos/makeOverrides.sh";
            testFile = "/Users/skyfence/repos/ATestFile1.docx";
        } else System.out.println("Wrong type OS ");
        docFilePath = pathFix(DOCX_TEST_FILE_LOCATION);
        txtFilePath = pathFix(TXT_TEST_FILE_LOCATION);

    }

    public static boolean isWinOS() {
        return getCurrentTypeOS().contains("win");
    }

    public static boolean isMacOS() {
        return getCurrentTypeOS().contains("mac");
    }

    private static boolean isWinRemoteToMacOS() {
        return getCurrentTypeOS().contains("win") && (!localDriver);
    }

    public static boolean isFileExists(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) return true;
        else return false;
    }

    public static void copyFile(File src, String dest) {
        try {
            FileUtils.copyFile(src, new File(dest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeDirecoryForScreenshots(String service) {
        screenShotFolder = screenshotsPath + "expectedScreenshots/" + SkyfenceConnectionSettings.getPROXY() + "/" + service + "/";
        File dirForScreenshots = new File(screenShotFolder);
        if (dirForScreenshots.mkdir()) {
            System.out.println("Folder  created by path :: \"" + screenShotFolder + "\"");
        } else {
            System.out.println(">>>Directory exists<<<");
        }
    }

    public static void runFile(String path) {
        try {
            ProcessBuilder pb = new ProcessBuilder(path);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
                System.out.println("tasklist: " + line);
            process.waitFor();
            System.out.println("Batch file done.");
        } catch (IOException e) {
            Assert.fail("File was not found or cannot be read");
        } catch (InterruptedException e) {
            Assert.fail("Batch file is not done!");
        }
    }

    public static String pathFix(String realPath) {
        File f = new File(realPath);
        String absolute = "";
        try {
            absolute = f.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return absolute;
    }

    public static String getCurrentTypeOS() {
        return System.getProperties().getProperty("os.name").toLowerCase();
    }

    public static String getCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH);
        return dateFormat.format(currentDate).toString();
    }

    public static String getTestFile() {
        return testFile;
    }

    public static String getTxtFilePath() {
        return txtFilePath;
    }

    public static String getDocFilePath() {
        return docFilePath;
    }

    public static String getScreenShotFolder() {
        return screenShotFolder;
    }

    public static String getOverridesPath() {
        return overridesPath;
    }

    public static String getGitUpdateScriptPath() {
        return gitUpdateScriptPath;
    }

    public static String getMakeOverridesScriptPath() {
        return makeOverridesScriptPath;
    }
}
