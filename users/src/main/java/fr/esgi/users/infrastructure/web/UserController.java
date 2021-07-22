package fr.esgi.users.infrastructure.web;

import fr.esgi.logger.ESLogger;
import fr.esgi.logger.Logger;
import fr.esgi.users.domain.User;
import fr.esgi.users.domain.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final ESLogger logger;

    UserController(UserService userService, ESLogger logger){
        this.userService = userService;
        this.logger = logger;
    }

    @GetMapping()
    public ResponseEntity<List<User>> listAll() {
        List<User> users = userService.userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable() Integer id) {
        Optional<User> user = userService.userRepository.findById(id);
        if(!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.log("got user: " + user.get().getEmail());
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<User> add(@RequestBody() User user) {

        User dbUser = userService.userRepository.save(
            new User(user.getEmail(), user.getPassword())
        );
        logger.log("created user: " + dbUser.getEmail());

        return new ResponseEntity<>(dbUser, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable() int id) {
        userService.userRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);


    }

}