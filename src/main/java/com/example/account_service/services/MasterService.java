package com.example.account_service.services;

import com.example.account_service.enumeration.CityName;
import com.example.account_service.enumeration.OrderType;
import com.example.account_service.enumeration.Rating;
import com.example.account_service.exceptions.simple_exception.EntityException;
import com.example.account_service.models.masters.Master;
import com.example.account_service.repositoryes.MasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MasterService implements ServiceSimpleCRUD<Master>{
    private final MasterRepository masterRepository;

    public MasterService(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    //    @Transactional(propagation = Propagation.MANDATORY)
    public void addNewMaster(Master master) {
        if (master==null) throw new EntityException("Отсутствует тренер", Master.class);
        masterRepository.save(master);
    }

    public Master getMasterByID(Long id){
        return masterRepository.findById(id).orElseThrow(() -> new EntityException("НЕ удалось найти такого тренера.", Master.class));
    }

    @Override
    public List<Master> getAllBy(String name, OrderType order) {
        switch (order.name()){
            case "TYPE_NAME": return masterRepository.findAllByName(name);
            case "TYPE_CITY": return masterRepository.findAllByNameOrderByCity(name);
            case "TYPE_PRICE": return masterRepository.findAllByNameOrderByPrice(name);
            case "TYPE_RATING": return masterRepository.findAllByNameOrderByRating(name);
            default: return masterRepository.findAllByName(name);
        }
    }

    @Override
    public List<Master> getAllByCity(CityName nameOfCity, OrderType order) {
        switch (order.name()){
            case "TYPE_NAME": return masterRepository.findAllByCityNameOfCityOrderByName(nameOfCity);
            case "TYPE_CITY": return masterRepository.findAllByCityNameOfCity(nameOfCity);
            case "TYPE_PRICE": return masterRepository.findAllByCityNameOfCityOrderByPrice(nameOfCity);
            case "TYPE_RATING": return masterRepository.findAllByCityNameOfCityOrderByRating(nameOfCity);
            default: return masterRepository.findAllByCityNameOfCity(nameOfCity);
        }
    }

    @Override
    public List<Master> getAllByRating(Rating rating, OrderType order) {
        switch (order.name()){
            case "TYPE_NAME": return masterRepository.findAllByRatingOrderByName(rating);
            case "TYPE_CITY": return masterRepository.findAllByRatingOrderByCity(rating);
            case "TYPE_PRICE": return masterRepository.findAllByRatingOrderByPrice(rating);
            case "TYPE_RATING": return masterRepository.findAllByRating(rating);
            default: return masterRepository.findAllByRating(rating);
        }
    }

    @Transactional(readOnly = true)
    public List<Master> getAllMasters(String name,
                                      CityName nameOfCity,
                                      Rating ratingMax,
                                      Rating ratingMin,
                                      int priceMax,
                                      int priceMin,
                                      OrderType orderType) {
        List<Master> masters = null;
        if (nameOfCity != null){
            masters = getAllByCity(nameOfCity, orderType);
            masters = masters.stream()
                    .filter(master -> (master.getPrice() <= priceMax) && (master.getPrice() >= priceMin) )
                    .filter(master -> (master.getRating().ordinal() <= ratingMax.ordinal()) && (master.getRating().ordinal() >= ratingMin.ordinal()))
                    .collect(Collectors.toList());
            if (name != null){
                masters = masters.stream()
                        .filter(master -> master.getName().equals(name)).
                        collect(Collectors.toList());
            }
        }else {
            if (name!= null){
                masters = this.getAllBy(name, orderType);
                masters = masters.stream()
                        .filter(master -> (master.getPrice() <= priceMax) && (master.getPrice() >= priceMin) )
                        .filter(master -> (master.getRating().ordinal() <= ratingMax.ordinal()) && (master.getRating().ordinal() >= ratingMin.ordinal()))
                        .collect(Collectors.toList());
            }else {
                masters = masterRepository.findAll();
            }
        }

        return masters;
    }
}
