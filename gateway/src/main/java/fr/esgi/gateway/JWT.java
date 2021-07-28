package fr.esgi.gateway;

import lombok.Data;

@Data
public class JWT {
    private String sub;
    private int id;
    private int iat;
    private int exp;
}
