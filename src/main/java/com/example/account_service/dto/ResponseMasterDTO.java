package com.example.account_service.dto;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.enumeration.DistrictName;
import com.example.account_service.enumeration.Rating;
import com.example.account_service.models.masters.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMasterDTO {
    private String name;
    private String lastName;
    private int age;
    private String description;
    private int experience;
    private int price;

    private Rating rating;
    private List<Review> reviews;

    private CityName nameOfCity;
    private List<DistrictName> districtNames;
}
