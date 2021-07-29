package fr.esgi.users.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
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
