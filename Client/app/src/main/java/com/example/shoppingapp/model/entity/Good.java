package com.example.shoppingapp.model.entity;

public class Good extends CartItem {

    private int price;
    private int shopId;



    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
}
