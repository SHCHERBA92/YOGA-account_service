package com.example.account_service.repositoryes;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.enumeration.Rating;
import com.example.account_service.models.masters.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MasterRepository extends JpaRepository<Master, Long> {
    List<Master> findAllByName(String name);
    List<Master> findAllByNameOrderByCity(String name);
    List<Master> findAllByNameOrderByRating(String name);
    List<Master> findAllByNameOrderByPrice(String name);

    List<Master> findAllByCityNameOfCityOrderByRating(CityName nameOfCity);
    List<Master> findAllByCityNameOfCityOrderByPrice(CityName nameOfCity);
    List<Master> findAllByCityNameOfCityOrderByName(CityName nameOfCity);
    List<Master> findAllByCityNameOfCity(CityName nameOfCity);

    List<Master> findAllByRating(Rating rating);
    List<Master> findAllByRatingOrderByPrice(Rating rating);
    List<Master> findAllByRatingOrderByName(Rating rating);
    List<Master> findAllByRatingOrderByCity(Rating rating);
}
