package com.es.core.exception;

public class QuantityNullException extends RuntimeException {
    public QuantityNullException() {
        super("Field quantity is empty");
    }
}
