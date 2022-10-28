package com.example.account_service.utils;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountErrorResponse {
    private String message;
    private Date date;

}
