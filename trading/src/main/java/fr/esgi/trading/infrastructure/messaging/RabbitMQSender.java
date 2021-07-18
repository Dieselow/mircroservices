package fr.esgi.trading.infrastructure.messaging;

import fr.esgi.trading.infrastructure.messaging.keys.UserQueueKey;
import net.minidev.json.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
