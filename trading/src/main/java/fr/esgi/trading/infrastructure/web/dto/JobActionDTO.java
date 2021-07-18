package fr.esgi.trading.infrastructure.web.dto;

import fr.esgi.trading.domain.Job;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JobActionDTO {

    @NotBlank(message = "userID is mandatory")
    private int userID;
}
