package fr.esgi.logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Log {

    private String message;
    private Date date;

    @JsonCreator
    public Log(@JsonProperty("message") String message, @JsonProperty("date") Date date) {
        this.message = message;
        this.date = date;
    }
}
