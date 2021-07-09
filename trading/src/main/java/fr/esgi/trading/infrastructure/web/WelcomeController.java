package fr.esgi.trading.infrastructure.web;

import fr.esgi.trading.infrastructure.messaging.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @Autowired
    RabbitMQSender rabbitmqSender;
    @GetMapping("/")
    public String Welcome(){
        rabbitmqSender.send();
        return "Hello world";
    }
}
