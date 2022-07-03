package com.es.core.exception;

public class PhonePriceException extends RuntimeException {
    public PhonePriceException() {
        super("Not available now, price is null");
    }
}
