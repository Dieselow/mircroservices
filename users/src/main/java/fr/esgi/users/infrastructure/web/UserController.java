package fr.esgi.users.infrastructure.web;

import fr.esgi.users.domain.User;
import fr.esgi.users.domain.UserRepository;
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

    UserController(UserService userService){
        this.userService = userService;
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
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<User> add(@RequestBody() User user) {

        User dbUser = userService.userRepository.save(
            new User(user.getEmail(), user.getPassword())
        );
        return new ResponseEntity<>(dbUser, HttpStatus.CREATED);
    }

}