package com.example.account_service.services.mq;

import com.example.account_service.dto.MessageForMail;

import com.example.account_service.exceptions.simple_exception.QueueException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.hasText;

import java.nio.charset.StandardCharsets;

@Service
public class ProducerService {

    private final RabbitTemplate template;
    private final Queue queue;
    private final DirectExchange directExchange;

    public ProducerService(RabbitTemplate template, Queue queue, DirectExchange directExchange) {
        this.template = template;
        this.queue = queue;
        this.directExchange = directExchange;
    }

    public void sendDataForEmail(String email, String code) {
        if ((hasLength(email) && hasLength(code) )&&(hasText(email) && hasText(code))){
            if (code.length()!=4) throw new QueueException("Не валидное кодовое слово!");
            template.setExchange(directExchange.getName());
            MessageForMail messageForMail = new MessageForMail();
            messageForMail.setEmail(email);
            messageForMail.setKod(code);
            Message message = new Message(messageForMail.toString().getBytes(StandardCharsets.UTF_8));
            template.send("emailKey", message);
        }else {
            throw new QueueException("Пустые email или кодовое слово у пользоваетля.", email);
        }

    }
}
