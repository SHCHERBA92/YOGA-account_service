package com.example.account_service.repositoryes;

import com.example.account_service.models.masters.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
