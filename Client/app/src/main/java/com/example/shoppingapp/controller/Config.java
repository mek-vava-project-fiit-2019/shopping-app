package com.example.shoppingapp.controller;

public class Config {


    //avd to computer localhost ip
    public static final String CONFIG_SERVER_PATH = "http://10.0.2.2:8000";
    public static final String CONFIG_AUTH_API_PATH = "QuickShopper/Customer/?email={email}&password={password}";
    public static final String CONFIG_PRODUCT_BY_CATEGORY_API_PATH = "QuickShopper/Product/Category/?category={category}";
    public static final String CONFIG_CART_BY_USER_ID_API_PATH = "QuickShopper/Customer/Cart/?userId={userId}";
    public static final String CONFIG_PRODUCT_BY_QR_API_PATH = "QuickShopper/Product/QR/?QR={qr}";
    public static final String CONFIG_SHOPS_API_PATH = "QuickShopper/Shop";
    public static final String CONFIG_SHOPS_NEAREST_API_PATH = "QuickShopper/Shop/Near/?longitude={longitude}&latitude={latitude}";
    public static final String CONFIG_GOOD_BY_SHOP_AND_PRODUCT_API_PATH = "QuickShopper/Shop/Sortiment/?shopId={shopId}&productId={productId}";

    public static final String CONFIG_CONTENT_TYPE = "application/json";
}
