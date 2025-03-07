package com.wiley.dao;

public class ProductDataPersistenceException  extends Exception{
    public ProductDataPersistenceException(String message) {
        super(message);
    }

    public ProductDataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
