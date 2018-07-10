package hillelauto.API_http_components;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Requests {
    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();


    public static String[] sendGet(String url, String body) throws IOException {
        return send(new HttpGet(url), null);
    }

    public static String[] sendPost(String url, String body) throws IOException {
        return send(new HttpPost(url), new StringEntity(body));
    }

    public static String[] sendPut(String url, String body) throws IOException {
        return send(new HttpPut(url), new StringEntity(body));
    }

    public static String[] sendDelete(String url, String body) throws IOException {
        return send(new HttpDelete(url), null);
    }

    public static String[] sendWrongGet(String url, String body) throws  IOException {
        return send(new HttpGet(url), null);
    }

    private static String[] send(HttpRequestBase request, StringEntity body) throws IOException {
        String method_name = Thread.currentThread().getStackTrace()[2].getMethodName();
        request.setConfig(requestConfig);

        if(method_name != "sendWrongGet") {
            request.addHeader("content-type", "application/json");
        }

        if (body != null)
            ((HttpEntityEnclosingRequestBase) request).setEntity(body);

        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();

        String[] responseData = new String[3];
        responseData[0] = entity != null ? entity.toString() : "No response data.";
        responseData[1] = entity != null ? EntityUtils.toString(entity) : "No response data.";
        responseData[2] = String.valueOf(response.getStatusLine().getStatusCode());
        return responseData;
    }
}