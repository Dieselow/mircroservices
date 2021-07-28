package fr.esgi.auth.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class User {
    private Integer id;

    private String email;

    private String password;

    public int postedJobCounter;
    public int acceptedJobCounter;
    public int finishedJobCounter;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.postedJobCounter = 0;
        this.acceptedJobCounter = 0;
        this.finishedJobCounter = 0;
    }

    public User(){}
}
