package fr.esgi.trading.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private String exchange;
    private String routingkey;

    @Autowired
    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public RabbitMQSender(String exchange, String routingkey) {
        this.exchange = exchange;
        this.routingkey = routingkey;
    }


    public void send(Object object) {
        rabbitTemplate.convertAndSend(exchange, routingkey, object);
    }
}