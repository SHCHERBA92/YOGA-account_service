package com.example.account_service.exceptions.simple_exception;

public class QueueException extends RuntimeException {

    private String email;

    public QueueException(String message) {
        super(message);
    }

    public QueueException(String message, String email) {
        super(message);
        this.email = email;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getEmail() {
        return this.email;
    }
}
