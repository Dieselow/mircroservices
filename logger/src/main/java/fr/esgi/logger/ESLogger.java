package fr.esgi.logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class ESLogger implements Logger {

    private static String apiUrl = "http://localhost:9200/logging/log";

    @Override
    public void log(String message) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(apiUrl, new Log(message, new Date()), Log.class);
    }
}
