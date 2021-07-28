package fr.esgi.users.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fr.esgi.users.domain.User;
import fr.esgi.users.domain.UserService;
import keys.UserQueueKey;
import net.minidev.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class RabbitMQReceiver implements RabbitListenerConfigurer {

    private Gson gson = new Gson();

    private final UserService userService;

    public RabbitMQReceiver(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "userQueue", durable = "false"),
            exchange = @Exchange("direct-exchange"),
            key={"INCR_POSTED", "INCR_ACCEPTED", "INCR_FINISHED", "CREATE_USER" }
        )
    )
    @Transactional
    public void userQueue(Message msg) throws IOException {

        JSONObject body = null;
        try {
            body = gson.fromJson(new String(msg.getBody(), "UTF-8"), JSONObject.class);
        } catch (UnsupportedEncodingException e) {
            System.out.println("bad encoding, not supported");
        }

        UserQueueKey key = UserQueueKey.valueOf(msg.getMessageProperties().getReceivedRoutingKey());

        System.out.println(msg);

        switch (key) {
            case INCR_FINISHED:
            case INCR_ACCEPTED:
            case INCR_POSTED:
                userService.updateUserStats(body.getAsNumber("id").intValue(), key);
                break;
            case CREATE_USER:
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(msg.getBody(), User.class);
                userService.userRepository.save(
                        new User(user.getEmail(), user.getPassword())
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected UserQueueKey: " + key);
        }
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

    }
}
