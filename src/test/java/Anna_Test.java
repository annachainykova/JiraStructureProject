//import hillelauto.ApiTests.Pages.API_Vars;
import hillelauto.WebDriverTestBase;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class Anna_Test extends WebDriverTestBase{


//        private HttpClient client;
//        private HttpResponse httpResponse;
//
//        @BeforeTest
//        public void beforeTest() {
//            client = new DefaultHttpClient();
//        }
//
//        @Test
//        public void checkApiTest() throws IOException {
//
//           // HttpHost target = new HttpHost(API_Vars.HOST, 20007, "http");
//
//            // specify the get request
//            //HttpGet getRequest = new HttpGet(API_Vars.URL);
//
//
//            //HttpResponse httpResponse = client.execute(target, getRequest);
//            HttpEntity entity = httpResponse.getEntity();
//
//            Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,
//                    "Wrong status code: server is not available.");
//        }

            //HttpPost httpPost = client.

    @BeforeTest
    public void login() {

    }

    @Test
            public void fac() {
        String url = "https://www.facebook.com/kristen.m.franco?fref=grp_mmbr_list";
        driver.get(url);
        driver.findElement(By.partialLinkText("referrer_profile_id")).click();
    }
}


