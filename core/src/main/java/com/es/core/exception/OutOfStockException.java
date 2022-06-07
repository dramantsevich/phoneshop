package com.es.core.exception;


import com.es.core.model.phone.Stock;

public class OutOfStockException extends Exception {
    private final Stock stock;
    private final int stockRequested;
    private final int stockAvailable;

    public OutOfStockException(Stock stock, int stockRequested, int stockAvailable) {
        this.stock = stock;
        this.stockRequested = stockRequested;
        this.stockAvailable = stockAvailable;
    }

    public Stock getStock() {
        return stock;
    }

    public int getStockRequested() {
        return stockRequested;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }
}
