package com.example.account_service.utils;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class QueueErrorResponse {
    String message;
    String email;
    Date date;
}
