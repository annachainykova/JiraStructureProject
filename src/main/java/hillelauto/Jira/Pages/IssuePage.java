package hillelauto.Jira.Pages;

import hillelauto.Jira.JiraVars;
import hillelauto.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;



import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class IssuePage {
    private final WebDriver driver;

    private final By inputProject = By.cssSelector("input[id='project-field']");
    private final By inputSummary = By.cssSelector("input[id='summary']");
    private String newIssuePath;
    private String attachmentLink;

    @FindBy(css = "a[id='create_link']")
    private WebElement buttonCreateIssue;
    @FindBy(css = "a[class='issue-created-key issue-link']")
    private List <WebElement> linkNewIssues;
    @FindBy(css = "input[class='issue-drop-zone__file ignore-inline-attach']")
    private WebElement inputUploadAttachment;
    @FindBy(css = "a[class='attachment-title']")
    private WebElement linkAttachmentName;


    public IssuePage(WebDriver driver) {
        this.driver = driver;
    }

    public void createIssue(){
        buttonCreateIssue.click();

        Tools.clearAndFill(inputProject, JiraVars.issueProject);

        new FluentWait<>(driver).withTimeout(Duration.ofSeconds(5)).pollingEvery(Duration.ofMillis(500))
                .ignoring(InvalidElementStateException.class)
                .until(driver -> Tools.clearAndFill(inputSummary, JiraVars.newIssueSummary)).submit();
        Assert.assertTrue(linkNewIssues.size() != 0);

        newIssuePath = linkNewIssues.get(0).getAttribute("href");
    }

    public void openIssue() {
        driver.get(newIssuePath);
        Assert.assertTrue(driver.getTitle().contains(JiraVars.newIssueSummary));
    }

    public void setInputUploadAttachment() {
        inputUploadAttachment.sendKeys(JiraVars.attachmentFileLocation + JiraVars.attachmentFileName);

        new FluentWait<>(driver).withTimeout(Duration.ofSeconds(10)).pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class)
                .until(driver -> linkAttachmentName);
        Assert.assertEquals(JiraVars.attachmentFileName, linkAttachmentName.getText());

        attachmentLink = linkAttachmentName.getAttribute("href");
    }

    public void downloadAttachment() throws IOException {
       driver.get(attachmentLink);
        URL website = new URL(driver.getCurrentUrl());
        Tools.downloadFileFromURL(website);
        Assert.assertEquals(Tools.hashOfFile(JiraVars.attachmentFileLocation+JiraVars.attachmentFileName),
                Tools.hashOfFile(JiraVars.attachmentFileLocation + JiraVars.downloadedFileName));
        File file1 = new File(website.getFile());
        File file2 = new File(JiraVars.attachmentFileLocation + JiraVars.downloadedFileName);
       Assert.assertTrue(FileUtils.contentEquals(file1, file2));


    }
}
