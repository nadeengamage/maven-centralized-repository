package me.nadeen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpRequest {

    private static final Logger logger = LogManager.getLogger(HttpRequest.class);

    private final String APACHE_MAVEN_HOST_PROTOCOL = "https://";
    private final String APACHE_MAVEN_HOST = "repo.maven.apache.org";
    private final String APACHE_MAVEN_URL = "/maven2/";
    
    Integer PORT = 443;
    private String method = "GET";
    private String path = "/";
    private String version = "HTTP/1.1";

    HashMap<String, String> headers = new HashMap<>();

    // Used for getResponse()
    Socket socket;
    DataOutputStream outputStream;
    BufferedReader reader;

    public HttpResponse getResponse(String path) throws Exception {
        logger.info("getResponse() called... " + path);
        
        HttpsURLConnection connection = (HttpsURLConnection) new URL(APACHE_MAVEN_HOST_PROTOCOL + APACHE_MAVEN_HOST + String.format("%s%s", APACHE_MAVEN_URL, path)).openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Host", APACHE_MAVEN_HOST);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("Cache-Control", "max-age=0");

        // Send request
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        HttpResponse response = new HttpResponse(reader);

        return response;
    }
}
