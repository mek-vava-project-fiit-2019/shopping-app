package sk.mek.shoppingapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shoppingapp.BuildConfig;
import com.example.shoppingapp.R;

import java.io.File;

import sk.mek.shoppingapp.controller.modelmanager.CartItemManager;
import sk.mek.shoppingapp.model.entity.CartItem;
import sk.mek.shoppingapp.model.entity.Product;

import static sk.mek.shoppingapp.QRCameraActivity.CONFIG_PRODUCT_IMAGES_PATH;

public class DetailedProductView extends LinearLayout {

    private CartItem cartItem;

    private DetailType displayType;

    private ImageView productImageView;
    private TextView productDescriptionTextView;


    //visible only in shop
    private TextView productPriceTextView;


    //visible only in products or basket
    private LinearLayout basketOptionLayout;
    private EditText amountEditText;
    private Button incrementButton;
    private Button decrementButton;


    //visible only in products
    private Button saveBasketButton;


    public DetailedProductView(Context context) {
        super(context);

        View view = inflate(context, R.layout.fragment_product_detail, null);
        addView(view);

        productImageView = findViewById(R.id.product_image);
        productDescriptionTextView = findViewById(R.id.product_description);
        productPriceTextView = findViewById(R.id.product_price);
        basketOptionLayout = findViewById(R.id.basketOptionLayout);
        amountEditText = findViewById(R.id.amountEditText);
        incrementButton = findViewById(R.id.incrementButton);
        decrementButton = findViewById(R.id.decrementButton);
        saveBasketButton = findViewById(R.id.saveBasketButton);

        incrementButton.setOnClickListener(new IncreaseOnClickListener());
        decrementButton.setOnClickListener(new DecreaseOnClickListener());
        saveBasketButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.setAmount(Integer.valueOf(amountEditText.getText().toString()));
                CartItemManager cartItemManager = new CartItemManager();
                cartItemManager.put(null,BuildConfig.CONFIG_SERVER_PATH + "/" +  BuildConfig.CONFIG_PUT_CART_ITEM_API_PATH, 0, cartItem);


            }
        });
    }

    private void display(int id, String description, String imageUrl, int amount) {
        productDescriptionTextView.setText(description);

        if (imageUrl != null) {
            //example "/sdcard/Images/test_image.jpg"CONFIG_PRODUCT_IMAGES_PATH
            Uri fileUri = Uri.parse("android.resource://" + CONFIG_PRODUCT_IMAGES_PATH + "/" + imageUrl);
            File imgFile = new File(fileUri.getPath());


            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                productImageView.setImageBitmap(myBitmap);

            } else {
                productImageView.setVisibility(View.GONE);
            }
        }
        amountEditText.setText(String.valueOf(amount));

        productPriceTextView.setVisibility(View.GONE);

        cartItem = new CartItem();
        cartItem.setProduct(new Product());
        cartItem.getProduct().setId(id);
        cartItem.getProduct().setPicture(imageUrl);
        cartItem.getProduct().setName(description);
    }

    public void displayBasketDetail(CartItem cartItem) {
        displayType = DetailType.BasketView;


        this.cartItem = cartItem;


        display(cartItem.getId(), cartItem.getProduct().getName(), cartItem.getProduct().getPicture(), cartItem.getAmount());

    }


    public void displayProductsDetail(int id, String description, String imageUrl, int amount) {
        displayType = DetailType.ProductsView;

        saveBasketButton.setVisibility(View.VISIBLE);
        display(id, description, imageUrl, amount);

    }

    public CartItem getCartItem() {
        cartItem.setAmount(Integer.valueOf(amountEditText.getText().toString()));
        if (displayType == DetailType.BasketView) {
            cartItem.setId(null);
        }
        return cartItem;
    }

    public enum DetailType {
        BasketView, ProductsView, ShopView
    }

    public class IncreaseOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (Integer.valueOf(amountEditText.getText().toString()) >= 0) {
                amountEditText.setText(String.valueOf(Integer.valueOf(amountEditText.getText().toString()) + 1));
            }
        }
    }

    public class DecreaseOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (Integer.valueOf(amountEditText.getText().toString()) >= 0) {
                amountEditText.setText(String.valueOf(Integer.valueOf(amountEditText.getText().toString()) - 1));
            }
        }
    }


}
