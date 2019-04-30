package com.example.shoppingapp.controller.modelmanager;

import android.view.View;

import com.example.shoppingapp.controller.viewmanager.TableManager;
import com.example.shoppingapp.model.entity.CartItem;
import com.example.shoppingapp.model.entity.Product;
import com.example.shoppingapp.model.service.HTTPManager;
import com.example.shoppingapp.view.DetailedProductView;
import com.example.shoppingapp.view.IDynamicGrid;
import com.example.shoppingapp.view.IDynamicGridLoader;

import java.util.HashMap;

public class CartItemManager extends HTTPManager<CartItem>  implements IDynamicGridLoader<CartItem> {

    public CartItemManager() {
        super(CartItem.class);
    }

    @Override
    public CartItem[] createFake(int num) {
        CartItem[] cartItems = new CartItem[num];

        ProductManager productManager = new ProductManager();

        Product[] products = productManager.createFake(num);

        for (int i = 0; i < num; i++) {
            CartItem item = new CartItem();
            item.setId(i);
            item.setAmount(i * 7);
            item.setProduct(products[i]);


            cartItems[i] = item;
        }
        return cartItems;
    }

    @Override
    protected HashMap<String, String> buildBodyHashMap(CartItem entity) {
        HashMap<String, String> bodyHashMap = new HashMap();

        bodyHashMap.put("id", String.valueOf(entity.getId()));
        bodyHashMap.put("amount", String.valueOf(entity.getAmount()));
        bodyHashMap.put("productId", String.valueOf(entity.getProduct().getId()));

        return bodyHashMap;
    }

    @Override
    public void display(CartItem[] cartItems, IDynamicGrid iDynamicGrid) {

        for (int i = 0; i < cartItems.length; i++) {

            CartItem item = cartItems[i];

            DetailedProductView detailedProductView = new DetailedProductView(iDynamicGrid.getActivity());
            detailedProductView.displayBasketDetail(item);

            TableManager.createSingle(detailedProductView, iDynamicGrid);

        }

    }

}
