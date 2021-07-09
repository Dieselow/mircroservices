package fr.esgi.users.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {
    @Autowired
    RabbitMQSender rabbitMQSender;

    @RabbitListener(queues = "user.queue")
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);
        rabbitMQSender.send();
    }
}
