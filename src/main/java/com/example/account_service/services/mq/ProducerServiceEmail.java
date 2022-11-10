package com.example.account_service.services.mq;

import com.example.account_service.dto.MessageForSend;
import com.example.account_service.exceptions.simple_exception.QueueException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.hasText;

import java.nio.charset.StandardCharsets;

@Service
public class ProducerServiceEmail implements ProducerService {

    private final RabbitTemplate template;
    private final Queue queue;
    private final DirectExchange directExchange;
    private final ObjectMapper objectMapper;

    public ProducerServiceEmail(RabbitTemplate template,
                                @Qualifier("queueEmail") Queue queue,
                                @Qualifier("directExchangeMail") DirectExchange directExchange,
                                ObjectMapper objectMapper) {
        this.template = template;
        this.queue = queue;
        this.directExchange = directExchange;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendToRegistration(String email, String code) {
        if ((hasLength(email) && hasLength(code)) && (hasText(email) && hasText(code))) {
            if (code.length() != 4) throw new QueueException("Не валидное кодовое слово!");
            template.setExchange(directExchange.getName());
            MessageForSend messageForMail = new MessageForSend();
            messageForMail.setToAddress(email);
            messageForMail.setCode(code);
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

    @Override
    public void sendToInfo(String email) {
        //TODO: Реализовать отправку уведомлений через почту
    }
}
