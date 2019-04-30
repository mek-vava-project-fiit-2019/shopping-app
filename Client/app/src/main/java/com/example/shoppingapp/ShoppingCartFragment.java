package com.example.shoppingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.shoppingapp.controller.AccountManager;
import com.example.shoppingapp.controller.modelmanager.CartItemManager;
import com.example.shoppingapp.controller.viewmanager.TableManager;
import com.example.shoppingapp.model.IAsyncResult;
import com.example.shoppingapp.model.entity.CartItem;
import com.example.shoppingapp.model.entity.Shop;
import com.example.shoppingapp.model.service.HTTPResult;
import com.example.shoppingapp.view.DetailedProductView;
import com.example.shoppingapp.view.IDynamicGrid;

import java.util.ArrayList;

import static com.example.shoppingapp.controller.Config.CONFIG_CART_BY_USER_ID_API_PATH;
import static com.example.shoppingapp.controller.Config.CONFIG_PRODUCT_BY_CATEGORY_API_PATH;
import static com.example.shoppingapp.controller.Config.CONFIG_SERVER_PATH;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

public class ShoppingCartFragment extends Fragment implements IDynamicGrid, IAsyncResult {


    private LinearLayout productTableLayout;

    private CartItemManager cartItemManager;

    private FloatingActionButton saveButton;
    private FloatingActionButton findButton;


    @Override
    public void onStart() {
        super.onStart();

        productTableLayout = getActivity().findViewById(R.id.basketGridLayout);
        saveButton = getActivity().findViewById(R.id.saveBasketButton);
        findButton = getActivity().findViewById(R.id.findShopButton);

        clearGrid();

        cartItemManager = new CartItemManager();

        System.out.println("SELECTING cart items of user: " + AccountManager.loggedUser.getId());
        String url = CONFIG_CART_BY_USER_ID_API_PATH.replace("{userId}", String.valueOf(AccountManager.loggedUser.getId()));

        cartItemManager.getByArgs(this,CONFIG_SERVER_PATH + "/" + url);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    public void clearGrid(){
        productTableLayout.removeAllViews();
    }

    public void addRow(View row){

        productTableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * getById response from find shop task, best shops based on your basket
     * @param result
     */
    @Override
    public void update(HTTPResult result) {
        Toast toast;
        String resultMessage = "";
        switch (result.getResultCode()) {
            case HTTP_OK:
                clearGrid();
                CartItem[] cartItems = (CartItem[]) result.getResultObject();
                resultMessage = cartItems.length + getString(R.string.product_were_loaded);

                resultMessage = getString(R.string.category);

                cartItemManager.display(cartItems, this);
                break;
            case HTTP_NOT_FOUND:
                resultMessage = getString(R.string.http_not_found);
                break;

            case HTTP_BAD_REQUEST:
                resultMessage = getString(R.string.http_bad_request);
                break;
            case HTTP_NO_CONTENT:
                resultMessage = getString(R.string.http_no_content);
                break;
        }
        toast = Toast.makeText(getActivity(), resultMessage, Toast.LENGTH_SHORT);
        toast.show();
    }


    public class SaveOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            CartItem[] cartItems = TableManager.readTable(productTableLayout);


            for (CartItem item:cartItems) {
                cartItemManager.put(ShoppingCartFragment.this, CONFIG_SERVER_PATH, item.getId(), item);
            }
            //TODO send to backend update amount or delete if amount = 0;
        }
    }

    public class FindOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //TODO find best shops for you based on your basket
        }
    }
}
