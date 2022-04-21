package me.nadeen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
public class ProxyService {
    
    private static final Logger logger = LogManager.getLogger(ProxyService.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/**")
    public void proxy() {
        logger.info("proxy() called...");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((request.getInputStream())));
            DataOutputStream outputStream = new DataOutputStream(response.getOutputStream());

            HttpRequest httpRequest = new HttpRequest();

            HttpResponse response = httpRequest.getResponse(this.extract(request));
            response.send(outputStream);

            reader.close();
            outputStream.close();
        } catch (Exception e) {
            logger.error("Error while proxying request: " + e.getMessage());
        }
    }

    public String extract(HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
}
