package com.example.account_service.services;

import com.example.account_service.exceptions.simple_exception.EntityException;
import com.example.account_service.models.masters.Master;
import com.example.account_service.repositoryes.MasterRepository;
import org.springframework.stereotype.Service;

@Service
public class MasterService {
    private final MasterRepository masterRepository;

    public MasterService(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    //    @Transactional(propagation = Propagation.MANDATORY)
    public void addNewMaster(Master master) {
        if (master==null) throw new EntityException("Отсутствует тренер", Master.class);
        masterRepository.save(master);
    }
}
