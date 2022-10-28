package com.example.account_service.exceptions.simple_exception;

// НЕ уверен что нужно наследоваться от SecurityException
public class NewAccountException extends SecurityException {
    public NewAccountException(String s) {
        super(s);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }
}
