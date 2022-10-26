package com.example.account_service.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queueEmail(){
        return new Queue("queueEmail");
    }

    @Bean
    public Binding bindingEmail(){
        return BindingBuilder.bind(queueEmail()).to(directExchange()).with("emailKey");
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("emailExchange");
    }

}
