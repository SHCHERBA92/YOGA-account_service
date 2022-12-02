package com.example.account_service.dto;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.enumeration.DistrictName;
import lombok.Data;

import java.util.List;

@Data
public class MasterDTO {

    private String name;
    private String lastName;
    private int age;
    private String description;
    private int experience;
    private int price;

    private String email;
    private String password;
    private String phoneNumber;

    private CityName nameOfCity;
    private List<DistrictName> districtNames;
}
