package com.example.account_service.dto;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.enumeration.DistrictName;
import lombok.Data;

import javax.persistence.Lob;

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

    private CityName cityName;
    private DistrictName districtName;
}
