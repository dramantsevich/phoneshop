package com.es.core.exception;

public class PhonePriceException extends RuntimeException {
    public PhonePriceException() {
        super("not available now");
    }
}
