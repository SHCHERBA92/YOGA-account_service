package com.example.account_service.controllers;

import com.example.account_service.dto.MasterDTO;
import com.example.account_service.enumeration.Authorities;
import com.example.account_service.models.masters.City;
import com.example.account_service.models.masters.District;
import com.example.account_service.models.masters.Master;
import com.example.account_service.models.security.Account;
import com.example.account_service.services.AccountService;
import com.example.account_service.services.DistrictService;
import com.example.account_service.services.MasterService;
import com.example.account_service.services.ParticipantService;
import com.example.account_service.services.mq.ProducerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collections;
import java.util.Random;

@RestController
@RequestMapping("/master")
public class MasterController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final MasterService masterService;
    private final ParticipantService participantService;
    private final PasswordEncoder passwordEncoder;
    private final DistrictService districtService;
    private final ProducerService producerService;

    public MasterController(AccountService accountService, ModelMapper modelMapper, MasterService masterService, ParticipantService participantService, PasswordEncoder passwordEncoder, DistrictService districtService, ProducerService producerService) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.masterService = masterService;
        this.participantService = participantService;
        this.passwordEncoder = passwordEncoder;
        this.districtService = districtService;
        this.producerService = producerService;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @PostMapping("/addMaster")
    public ResponseEntity addMaster(@RequestBody MasterDTO masterDTO) {
        var currentMaster = modelMapper.map(masterDTO, Master.class);
        var currentAccount = this.createAccountFor(modelMapper.map(masterDTO, Account.class), Authorities.ROLE_MASTER);

        var currentCity = modelMapper.map(masterDTO, City.class);
        var currentDistrict = modelMapper.map(masterDTO, District.class);

        currentCity.setDistricts(Collections.singletonList(currentDistrict));
        currentDistrict.setCity(currentCity);

        currentMaster.setAccount(currentAccount);
        currentAccount.setMaster(currentMaster);

        currentMaster.setCity(currentCity);
        currentCity.setMaster(Collections.singletonList(currentMaster));

        masterService.addNewMaster(currentMaster);
        districtService.addDistrict(currentDistrict);

//        return ResponseEntity.accepted().location(URI.create("http://localhost:8081/sender")).allow(HttpMethod.POST).body(currentAccount.getEmail());
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8081/sender?email="+currentAccount.getEmail() + "&code=" + currentAccount.getCode()))
                .build();
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
