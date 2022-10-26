package com.example.account_service.repositoryes;

import com.example.account_service.models.masters.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Long> {
}
