package com.example.account_service.dto;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.enumeration.OrderType;
import com.example.account_service.enumeration.Rating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MasterFilterDTO {
    private String name;
    private CityName nameOfCity;
    private int priceMax;
    private int priceMin;
    private Rating ratingMax;
    private Rating ratingMin;
    private OrderType orderType;
}
