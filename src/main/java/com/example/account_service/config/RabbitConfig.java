package com.example.account_service.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean(name = "queueEmail")
    public Queue queueEmail() {
        return new Queue("queueEmail");
    }

    @Bean(name = "directExchangeMail")
    public DirectExchange directExchangeMail() {
        return new DirectExchange("emailExchange");
    }

    @Bean(name = "bindingEmail")
    public Binding bindingEmail(@Qualifier("queueEmail") Queue queue, @Qualifier("directExchangeMail") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("emailKey");
    }

    /// Настройка очереди для отправки смс.

    @Bean(name = "queueSms")
    public Queue queueSms() {
        return new Queue("queueSms");
    }

    @Bean(name = "directExchangeSms")
    public DirectExchange directExchangeSms() {
        return new DirectExchange("smsExchange");
    }

    @Bean(name = "bindingSms")
    public Binding bindingSms(@Qualifier("queueSms") Queue queue, @Qualifier("directExchangeSms") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("smsKey");
    }

}
