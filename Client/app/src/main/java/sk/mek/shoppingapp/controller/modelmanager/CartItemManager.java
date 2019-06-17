package sk.mek.shoppingapp.controller.modelmanager;

import java.util.HashMap;

import sk.mek.shoppingapp.controller.AccountManager;
import sk.mek.shoppingapp.controller.viewmanager.TableManager;
import sk.mek.shoppingapp.model.entity.CartItem;
import sk.mek.shoppingapp.model.entity.Product;
import sk.mek.shoppingapp.model.service.HTTPManager;
import sk.mek.shoppingapp.view.DetailedProductView;
import sk.mek.shoppingapp.view.IDynamicGrid;
import sk.mek.shoppingapp.view.IDynamicGridLoader;

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
        bodyHashMap.put("user_id", String.valueOf(AccountManager.loggedUser.getId()));
        bodyHashMap.put("amount", String.valueOf(entity.getAmount()));
        bodyHashMap.put("product_id", String.valueOf(entity.getProduct().getId()));

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
