package com.example.account_service.services;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.enumeration.OrderType;
import com.example.account_service.enumeration.Rating;

import java.io.Serializable;
import java.util.List;

public interface ServiceSimpleCRUD<T> {
    List<T> getAllBy(String name, OrderType order);

    List<T> getAllByCity(CityName nameOfCity, OrderType order);

    List<T> getAllByRating(Rating rating, OrderType order);

}
