package com.example.account_service.services;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.models.masters.City;
import com.example.account_service.repositoryes.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    void getCityByName() {
        City city = new City(CityName.ROSTOV_ON_DON);
        Mockito.when(cityRepository.findByNameOfCity(CityName.ROSTOV_ON_DON)).thenReturn(Optional.of(city));

        var actualCity = cityService.getCityByName(CityName.ROSTOV_ON_DON);

        Assertions.assertNotNull(actualCity, "actualCity is null");
        Assertions.assertEquals(city, actualCity);
        Mockito.verify(cityRepository).findByNameOfCity(CityName.ROSTOV_ON_DON);

    }
}