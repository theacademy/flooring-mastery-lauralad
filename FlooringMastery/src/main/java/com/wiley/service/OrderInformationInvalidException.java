package com.wiley.service;

public class OrderInformationInvalidException extends Exception{
    public OrderInformationInvalidException(String message) {
        super(message);
    }

    public OrderInformationInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
