package com.example.shoppingapp.controller.modelmanager;

import android.view.View;

import com.example.shoppingapp.controller.viewmanager.TableManager;
import com.example.shoppingapp.model.entity.Product;
import com.example.shoppingapp.model.service.HTTPManager;
import com.example.shoppingapp.view.DetailedProductView;
import com.example.shoppingapp.view.IDynamicGrid;
import com.example.shoppingapp.view.IDynamicGridLoader;

import java.util.HashMap;

public class ProductManager extends HTTPManager<Product> implements IDynamicGridLoader<Product> {
    public ProductManager() {
        super(Product.class);
    }

    @Override
    public Product[] createFake(int num){
        Product[] products = new Product[num];

        for (int i = 0; i < num; i++) {
            Product product = new Product();
            product.setName("product  id:  " + i);
            product.setId(i);
            products[i] = (product);
        }
        return products;
    }


    @Override
    public HashMap<String, String> buildBodyHashMap(Product entity) {
        HashMap<String, String> bodyHashMap = new HashMap();

        bodyHashMap.put("id", String.valueOf(entity.getId()));
        bodyHashMap.put("description", String.valueOf(entity.getName()));
        bodyHashMap.put("category", String.valueOf(entity.getCategory()));
        bodyHashMap.put("picture", String.valueOf(entity.getPicture()));

        return bodyHashMap;
    }



    @Override
    public void display(Product[] products, IDynamicGrid iDynamicGrid){

        int addedNumber = 0;
        View leftProduct = null;
        View rightProduct;

        for (int i = 0; i < products.length; i++) {


            Product product = products[i];

            DetailedProductView detailedProductView = new DetailedProductView(iDynamicGrid.getActivity());
            detailedProductView.displayProductsDetail(product.getId(), product.getName(),product.getPicture(),0);

                TableManager.createSingle(detailedProductView, iDynamicGrid);

        }

    }
}
