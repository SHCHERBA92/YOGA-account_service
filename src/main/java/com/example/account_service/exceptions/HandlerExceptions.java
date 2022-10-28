package com.example.account_service.exceptions;

import com.example.account_service.exceptions.simple_exception.EntityException;
import com.example.account_service.exceptions.simple_exception.NewAccountException;
import com.example.account_service.exceptions.simple_exception.QueueException;
import com.example.account_service.utils.AccountErrorResponse;
import com.example.account_service.utils.EntityErrorResponse;
import com.example.account_service.utils.QueueErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class HandlerExceptions {

    @ExceptionHandler(QueueException.class)
    public ResponseEntity BadQueueException(QueueException queueException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new QueueErrorResponse(queueException.getMessage(), queueException.getEmail(), new Date()));
    }

    @ExceptionHandler(NewAccountException.class)
    public ResponseEntity BadAccountException(NewAccountException newAccountException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AccountErrorResponse(newAccountException.getMessage(), new Date()));
    }

    @ExceptionHandler(EntityException.class)
    public ResponseEntity BadEntityException(EntityException entityException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new EntityErrorResponse(entityException.getMessage(), entityException.getNameClass(), new Date()));
    }
}
