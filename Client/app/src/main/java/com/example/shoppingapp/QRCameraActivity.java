package com.example.shoppingapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.example.shoppingapp.controller.LocationController;
import com.example.shoppingapp.controller.modelmanager.CartItemManager;
import com.example.shoppingapp.model.IAsyncResult;
import com.example.shoppingapp.model.entity.CartItem;
import com.example.shoppingapp.model.entity.Good;
import com.example.shoppingapp.model.entity.Product;
import com.example.shoppingapp.model.entity.Shop;
import com.example.shoppingapp.model.service.HTTPGetRequestTask;
import com.example.shoppingapp.model.service.HTTPResult;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import static com.example.shoppingapp.controller.Config.CONFIG_GOOD_BY_SHOP_AND_PRODUCT_API_PATH;
import static com.example.shoppingapp.controller.Config.CONFIG_PRODUCT_BY_QR_API_PATH;
import static com.example.shoppingapp.controller.Config.CONFIG_SERVER_PATH;
import static com.example.shoppingapp.controller.Config.CONFIG_SHOPS_NEAREST_API_PATH;
import static java.net.HttpURLConnection.HTTP_OK;

public class QRCameraActivity extends Activity  implements IAsyncResult<Product>{
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public  static final String CONFIG_PRODUCT_IMAGES_PATH = "drawable/products";



    private CartItemManager cartItemManager;

    private Integer found;
    private CartItem cartItem;

    private TextView product_name_QR;
    private ImageView product_image_QR;
    private TextView product_price_QR;
    private TextView product_quantity_QR;

    private Button payButton;
    private Button addToBasketButton;
    private Button decreaseButton;
    private Button increaseButton;

