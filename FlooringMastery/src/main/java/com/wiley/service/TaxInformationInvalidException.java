package com.wiley.service;

public class TaxInformationInvalidException extends Exception{
    public TaxInformationInvalidException(String message) {
        super(message);
    }

    public TaxInformationInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
