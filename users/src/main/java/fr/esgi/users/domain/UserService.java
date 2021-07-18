package fr.esgi.users.domain;

import fr.esgi.users.infrastructure.messaging.keys.UserQueueKey;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
