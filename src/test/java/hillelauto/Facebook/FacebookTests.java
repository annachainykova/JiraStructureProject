package hillelauto.Facebook;

import hillelauto.Facebook.Pages.LoginPage;
import hillelauto.Facebook.Pages.ProfilePage;
import hillelauto.Tools;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class FacebookTests{
    private LoginPage loginPage;
    private ProfilePage profilePage;


    @Test
    public void login() {
        loginPage.login();
    }

    @Test
    public String api(String path) {
        String response = given().
            when().
                get(path)
            .then()
                .extract().response().body().asString();
        String patternString = ".*fb://profile/(\\d+)\".*";
        //System.out.println(response);
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()){
            String ID = matcher.group(1);
            return ID;
        } else {
            return "No such user";
        }
    }

    @Test
    public void apit() throws IOException {
        List <String> lines = Tools.lineOfFile("new.csv");
        ArrayList <String> newLines = new ArrayList<>();
        String patternString = ".*profile.php\\?id=(\\d+)&.*";
        Pattern pattern = Pattern.compile(patternString);
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                newLines.add(line.split(",")[0] + "," + matcher.group(1));
            } else {
                newLines.add(line.split(",")[0] + api(line.split(",")[1]));
            }
        }
        Files.write(Paths.get("newfile.txt"), newLines);
    }
}

