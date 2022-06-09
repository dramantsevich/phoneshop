package com.es.core.model.cart;

import com.es.core.model.phone.Stock;

public class CartItem {
    private Stock stock;

    private int quantity;

    public CartItem(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public CartItem(CartItem cartItem) {
        this.stock = cartItem.stock;
        this.quantity = cartItem.quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
