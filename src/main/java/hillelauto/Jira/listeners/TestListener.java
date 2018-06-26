package hillelauto.Jira.listeners;

import hillelauto.Jira.reporting.TestRail;
import hillelauto.Tools;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashSet;

public class TestListener implements ITestListener{

    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {

        HashSet<ITestResult> allResults = new HashSet<>();
        allResults.addAll(context.getSkippedTests().getAllResults());
        allResults.addAll(context.getFailedTests().getAllResults());
        allResults.addAll(context.getPassedTests().getAllResults());

        reportToTestRail(allResults);
    }

    private void reportToTestRail(HashSet<ITestResult> results) {
        String baseURL = "https://hillelrob.testrail.io/";
        String projectID = "1";
        String runPrefix = "Jira";
        String username = "rvalek@intersog.com";
        String password = "hillel";

        if (baseURL.isEmpty()) {
            System.out.println("TestRail URL is not set");
            return;
        }

        System.out.println("Reporting to: " + baseURL);

        TestRail trReport = new TestRail(baseURL);
        trReport.setCreds(username, password);

        try {
            trReport.startRun(Integer.parseInt(projectID), runPrefix + " Robert Auto - " + Tools.timestamp());

            for (ITestResult result : results) {
                String testDescription = result.getMethod().getDescription();
                try {
                    int caseID = Integer.parseInt(testDescription.substring(0, testDescription.indexOf(".")));
                    trReport.setResult(caseID, result.getStatus());
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println(testDescription + " - Case ID missing; not reporting to TestRail.");
                    e.printStackTrace();
                }
            }

            trReport.endRun();
            System.out.println("Sent reports successfully.");
        } catch (Exception e) {
            System.out.println("Failed to send report to TestRail.");
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result){
        result.getTestContext().getSkippedTests().removeResult(result.getMethod());
    }

    public void onTestSuccess(ITestResult result) {
    }

    public void onTestFailure(ITestResult result) {
    }

    public void onTestSkipped(ITestResult result) {
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }
}