    private boolean firstStart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_product_qr);

        product_name_QR = findViewById(R.id.product_name_QR);
        product_price_QR = findViewById(R.id.product_price_QR);
        product_quantity_QR = findViewById(R.id.product_quantity_QR);
        product_image_QR = findViewById(R.id.product_image_QR);

        payButton = findViewById(R.id.pay_QR);
        addToBasketButton = findViewById(R.id.add_to_basket_QR);
        decreaseButton = findViewById(R.id.decrease_quantity_QR);
        increaseButton = findViewById(R.id.increase_quantity_QR);

        increaseButton.setOnClickListener(new IncrementOnClickedListener());
        decreaseButton.setOnClickListener(new DecrementOnClickedListener());
        payButton.setOnClickListener(new BuyOnClickListener());
        addToBasketButton.setOnClickListener(new AddToBasketOnClickListener());



        product_price_QR.setVisibility(View.VISIBLE);

        payButton.setVisibility(View.VISIBLE);

        found = null;
        firstStart = true;

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        HashMap<String,String> product_info = new HashMap();
        Shop nearestShop = null;
        Good good;
        Product product;


        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            BarcodeDetector detector = setupDetector();
            if(detector == null) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return;
            }
            SparseArray<Barcode> barcodes = detect(detector,photo);




            //No barcode were found
            if(barcodes.size() > 0) {
                //Get current location

                LocationController.loadLocationProvider(this);
                Location current = LocationController.getLastBestLocation(this);

                System.out.println(current.getLatitude() + " , " + current.getLongitude());

                String nearestShopUrl = CONFIG_SHOPS_NEAREST_API_PATH.replace("{longitude}", String.valueOf(current.getLongitude()));
                nearestShopUrl = nearestShopUrl.replace("{latitude}", String.valueOf(current.getLatitude()));

                HTTPGetRequestTask<Shop[]> nearestShopTask = new HTTPGetRequestTask(CONFIG_SERVER_PATH + "/" + nearestShopUrl, Shop[].class,null );

                HTTPResult<Shop[]> nearestShopSet = null;
                try {
                    nearestShopSet  = nearestShopTask.execute().get();

                    if(HTTP_OK == nearestShopSet.getResultCode()) {
                        nearestShop = nearestShopSet.getResultObject()[0];
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, barcodes.valueAt(0).displayValue, Toast.LENGTH_LONG).show();


                int qrId = 0;
                try {
                    qrId = Integer.valueOf(barcodes.valueAt(0).displayValue);
                } catch (Exception e){
                    Toast.makeText( this, getString(R.string.qr_not_valid), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                String productUrl = CONFIG_PRODUCT_BY_QR_API_PATH.replace("{qr}", String.valueOf(qrId));

                HTTPGetRequestTask<Product> productByQrTask = new HTTPGetRequestTask(CONFIG_SERVER_PATH + "/" + productUrl, Product.class,null );

                try {
                    HTTPResult<Product> productResult = productByQrTask.execute().get();

                    if(productResult.getResultCode() == HTTP_OK){
                        product = productResult.getResultObject();

                        System.out.println("name: " + product.getName());
                        System.out.println("id: " + product.getId());
                        System.out.println("category: " + product.getCategory());
                        System.out.println("image: " + product.getPicture());

                    } else {
                        Toast.makeText(this, getString(R.string.product_not_found), Toast.LENGTH_SHORT).show();
                        Toast.makeText( this, getString(R.string.qr_not_valid), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(nearestShop != null) {
                    String urlGood = CONFIG_GOOD_BY_SHOP_AND_PRODUCT_API_PATH.replace("shopId", String.valueOf(nearestShop.getId()));
                    urlGood = urlGood.replace("productId", String.valueOf(product.getId()));


                    HTTPGetRequestTask<Good> goodTask = new HTTPGetRequestTask(CONFIG_SERVER_PATH + "/" + nearestShopUrl, Good.class,null );

                    try {
                        HTTPResult<Good>  cartItemResult = goodTask.execute().get();

                        cartItem = cartItemResult.getResultObject();
                        product_info.put("amount","0");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }




                String id = product_info.get("id"); // id of product
                String name = product_info.get("name");
                String amount = product_info.get("amount");
                String price = product_info.get("price");
                String image = product_info.get("image");
                String shopId = product_info.get("shopId");

                found = Integer.valueOf(id);

                Resources resources = getResources();
                final int resourceId = resources.getIdentifier(image, CONFIG_PRODUCT_IMAGES_PATH,
                        getPackageName());
                Drawable d = ContextCompat.getDrawable(this, resourceId);

                product_name_QR.setText(name);
                product_image_QR.setImageDrawable(d);

                if(price != null) {
                    product_price_QR.setText(price);
                } else {
                    product_price_QR.setVisibility(View.GONE);
                }

                if(shopId == null) {
                    payButton.setVisibility(View.GONE);
                }

                product_quantity_QR.setText(amount);

            } else {
                found = null;


                Toast.makeText( this, getString(R.string.qr_not_valid), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void update(HTTPResult<Product> result) {
        //TODO make universal
    }


    public class BuyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            //HTTPDeleteRequestTask
            // TODO SEND remove cartItem from basket
            // TODO remove money from wallet maybe, probably not

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public class AddToBasketOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            CartItem cartItem = new CartItem(); //TODO load cartItem

            // TODO SEND post/put product to basket

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public class IncrementOnClickedListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int amount = (Integer.valueOf(product_quantity_QR.getText().toString()));
            product_quantity_QR.setText(String.valueOf(amount+1));
        }
    }

    public class DecrementOnClickedListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int amount = (Integer.valueOf(product_quantity_QR.getText().toString()));
            if(amount > 0) {
                product_quantity_QR.setText(String.valueOf(amount - 1));
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!firstStart && found == null) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private BarcodeDetector setupDetector(){
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        if(!detector.isOperational()){
            Toast.makeText(this, getString(R.string.detector_error), Toast.LENGTH_SHORT).show();
            return null;
        }

        return  detector;
    }

    private SparseArray<Barcode> detect(BarcodeDetector detector, Bitmap bitmap){
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        return barcodes;
    }


}

