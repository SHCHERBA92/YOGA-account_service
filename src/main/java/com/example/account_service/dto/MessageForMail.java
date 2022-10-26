package com.example.account_service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageForMail implements Serializable {
    private String email;
    private String kod;
}
