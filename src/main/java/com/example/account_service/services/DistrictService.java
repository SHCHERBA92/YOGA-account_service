package com.example.account_service.services;

import com.example.account_service.models.masters.District;
import com.example.account_service.repositoryes.DistrictRepository;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public void addDistrict(District district){
        districtRepository.save(district);
    }
}
