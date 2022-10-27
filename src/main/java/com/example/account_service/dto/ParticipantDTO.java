package com.example.account_service.dto;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.enumeration.DistrictName;
import lombok.Data;

import java.util.List;

@Data
public class ParticipantDTO {
    private String name;
    private String lastName;
    private int age;
    private String expectations;

    private String email;
    private String password;

    private CityName cityName;
    private List<DistrictName> districtNames;
}
