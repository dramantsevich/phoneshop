package com.es.core.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException() {
        super("No such quantity available");
    }
}
