package com.example.account_service.controllers;

import com.example.account_service.dto.ParticipantDTO;
import com.example.account_service.enumeration.Authorities;
import com.example.account_service.models.masters.City;
import com.example.account_service.models.masters.District;
import com.example.account_service.models.masters.Participant;
import com.example.account_service.models.security.Account;
import com.example.account_service.services.AccountService;
import com.example.account_service.services.DistrictService;
import com.example.account_service.services.MasterService;
import com.example.account_service.services.ParticipantService;
import com.example.account_service.services.mq.ProducerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Random;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final ParticipantService participantService;
    private final PasswordEncoder passwordEncoder;
    private final DistrictService districtService;

    public ParticipantController(AccountService accountService, ModelMapper modelMapper, ParticipantService participantService, PasswordEncoder passwordEncoder, DistrictService districtService) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.participantService = participantService;
        this.passwordEncoder = passwordEncoder;
        this.districtService = districtService;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @PostMapping("/addParticipant")
    public ResponseEntity addParticipant(@RequestBody ParticipantDTO participantDTO) {
        var currentParticipant = modelMapper.map(participantDTO, Participant.class);
        var currentAccount = this.createAccountFor(modelMapper.map(participantDTO, Account.class), Authorities.ROLE_USER);

        var currentCity = modelMapper.map(participantDTO, City.class);
        var currentDistrict = modelMapper.map(participantDTO, District.class);

        currentCity.setDistricts(Collections.singletonList(currentDistrict));
        currentDistrict.setCity(currentCity);

        currentParticipant.setAccount(currentAccount);
        currentAccount.setParticipant(currentParticipant);

        currentParticipant.setCity(currentCity);
        currentCity.setParticipant(Collections.singletonList(currentParticipant));

        //TODO: ?????????????????? ?????????????? ?????????????? ?? ???????? ?? ???????? ???????????? ??????, ???? ?????????????????? ??????, ???????? ????????, ???? ???????????????????????????? ?? ????????????????????
        participantService.addNewParticipant(currentParticipant);
        districtService.addDistrict(currentDistrict);

        return ResponseEntity.ok("ok");
    }

    private Account createAccountFor(Account account, Authorities authorities) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAuthorities(authorities);
        //TODO: ?????????? ?????????????????? false ?? ???????????? ???????????? ??????????, ?????????? ???????????????????????? ???????????? ?????????????????? ??????????
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
