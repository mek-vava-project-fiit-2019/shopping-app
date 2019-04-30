package com.example.shoppingapp.view;

import android.view.View;

import com.example.shoppingapp.model.entity.Product;

public interface IDynamicGridLoader<T> {

    void display(T[] products, IDynamicGrid iDynamicGrid);

}
