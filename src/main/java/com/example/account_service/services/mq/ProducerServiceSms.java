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

import java.nio.charset.StandardCharsets;

import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.hasText;

@Service
public class ProducerServiceSms implements ProducerService {

    private final RabbitTemplate template;
    private final Queue queue;
    private final DirectExchange directExchange;
    private final ObjectMapper objectMapper;

    public ProducerServiceSms(RabbitTemplate template,
                              @Qualifier("queueSms") Queue queue,
                              @Qualifier("directExchangeSms") DirectExchange directExchange,
                              ObjectMapper objectMapper) {
        this.template = template;
        this.queue = queue;
        this.directExchange = directExchange;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendToRegistration(String phone, String code) {
        if ((hasLength(phone) && hasLength(code)) && (hasText(phone) && hasText(code))) {

            phone = validNumberPhone(phone);

            if (code.length() != 4) throw new QueueException("Не валидное кодовое слово!");
            template.setExchange(directExchange.getName());
            MessageForSend messageForSms = new MessageForSend();
            messageForSms.setToAddress(phone);
            messageForSms.setCode(code);
            try {
                var jsonMessage = objectMapper.writeValueAsString(messageForSms);
                Message message = new Message(jsonMessage.getBytes(StandardCharsets.UTF_8));
                template.send("smsKey", message);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            throw new QueueException("Номер телефона не указан или кодовое слово у пользоваетля пустое.", phone);
        }

    }

    @Override
    public void sendToInfo(String phone) {
        //TODO: Реализовать отправку уведомлений через СМС
    }

    private String validNumberPhone(String numberPhone) {
        String validNumberPhone = numberPhone.trim();
        if (numberPhone.startsWith("+")) {
            validNumberPhone = numberPhone.substring(0, 1);
        }
        if (validNumberPhone.length() != 11) throw new QueueException("Неправильный номер телефона", numberPhone);
        if (!validNumberPhone.startsWith("7")) {
            validNumberPhone = "7" + validNumberPhone.substring(1, validNumberPhone.length());
        }
        return validNumberPhone;
    }
}
