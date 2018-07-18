package hillelauto.Robert_cools.Utils.Listeners;

import org.testng.IResultMap;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Set;

// TestListener purpose: test that re-runs not to affect total number of tests
public class TestListener implements ITestListener {
    @Override
    public void onFinish(ITestContext context) {
        Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
//        IResultMap passedTests = context.getPassedTests();
//        IResultMap failedTests = context.getFailedTests();

        // Removes duplicated skip results
//        skippedTests.removeIf(result -> 0 != passedTests.getResults(result.getMethod()).size() + failedTests.getResults(result.getMethod()).size());
    }

    // this method before each test cleans Map with skipped tests
    // this way we run tests which depends from tests skipping before
    @Override
    public void onTestStart(ITestResult result) {
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

    public void onStart(ITestContext context) {
    }
}