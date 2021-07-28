package fr.esgi.auth.infrastructure.messaging;

import fr.esgi.other.common.keys.UserQueueKey;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${spring.rabbitmq.exchange}")
    public String exchange;

    public void sendToUser(UserQueueKey routingKey, JSONObject body) {
        amqpTemplate.convertAndSend(exchange, routingKey.toString(), body);
    }
}
