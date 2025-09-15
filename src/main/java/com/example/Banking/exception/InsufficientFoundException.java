package com.example.Banking.exception;

public class InsufficientFoundException extends RuntimeException {
    public InsufficientFoundException(String message) { super(message); }
}
