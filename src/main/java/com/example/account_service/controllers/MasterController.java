package com.example.account_service.controllers;

import com.example.account_service.dto.MasterDTO;
import com.example.account_service.dto.MasterFilterDTO;
import com.example.account_service.dto.ResponseMasterDTO;
import com.example.account_service.enumeration.Authorities;
import com.example.account_service.enumeration.DistrictName;
import com.example.account_service.enumeration.TypeRegistration;
import com.example.account_service.models.masters.City;
import com.example.account_service.models.masters.District;
import com.example.account_service.models.masters.Master;
import com.example.account_service.models.security.Account;
import com.example.account_service.services.*;
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
@RequestMapping("/master")
@Tag(name = "Master API", description = "Для работы с сущностью master(тренер)")
public class MasterController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final MasterService masterService;
    private final CityService cityService;

    public MasterController(AccountService accountService,
                            ModelMapper modelMapper,
                            MasterService masterService,
                            CityService cityService
    ) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.masterService = masterService;
        this.cityService = cityService;
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @PostMapping("/addMaster/{typeRegistration}")
    @Operation(summary = "Добавить мастера", description = "Добавляет нового пользователя с Авторизацией MASTER и создаёт аккаунт для Master")
    public ResponseEntity addMaster(@RequestBody MasterDTO masterDTO,
                                    @PathVariable("typeRegistration") TypeRegistration typeRegistration) {
        var currentMaster = modelMapper.map(masterDTO, Master.class);
        var currentAccount = accountService.createAccountFor(modelMapper.map(masterDTO, Account.class),
                Authorities.ROLE_MASTER);

        var currentCity = cityService.getCityByName(modelMapper.map(masterDTO, City.class).getNameOfCity());

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

        if (typeRegistration.name().equals(TypeRegistration.EMAIL.name())) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:8081/sender-mail?email=" + currentAccount.getEmail() + "&code=" + currentAccount.getCode()))
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:8081/sender-sms?phone=" + currentAccount.getPhoneNumber() + "&code=" + currentAccount.getCode()))
                    .build();
        }
    }


    @PostMapping("/masters")
    @Transactional
    public List<ResponseMasterDTO> findAllMaster(@RequestBody MasterFilterDTO masterFilterDTO) {

        var masters = masterService.getAllMasters(masterFilterDTO.getName(),
                masterFilterDTO.getNameOfCity(),
                masterFilterDTO.getRatingMax(),
                masterFilterDTO.getRatingMin(),
                masterFilterDTO.getPriceMax(),
                masterFilterDTO.getPriceMin(),
                masterFilterDTO.getOrderType());
        var masterDTOS = masters.stream()
                .map(master -> {
                    var temp = modelMapper.map(master, ResponseMasterDTO.class);
                    temp.setDistrictNames(master.getCity().getDistricts().stream()
                            .map(District::getDistrictName)
                            .collect(Collectors.toList()));
                    return temp;
                })
                .collect(Collectors.toList());
        return masterDTOS;
    }


    @GetMapping("/info/{id}")
    public Master infoMaster(@PathVariable Long id) {
        return masterService.getMasterByID(id);
    }


}
