package com.example.account_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "Account Service API",
        version = "1.0.1",
        description = "Information about Users and Account"))
public class AccountServiceApplication {

    // TODO: Добавить Swagger
    // TODO: Добавить тестирование
    // TODO: Добавить Кэширование через Redis для сущности Participant
    // TODO: Добавить фильтр и если получится пагинацию для сущности Participant
    // TODO: Добаить логирование
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
