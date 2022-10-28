package com.example.account_service.services;

import com.example.account_service.models.masters.District;
import com.example.account_service.repositoryes.DistrictRepository;
import com.example.account_service.exceptions.simple_exception.EntityException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public void addDistrict(District district){
        if (district==null) throw new EntityException("Отсутствует район", District.class);
        districtRepository.save(district);
    }

    public void addAllDistrict(List<District> districtList){
        if (districtList.isEmpty() || districtList == null) throw new EntityException("Отсутствуют район");
        districtRepository.saveAll(districtList);
    }
}
