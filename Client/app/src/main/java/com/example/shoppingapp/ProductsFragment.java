package com.example.shoppingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.shoppingapp.model.IAsyncResult;
import com.example.shoppingapp.controller.modelmanager.ProductManager;
import com.example.shoppingapp.model.entity.Product;
import com.example.shoppingapp.model.service.HTTPResult;
import com.example.shoppingapp.view.IDynamicGrid;

import static com.example.shoppingapp.controller.Config.CONFIG_PRODUCT_BY_CATEGORY_API_PATH;
import static com.example.shoppingapp.controller.Config.CONFIG_SERVER_PATH;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

public class ProductsFragment extends Fragment implements IDynamicGrid, BottomNavigationView.OnNavigationItemSelectedListener, IAsyncResult<Product[]> {

    private LinearLayout productTableLayout;

    private ProductManager productManager;

    public static final String CONFIG_CATEGORY_FOOD = "food";
    public static final String CONFIG_CATEGORY_CLOTHES = "clothes";
    public static final String CONFIG_CATEGORY_ELECTRONICS = "electronics";


    @Override
    public void onStart() {
        super.onStart();

        productTableLayout = getActivity().findViewById(R.id.basketGridLayout);

        clearGrid();

        productManager = new ProductManager();

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        System.out.println("SELECTING products of category: food");
        String url = CONFIG_PRODUCT_BY_CATEGORY_API_PATH.replace("{category}", "food");

        productManager.getByArgs(this,CONFIG_SERVER_PATH + "/" + url);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    public void clearGrid(){
        productTableLayout.removeAllViews();
    }

    public void addRow(View row){

        productTableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        String category = "";
        switch (menuItem.getItemId()) {
            case R.id.nav_food:
                // nacitanie potravin
                category = CONFIG_CATEGORY_FOOD;

                break;
            case R.id.nav_clothing:
                //nacitanie oblecenia

                category = CONFIG_CATEGORY_CLOTHES;
                break;
            case R.id.nav_electronics:
                //nacitanie ostatnych produktov
                category = CONFIG_CATEGORY_ELECTRONICS;
                break;
        }


        System.out.println("SELECTING products of category: " + category);
        String url = CONFIG_PRODUCT_BY_CATEGORY_API_PATH.replace("{category}",category);

        productManager.getByArgs(this,CONFIG_SERVER_PATH + "/" + url);


        return true;
    }

    @Override
    public void update(HTTPResult<Product[]> result) {
        Toast toast;
        String resultMessage = "";

        if(result != null) {
            switch (result.getResultCode()) {
                case HTTP_OK:
                    clearGrid();
                    Product[] products =  result.getResultObject();
                    resultMessage = products.length + getString(R.string.product_were_loaded);

                    if (products.length != 0) {
                        resultMessage += " " + getString(R.string.category) + " " + products[0].getCategory();
                    }
                    productManager.display(products, this);
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
        } else {
            resultMessage = getString(R.string.http_not_found);
        }

        toast = Toast.makeText(getActivity(), resultMessage, Toast.LENGTH_SHORT);
        toast.show();

    }
}
