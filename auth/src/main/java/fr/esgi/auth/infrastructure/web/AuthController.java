package fr.esgi.auth.infrastructure.web;

import fr.esgi.auth.domain.AuthService;
import fr.esgi.auth.domain.User;
import fr.esgi.auth.infrastructure.messaging.RabbitMQSender;
import fr.esgi.auth.infrastructure.web.dto.LoginDTO;
import fr.esgi.auth.infrastructure.web.dto.LoginResponseDTO;
import keys.UserQueueKey;
import org.springframework.beans.factory.annotation.Autowired;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;

@RestController()
public class AuthController {

    @Autowired
    private RabbitMQSender rabbitMQSender;

    private final AuthenticationManagerBuilder authenticationManager;
    private final AuthService authService;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManagerBuilder authenticationManager, AuthService authService, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.encoder = encoder;
    }


    @PostMapping("auth/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO){

        User user = authService.findUserByEmail(loginDTO.getEmail());

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exist");
        }

        if(!encoder.matches(loginDTO.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "wrong password");
        }

        String token = authService.createToken(user);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("auth/register")
    public ResponseEntity<Object> register(@Valid @RequestBody LoginDTO body){

        JSONObject json = new JSONObject();
        json.appendField("email", body.getEmail());
        json.appendField("password", encoder.encode(body.getPassword()));

        rabbitMQSender.sendToUser(UserQueueKey.CREATE_USER, json);

        return new ResponseEntity<>(body, HttpStatus.CREATED);

    }

    @PutMapping()
    public ResponseEntity<Object> test(){
        return new ResponseEntity<>("hello", HttpStatus.CREATED);
    }
}
