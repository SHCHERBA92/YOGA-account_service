package com.example.account_service.repositoryes;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.models.masters.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameOfCity(CityName cityName);
}
