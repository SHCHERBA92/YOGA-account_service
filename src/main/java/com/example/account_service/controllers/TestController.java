package com.example.account_service.controllers;

import com.example.account_service.dto.MasterDTO;
import com.example.account_service.dto.ParticipantDTO;
import com.example.account_service.enumeration.Authorities;
import com.example.account_service.models.masters.City;
import com.example.account_service.models.masters.District;
import com.example.account_service.models.masters.Master;
import com.example.account_service.models.masters.Participant;
import com.example.account_service.models.security.Account;
import com.example.account_service.services.AccountService;
import com.example.account_service.services.DistrictService;
import com.example.account_service.services.MasterService;
import com.example.account_service.services.ParticipantService;
import com.example.account_service.services.mq.ProducerService;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.Collections;
import java.util.Random;

@RestController
public class TestController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final MasterService masterService;
    private final ParticipantService participantService;
    private final PasswordEncoder passwordEncoder;
    private final DistrictService districtService;
    private final ProducerService producerService;

    public TestController(AccountService accountService, ModelMapper modelMapper, MasterService masterService, ParticipantService participantService, PasswordEncoder passwordEncoder, DistrictService districtService, ProducerService producerService) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.masterService = masterService;
        this.participantService = participantService;
        this.passwordEncoder = passwordEncoder;
        this.districtService = districtService;
        this.producerService = producerService;
    }

    @GetMapping
    public String startPAge() {
        return "hello";
    }



    @GetMapping("/sender")
    public ResponseEntity senderGet(@PathParam("email") String email,
                                    @PathParam("email") String code) {
        producerService.sendDataForEmail(email,code);
        return ResponseEntity.ok("1");
    }


    private Account createAccountFor(Account account, Authorities authorities) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAuthorities(authorities);
        //TODO: потом поставить false и менять только тогда, когда пользователь введёт секретное слово
        account.setEnable(true);
        account.setCode(this.generateCode());
        return account;
    }

    private String generateCode(){
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < 4; i++) {
            builder.append(new Random().nextInt(10));
        }
        return builder.toString();
    }
}
