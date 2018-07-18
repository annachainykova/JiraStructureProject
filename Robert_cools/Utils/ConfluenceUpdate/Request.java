package hillelauto.Robert_cools.Utils.ConfluenceUpdate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.apache.commons.lang.StringEscapeUtils.escapeJava;


public class Request {
    private final static Logger log = Logger.getLogger(Request.class);

    private static final String BASE_URL = "https://confluence.websense.com";
    private static final String USERNAMEPASSWORD = "robert.valek:9cm<2fvQDu$q";
    private static final String ENCODING = "utf-8";

    private final HttpClient client = createHttpClient_AcceptsUntrustedCerts();


    private String responseResult;
    private int number;
    private String value;

    // create  URL, PUT request for update page
    private static String getContentRestUrlHTTPS(final int contentId, final String[] expansions) throws UnsupportedEncodingException {
        final String expand = URLEncoder.encode(StringUtils.join(expansions, ","), ENCODING);
        return String.format("%s/rest/api/content/%s?expand=%s", BASE_URL, contentId, expand);
    }

    // create body for updating page
    private static String getContentBody(final int contentId, String pageName, String spaceKey, String value, int version) {
        return String.format("{\"id\":\"%s\",\"type\":\"page\",\"title\":\"%s\",\"space\":{\"key\":\"%s\"},\"body\":{\"storage\":{\"value\":\"%s\",\"representation\":\"storage\"}},\"version\":{\"number\":\"%s\"}}", contentId, pageName, spaceKey, value, version);
    }

    private static HttpClient createHttpClient_AcceptsUntrustedCerts() {
        HttpClientBuilder b = HttpClientBuilder.create();

        // setup a Trust Strategy that allows all certificates.
        //
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
            b.setSSLContext(sslContext);

            // don't check Hostnames, either.
            //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
            @SuppressWarnings("deprecation") HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            // here's the special part:
            //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
            //      -- and create a Registry, to register it.
            //
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory)
                    .build();

            // now, we create connection-manager using our Registry.
            //      -- allows multi-threaded use
            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            b.setConnectionManager(connMgr);
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            log.error(e);
        }
        // finally, build the HttpClient;
        //      -- done!
        return b.build();

    }

    private static String encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public String getValue() {
        return value;
    }

    public String getResponseResult() {
        return responseResult;
    }

    // Get current page
    public void getPage(Services service) throws IOException {

        String pageObj;
        HttpEntity pageEntity;

        HttpGet getPageRequest = new HttpGet(getContentRestUrlHTTPS(service.getPageId(), new String[]{"body.storage,version"}));
        getPageRequest.addHeader("Authorization", "Basic " + encodeBase64(USERNAMEPASSWORD));
        HttpResponse getPageResponse = client.execute(getPageRequest);
        pageEntity = getPageResponse.getEntity();
        pageObj = IOUtils.toString(pageEntity.getContent(), ENCODING);
        responseResult = getPageResponse.getStatusLine().toString();
        log.info("Get request " + responseResult + " " + service.getPageName());

// Parse response into JSON
        if (responseResult.contains("200")) {
            JSONObject page = new JSONObject(pageObj);
            value = page.getJSONObject("body").getJSONObject("storage").getString("value");
            number = page.getJSONObject("version").getInt("number");
        } else {
            log.error("getPage" + service.name() + responseResult);
        }
    }

    public void updatePage(String table, Services service) throws IOException {


        try {
            HttpPut putPageRequest = new HttpPut(getContentRestUrlHTTPS(service.getPageId(), new String[]{"body.storage"}));
            putPageRequest.addHeader("Content-Type", "application/json");
            putPageRequest.addHeader("Authorization", "Basic " + encodeBase64(USERNAMEPASSWORD));
            StringEntity entity = new StringEntity(getContentBody(service.getPageId(), service.getPageName(), service.getSpaceKey(), escapeJava(table), number + 1), ContentType.APPLICATION_JSON); //form body
            putPageRequest.setEntity(entity);
            HttpResponse putPageResponse = client.execute(putPageRequest);
            log.info("Put request " + putPageResponse.getStatusLine().toString() + " " + service.getPageName());
        } catch (HttpHostConnectException e) {
            log.error("Put request" + service.name() + e);
        }
    }
}