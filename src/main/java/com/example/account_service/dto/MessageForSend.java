package com.example.account_service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageForSend implements Serializable {
    private String toAddress;
    private String code;
}
