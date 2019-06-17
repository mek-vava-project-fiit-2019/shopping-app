package sk.mek.shoppingapp.controller.modelmanager;

import sk.mek.shoppingapp.model.entity.Shop;
import sk.mek.shoppingapp.model.service.HTTPManager;

import java.util.HashMap;

public class ShopManager extends HTTPManager<Shop> {

    public ShopManager() {
        super(Shop.class);
    }



    @Override
    public HashMap<String, String> buildBodyHashMap(Shop entity) {
        HashMap<String, String> bodyHashMap = new HashMap();

        bodyHashMap.put("id", String.valueOf(entity.getId()));
        bodyHashMap.put("description", String.valueOf(entity.getName()));
        bodyHashMap.put("latitude", String.valueOf(entity.getLatitude()));
        bodyHashMap.put("longitude", String.valueOf(entity.getLongitude()));

        return bodyHashMap;
    }

    @Override
    public Shop[] createFake(int num) {
        return new Shop[0];
    }
}
