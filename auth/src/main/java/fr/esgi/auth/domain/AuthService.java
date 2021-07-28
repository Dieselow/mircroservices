package fr.esgi.auth.domain;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AuthService {

    @Value("${jwt.ttl_ms}")
    public long tokenValidityInMilliseconds;

    @Value("${jwt.secret}")
    private String secretKey;

    public String createToken(User user) {
        Date validity = new Date(new Date().getTime() + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public User findUserByEmail(String email){

        RestTemplate restTemplate = new RestTemplate();

        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

        return restTemplate.getForObject("http://localhost:3002/users/byEmail/" + encodedEmail, User.class);
    }
}
