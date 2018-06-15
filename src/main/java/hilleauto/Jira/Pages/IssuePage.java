package hilleauto.Jira.Pages;

import hilleauto.Jira.JiraVars;
import hilleauto.Tools;
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

import java.net.URISyntaxException;
import java.net.URL;

import java.nio.file.Paths;
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
       driver.get("https://lh3.googleusercontent.com/5AAHbsBlfjjPOtAF8W_mhcUa-tMIe9gmNDCqcOr7KMpolvjRDsiFLSez-7Y-bzRXw4L575Wu7S_o1iob_OodfEdsAW3e_MqTemLgJ5Cqo2AQhlmsxkqTp-_1rENgtiU3UK3H-HIDAA-U7Jr85nEZ_pm3gVu2Gn7_uJpMG1GetS2GCEZcaEgcX8WRdfG7PMeHi1ai4-bduHBt4ehEBnpgAGh6W-0Gsf92tSOZwtvwsfOsL14uZUuwig5M2Dok957_mRubhDqAqfx4JNUIMGOt1I44NgN65c8khWoMvXSOmLRlf0p64VSUTKAAkmW8vxca36ZxCdPASYzAQRbgb4VNnoMKG12fSx6IWo-hdD9c50R5jkaTBbpjlag41jmsNx930iE1yFg6pYQTffryKOVC4sVTEYiAo0daVSw_HWc-DzmyJdbdtzP769ZIs8blOqlDQZDz6p_HyCjsikfzfFb11XKM48rr4dxNlGLkzKEF0TKM1VeHTL2vi01YluPZWQqr218yu-VJ4yDIYw4jcZ2vulrf2jqkskLN9fLtr7mGGLpaIS7Qyv0wxDtcZ21sTjYKk5KZANhbw0rYgu8aw_actdENVHNZp9Ss4dWMESA=w500-h300-no");
        URL website = new URL(driver.getCurrentUrl());
        Tools.downloadFileFromURL(website);
        Assert.assertEquals(Tools.hashOfFile(JiraVars.attachmentFileLocation+JiraVars.attachmentFileName),
                Tools.hashOfFile(JiraVars.attachmentFileLocation + JiraVars.downloadedFileName));
        File file1 = new File(website.getFile());
        File file2 = new File(JiraVars.attachmentFileLocation + JiraVars.downloadedFileName);
       Assert.assertTrue(FileUtils.contentEquals(file1, file2));


    }
}
