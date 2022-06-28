package com.es.core.exception;

public class NegativeQuantityException extends RuntimeException {
    public NegativeQuantityException() {
        super("Should be grater then 0");
    }
}
