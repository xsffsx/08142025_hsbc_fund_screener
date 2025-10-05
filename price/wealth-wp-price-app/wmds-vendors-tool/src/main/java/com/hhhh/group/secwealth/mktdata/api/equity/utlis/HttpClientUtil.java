package com.hhhh.group.secwealth.mktdata.api.equity.utlis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;


public class HttpClientUtil {

    public static String doGet(String url, String usernameColonPassword, String proxyHost, Integer proxyPort) throws Exception {
        BufferedReader httpResponseReader = null;
        try {
            // Connect to the web server endpoint
            URL serverUrl = new URL(url);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection(proxy);
            // Set HTTP method as GET
            urlConnection.setRequestMethod("GET");

            // Include the HTTP Basic Authentication payload
            urlConnection.addRequestProperty("Authorization", usernameColonPassword);

            // Read response from web server, which will trigger HTTP Basic Authentication request to be sent.
            // return 200 or 401 ResponseCode
            StringBuilder sb = new StringBuilder();
            if (urlConnection.getResponseCode() == 200) {
                InputStreamReader inR = new InputStreamReader(urlConnection.getInputStream());
                httpResponseReader = new BufferedReader(inR);

                String lineRead;
                while ((lineRead = httpResponseReader.readLine()) != null) {
                    sb.append(lineRead);
                }
            }
            return sb.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        } finally {
            if (httpResponseReader != null) {
                try {
                    httpResponseReader.close();
                } catch (IOException ioe) {
                    // Close quietly
                }
            }
        }
    }


}
