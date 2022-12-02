package com.example.account_service.services.mq;

public interface ProducerService {
    void sendToRegistration(String to, String code);

    void sendToInfo(String to);
}
