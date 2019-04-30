package com.example.shoppingapp.model.entity;

import java.util.ArrayList;

/**
 * Created by Erik
 */
public class CartItem {
    private Integer id;
    private int amount;
    private Product product;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
