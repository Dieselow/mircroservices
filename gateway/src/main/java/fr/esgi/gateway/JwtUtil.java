package fr.esgi.gateway;


import com.google.gson.Gson;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.smallrye.jwt.build.Jwt;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.springframework.security.config.Elements.JWT;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public JWT getAllClaimsFromToken(String token) {
        try {
            Base64.Decoder decoder = java.util.Base64.getUrlDecoder();

            String[] split_string = token.split("\\.");
            String base64EncodedBody = split_string[1];

            String body = new String(decoder.decode(base64EncodedBody.trim()));

            Gson gson = new Gson();

            JWT jwt = gson.fromJson(body, JWT.class);

            return jwt;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "wrong jwt format");
        }
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExp() < (int) new Date().getTime();
    }

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

}