package com.example.account_service.exceptions.simple_exception;

public class EntityException extends RuntimeException {

    private String nameClass;

    public EntityException(String message) {
        super(message);
    }

    public EntityException(String message, Class aClass) {
        super(message);
    }

    public String getNameClass() {
        return nameClass;
    }
}
