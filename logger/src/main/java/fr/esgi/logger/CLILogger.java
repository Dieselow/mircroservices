package fr.esgi.logger;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CLILogger implements Logger {

    @Override
    public void log(String message) {
        System.out.println(new Log(message, new Date()));
    }
}
