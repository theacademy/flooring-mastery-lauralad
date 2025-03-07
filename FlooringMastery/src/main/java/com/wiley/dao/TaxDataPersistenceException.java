package com.wiley.dao;

public class TaxDataPersistenceException extends Exception{
    public TaxDataPersistenceException(String message) {
        super(message);
    }

    public TaxDataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
