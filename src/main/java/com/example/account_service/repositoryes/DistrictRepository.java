package com.example.account_service.repositoryes;

import com.example.account_service.enumeration.DistrictName;
import com.example.account_service.models.masters.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {
//    List<District> findDistrictsByDistrictName(List<DistrictName> districtNameList);
}
