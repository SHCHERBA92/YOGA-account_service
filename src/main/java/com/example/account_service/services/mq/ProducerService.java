package com.example.account_service.services.mq;

import com.example.account_service.dto.MessageForMail;

import com.example.account_service.exceptions.simple_exception.QueueException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public ProducerService(RabbitTemplate template, Queue queue, DirectExchange directExchange, ObjectMapper objectMapper) {
        this.template = template;
        this.queue = queue;
        this.directExchange = directExchange;
        this.objectMapper = objectMapper;
    }

    public void sendDataForEmail(String email, String code) {
        if ((hasLength(email) && hasLength(code)) && (hasText(email) && hasText(code))) {
            if (code.length() != 4) throw new QueueException("Не валидное кодовое слово!");
            template.setExchange(directExchange.getName());
            MessageForMail messageForMail = new MessageForMail();
            messageForMail.setEmail(email);
            messageForMail.setKod(code);
            try {
                var jsonMessageForMail = objectMapper.writeValueAsString(messageForMail);
                Message message = new Message(jsonMessageForMail.getBytes(StandardCharsets.UTF_8));
                template.send("emailKey", message);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            throw new QueueException("Пустые email или кодовое слово у пользоваетля.", email);
        }

    }
}
