package fr.esgi.users.domain;

import keys.UserQueueKey;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    public final UserRepository userRepository;
    public final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User addUser(User user){

        User dbUser = userRepository.save(
                new User(
                        user.getEmail(),
                        encoder.encode(user.getPassword())
                )
        );
        return dbUser;
    }

    public User updateUserStats(int userId, UserQueueKey action){

        User userDB = userRepository.getById(userId);


        switch (action) {
            case INCR_FINISHED:
                userDB.setFinishedJobCounter(userDB.getFinishedJobCounter() + 1);
                break;

            case INCR_ACCEPTED:
                userDB.setAcceptedJobCounter(userDB.getAcceptedJobCounter() + 1);
                break;

            case INCR_POSTED:
                userDB.setPostedJobCounter(userDB.getPostedJobCounter() + 1);
                break;
        }


        return userRepository.save(userDB);
    }
    
}
