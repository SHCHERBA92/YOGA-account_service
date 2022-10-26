package com.example.account_service.repositoryes;

import com.example.account_service.models.masters.Master;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterRepository extends JpaRepository<Master, Long> {
}
