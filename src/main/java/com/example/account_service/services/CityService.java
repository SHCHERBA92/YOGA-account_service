package com.example.account_service.services;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.models.masters.City;
import com.example.account_service.repositoryes.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City getCityByName(CityName cityName) {
        return cityRepository.findByNameOfCity(cityName).orElse(new City(cityName));
    }
}
