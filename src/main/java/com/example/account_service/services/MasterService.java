package com.example.account_service.services;

import com.example.account_service.models.masters.Master;
import com.example.account_service.repositoryes.MasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MasterService {
    private final MasterRepository masterRepository;

    public MasterService(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

//    @Transactional(propagation = Propagation.MANDATORY)
    public void addNewMaster(Master master){
        masterRepository.save(master);
    }
}
