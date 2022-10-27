package com.example.account_service.controllers;

import com.example.account_service.dto.MasterDTO;
import com.example.account_service.enumeration.Authorities;
import com.example.account_service.enumeration.DistrictName;
import com.example.account_service.models.masters.City;
import com.example.account_service.models.masters.District;
import com.example.account_service.models.masters.Master;
import com.example.account_service.models.security.Account;
import com.example.account_service.services.*;
import com.example.account_service.services.mq.ProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/master")
@ApiResponse
public class MasterController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final MasterService masterService;
    private final ParticipantService participantService;
    private final PasswordEncoder passwordEncoder;
    private final DistrictService districtService;
    private final ProducerService producerService;
    private final CityService cityService;
    private final ObjectMapper objectMapper;

    public MasterController(AccountService accountService,
                            ModelMapper modelMapper,
                            MasterService masterService,
                            ParticipantService participantService,
                            PasswordEncoder passwordEncoder,
                            DistrictService districtService,
                            ProducerService producerService,
                            CityService cityService,
                            ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.masterService = masterService;
        this.participantService = participantService;
        this.passwordEncoder = passwordEncoder;
        this.districtService = districtService;
        this.producerService = producerService;
        this.cityService = cityService;
        this.objectMapper = objectMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @PostMapping("/addMaster")
    public ResponseEntity addMaster(@RequestBody MasterDTO masterDTO) {
        var currentMaster = modelMapper.map(masterDTO, Master.class);
        var currentAccount = accountService.createAccountFor(modelMapper.map(masterDTO, Account.class),
                Authorities.ROLE_MASTER);

        var currentCity = cityService.getCityByName(modelMapper.map(masterDTO, City.class).getCityName());

        List<DistrictName> currentDistrictNames = modelMapper.map(masterDTO.getDistrictNames(), new TypeToken<>() {
        }.getType());
        var currentDistricts = currentDistrictNames.stream()
                //TODO: проверять районы из БД - если их там нет, то добалять
                .map(districtName -> new District(districtName))
                .collect(Collectors.toList());

        currentCity.setDistricts(currentDistricts);
        currentDistricts.stream().forEach(district -> district.setCity(currentCity));

        currentMaster.setAccount(currentAccount);
        currentAccount.setMaster(currentMaster);

        currentMaster.setCity(currentCity);
        currentCity.setMaster(Collections.singletonList(currentMaster));

        masterService.addNewMaster(currentMaster);
        districtService.addAllDistrict(currentDistricts);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8081/sender?email=" + currentAccount.getEmail() + "&code=" + currentAccount.getCode()))
                .build();
    }
}
