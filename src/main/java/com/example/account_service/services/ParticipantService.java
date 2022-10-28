package com.example.account_service.services;

import com.example.account_service.exceptions.simple_exception.EntityException;
import com.example.account_service.models.masters.Participant;
import com.example.account_service.repositoryes.ParticipantRepository;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void addNewParticipant(Participant participant) {
        if (participant==null) throw new EntityException("Отсутствует пользователь", Participant.class);
        participantRepository.save(participant);
    }
}
