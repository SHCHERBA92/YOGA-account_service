package com.example.account_service.controllers;

import com.example.account_service.dto.ParticipantDTO;
import com.example.account_service.enumeration.Authorities;
import com.example.account_service.enumeration.DistrictName;
import com.example.account_service.enumeration.TypeRegistration;
import com.example.account_service.models.masters.City;
import com.example.account_service.models.masters.District;
import com.example.account_service.models.masters.Participant;
import com.example.account_service.models.security.Account;
import com.example.account_service.services.AccountService;
import com.example.account_service.services.CityService;
import com.example.account_service.services.DistrictService;
import com.example.account_service.services.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/participant")
@Tag(name = "Participant API", description = "Для работы с сущностью participant(пользователь)")
public class ParticipantController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final ParticipantService participantService;
    private final DistrictService districtService;
    private final CityService cityService;

    public ParticipantController(AccountService accountService,
                                 ModelMapper modelMapper,
                                 ParticipantService participantService,
                                 DistrictService districtService,
                                 CityService cityService) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.participantService = participantService;
        this.districtService = districtService;
        this.cityService = cityService;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @PostMapping("/addParticipant/{typeRegistration}")
    @Operation(summary = "Добавить пользователя", description = "Добавляет нового пользователя с Авторизацией User и создаёт аккаунт для User")
    public ResponseEntity addParticipant(@RequestBody ParticipantDTO participantDTO,
                                         @PathVariable("typeRegistration") TypeRegistration typeRegistration) {

        var currentParticipant = modelMapper.map(participantDTO, Participant.class);
        var currentAccount = accountService.createAccountFor(modelMapper.map(participantDTO, Account.class),
                Authorities.ROLE_USER);

        var currentCity = cityService.getCityByName(modelMapper.map(participantDTO, City.class).getNameOfCity());

        List<DistrictName> currentDistrictNames = modelMapper.map(participantDTO.getDistrictNames(), new TypeToken<>() {
        }.getType());
        var currentDistricts = currentDistrictNames.stream()
                //TODO: проверять районы из БД - если их там нет, то добалять
                .map(districtName -> new District(districtName))
                .collect(Collectors.toList());

        currentCity.setDistricts(currentDistricts);
        currentDistricts.stream().forEach(district -> district.setCity(currentCity));

        currentParticipant.setAccount(currentAccount);
        currentAccount.setParticipant(currentParticipant);

        currentParticipant.setCity(currentCity);
        currentCity.setParticipant(Collections.singletonList(currentParticipant));

        participantService.addNewParticipant(currentParticipant);
        districtService.addAllDistrict(currentDistricts);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8081/sender?email=" + currentAccount.getEmail() + "&code=" + currentAccount.getCode()))
                .build();
    }


    @GetMapping("/info/{id}")
    public Participant infoParticipant(@PathVariable Long id) {
        return participantService.getParticipantById(id);
    }
}
