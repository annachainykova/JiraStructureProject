package hillelauto.Robert_cools.Utils.Listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    private static boolean compareCounter;
    private final int maxRetryCount = 1;
    private int retryCount = 0;

    public static boolean getCompareCounter() {
        return compareCounter;
    }

    public static void setCompareCounter(boolean compareCounter) {
        Retry.compareCounter = compareCounter;
    }

    // Below method returns 'true' if the test method has to be retried else 'false'
    //and it takes the 'Result' as parameter of the test method that just ran

    // This method implementation should return true if you want to re-execute your failed test and false if you don’t want to re-execute your test
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            System.out.println("Retrying test " + result.getName() + " with statusText "
                    + getResultStatusName(result.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
            retryCount++;
            compareCounter = true;
            return true;
        }
        return false;
    }

    public String getResultStatusName(int status) {
        switch (status) {
            case 1:
                return "SUCCESS";
            case 2:
                return "FAILURE";
            case 3:
                return "SKIP";
            default:
                return null;
        }
    }
}

