package com.example.account_service.controllers;

import com.example.account_service.exceptions.simple_exception.NewAccountException;
import com.example.account_service.services.AccountService;
import com.example.account_service.services.DistrictService;
import com.example.account_service.services.MasterService;
import com.example.account_service.services.ParticipantService;
import com.example.account_service.services.mq.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Random;

@RestController
@Tag(name = "Тестовый контроллер", description = "Пока что это тестовое API для внутренних нужд")
public class TestController {

    private final AccountService accountService;
    private final ProducerService producerService;
    private final ObjectMapper objectMapper;

    public TestController(AccountService accountService, ProducerService producerService, ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.producerService = producerService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String startPAge() {
        return "hello";
    }

    @GetMapping("/start")
    public String startPAge2() {
        return "hello2222";
    }

    @GetMapping("/sender")
    public ResponseEntity senderGet(@PathParam("email") String email,
                                    @PathParam("email") String code) {
        producerService.sendDataForEmail(email, code);
        return ResponseEntity.ok("Message has be sender");
    }

    @GetMapping("/check/code/{email}")
    public ResponseEntity getCheckCode(@PathVariable String email) {
        // Чисто для теста - в дальнейшем только на фронт будет передаваться ссылка, а с фронта уже пост запросом на бэк
        System.out.println(email);
        return ResponseEntity.ok("11");
    }

    @Operation(summary = "Авторизовать аккаунт", description = "Перевод аккаунт из неактивного состояния в активное")
    @PostMapping("/check/code/{email}")
    public ResponseEntity postCheckCode(@PathVariable String email, @RequestBody String code) {
        try {
            var account = accountService.getAccountByEmail(email);

            var codeAsText = objectMapper.readTree(code).get("code").asText();
            if (account.getCode().equals(codeAsText)) {
                account.setEnable(true);
                accountService.updateAccount(account);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(account, account.getPassword()));
                return ResponseEntity.status(HttpStatus.OK).body("Регистрация закончена успешно!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Введённый код не подходит, попробуйте снова или запросите новй код");
            }
        } catch (RuntimeException runtimeException) {
            throw new NewAccountException("Не получается получить аккаунт. На странице логина нажмите \" Авторизоваться по коду \" ");
        } catch (JsonMappingException e) {
            throw new NewAccountException("Не получается получить аккаунт. На странице логина нажмите \" Авторизоваться по коду \" ");
        } catch (JsonProcessingException e) {
            throw new NewAccountException("Не получается получить аккаунт. На странице логина нажмите \" Авторизоваться по коду \" ");
        }
    }
}
