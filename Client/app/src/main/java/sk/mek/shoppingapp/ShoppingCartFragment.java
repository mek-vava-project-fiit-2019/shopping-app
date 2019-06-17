package sk.mek.shoppingapp;

import android.content.Intent;
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

import com.example.shoppingapp.BuildConfig;
import com.example.shoppingapp.R;

import sk.mek.shoppingapp.controller.AccountManager;
import sk.mek.shoppingapp.controller.modelmanager.CartItemManager;
import sk.mek.shoppingapp.model.IAsyncResult;
import sk.mek.shoppingapp.model.entity.CartItem;
import sk.mek.shoppingapp.model.service.HTTPResult;
import sk.mek.shoppingapp.view.IDynamicGrid;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

public class ShoppingCartFragment extends Fragment implements IDynamicGrid, IAsyncResult {


    private LinearLayout productTableLayout;

    private CartItemManager cartItemManager;

    private FloatingActionButton findButton;


    @Override
    public void onStart() {
        super.onStart();

        productTableLayout = getActivity().findViewById(R.id.basketGridLayout);
        findButton = getActivity().findViewById(R.id.findShopButton);


        findButton.setOnClickListener(new FindOnClickListener());
        clearGrid();

        cartItemManager = new CartItemManager();

        System.out.println("SELECTING cart items of user: " + AccountManager.loggedUser.getId());
        String url = BuildConfig.CONFIG_CART_BY_USER_ID_API_PATH.replace("{userId}", String.valueOf(AccountManager.loggedUser.getId()));

        cartItemManager.getByArgs(this, BuildConfig.CONFIG_SERVER_PATH + "/" + url);

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


    public class FindOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),MainActivity.class);
            intent.putExtra("best" , true);
            startActivity(intent);
        }
    }
}
