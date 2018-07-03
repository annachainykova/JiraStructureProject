package hillelauto.Facebook;

import hillelauto.Facebook.Pages.LoginPage;
import hillelauto.Facebook.Pages.ProfilePage;
import hillelauto.Tools;
import hillelauto.WebDriverTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.tools.Tool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacebookTests extends WebDriverTestBase{
    private LoginPage loginPage;
    private ProfilePage profilePage;


    @BeforeClass(alwaysRun = true)
    public void setPages() {
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        profilePage = PageFactory.initElements(driver, ProfilePage.class);

    }
    @Test
    public void login() {
        loginPage.login();
    }

    @Test (dependsOnMethods = "login")
    public void fac() throws InterruptedException, IOException {
        List <String> lines = Tools.lineOfFile("new.csv");
        ArrayList <String> newLines = new ArrayList<>();
        for (String line : lines) {
            line = line + ", " + profilePage.getId(line);
            System.out.println(line);
            newLines.add(line);
        }
        for(String fa : newLines){
            System.out.println(fa);
        }

    }
}
