package sk.mek.shoppingapp;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.BuildConfig;
import com.example.shoppingapp.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import sk.mek.shoppingapp.controller.AccountManager;
import sk.mek.shoppingapp.controller.LocationController;
import sk.mek.shoppingapp.controller.modelmanager.CartItemManager;
import sk.mek.shoppingapp.model.entity.CartItem;
import sk.mek.shoppingapp.model.entity.Good;
import sk.mek.shoppingapp.model.entity.Product;
import sk.mek.shoppingapp.model.entity.Shop;
import sk.mek.shoppingapp.model.service.HTTPGetRequestTask;
import sk.mek.shoppingapp.model.service.HTTPResult;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import static java.net.HttpURLConnection.HTTP_OK;

public class QRCameraActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public  static final String CONFIG_PRODUCT_IMAGES_PATH = "drawable/products";

    private CartItemManager cartItemManager;

    private Integer found;
    private CartItem cartItem;

    private TextView productNameQR;
    private ImageView product_image_QR;
    private TextView productPriceQR;
    private TextView productQuantityQR;

    private Button payButton;
    private Button addToBasketButton;
    private Button decreaseButton;
    private Button increaseButton;

    private boolean firstStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_product_qr);

        productNameQR = findViewById(R.id.product_name_QR);
        productPriceQR = findViewById(R.id.product_price_QR);
        productQuantityQR = findViewById(R.id.product_quantity_QR);
        product_image_QR = findViewById(R.id.product_image_QR);

        payButton = findViewById(R.id.pay_QR);
        addToBasketButton = findViewById(R.id.add_to_basket_QR);
        decreaseButton = findViewById(R.id.decrease_quantity_QR);
        increaseButton = findViewById(R.id.increase_quantity_QR);

        increaseButton.setOnClickListener(new IncrementOnClickedListener());
        decreaseButton.setOnClickListener(new DecrementOnClickedListener());
        payButton.setOnClickListener(new BuyOnClickListener());
        addToBasketButton.setOnClickListener(new AddToBasketOnClickListener());

        productPriceQR.setVisibility(View.VISIBLE);

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
        Good good = null;
        CartItem cartItem = null;
        Product product = null;


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

                String nearestShopUrl = BuildConfig.CONFIG_SHOPS_NEAREST_API_PATH.replace("{longitude}", String.valueOf(current.getLongitude()));
                nearestShopUrl = nearestShopUrl.replace("{latitude}", String.valueOf(current.getLatitude()));

                HTTPGetRequestTask<Shop[]> nearestShopTask = new HTTPGetRequestTask(BuildConfig.CONFIG_SERVER_PATH + "/" + nearestShopUrl, Shop[].class, null);

                HTTPResult<Shop[]> nearestShopSet = null;
                try {
                    nearestShopSet = nearestShopTask.execute().get();

                    if (HTTP_OK == nearestShopSet.getResultCode()) {
                        nearestShop = nearestShopSet.getResultObject()[0];

                        product_info.put("shopId", String.valueOf(nearestShop.getId()));

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
                } catch (Exception e) {

                }

                String productUrl = BuildConfig.CONFIG_PRODUCT_BY_QR_API_PATH.replace("{qr}", String.valueOf(qrId));

                HTTPGetRequestTask<Product> productByQrTask = new HTTPGetRequestTask(BuildConfig.CONFIG_SERVER_PATH + "/" + productUrl, Product.class, null);

                try {
                    HTTPResult<Product> productResult = productByQrTask.execute().get();

                    if (productResult.getResultCode() == HTTP_OK) {
                        product = productResult.getResultObject();

                        product_info.put("name: ", product.getName());
                        product_info.put("id: ", String.valueOf(product.getId()));
                        product_info.put("category: ", product.getCategory());
                        product_info.put("image: ", product.getPicture());

                    } else {
                        Toast.makeText(this, getString(R.string.product_not_found), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                payButton.setVisibility(View.VISIBLE);
                if (nearestShop != null && product != null) {
                    String urlGood = BuildConfig.CONFIG_GOOD_BY_SHOP_AND_PRODUCT_API_PATH.replace("{shopId}", String.valueOf(nearestShop.getId()));
                    urlGood = urlGood.replace("{productId}", String.valueOf(product.getId()));

                    HTTPGetRequestTask<Good> goodTask = new HTTPGetRequestTask(BuildConfig.CONFIG_SERVER_PATH + "/" + nearestShopUrl, Good.class, null);

                    try {

                        HTTPResult<Good> goodResult = goodTask.execute().get();
                        good = goodResult.getResultObject();
                        product_info.put("price", String.valueOf(good.getPrice()));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    productPriceQR.setVisibility(View.VISIBLE);

                } else {
                    productPriceQR.setVisibility(View.INVISIBLE);
                    payButton.setVisibility(View.GONE);
                }


                if (product != null) {

                    String urlCart = BuildConfig.CONFIG_CART_BY_USER_ID_API_PATH.replace("{userId}", String.valueOf(AccountManager.loggedUser.getId()));

                    HTTPGetRequestTask<CartItem> cartItemHTTPGetRequestTask = new HTTPGetRequestTask(BuildConfig.CONFIG_SERVER_PATH + "/" + urlCart, CartItem.class, null);

                    try {
                        HTTPResult<CartItem> cartItemHTTPResult = cartItemHTTPGetRequestTask.execute().get();

                        if (cartItemHTTPResult.getResultCode() == HTTP_OK) {
                            cartItem = cartItemHTTPResult.getResultObject();
                            product_info.put("amount", String.valueOf(cartItem.getAmount()));
                        } else {
                            product_info.put("amount", "0");
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    product_info.put("amount", "0");
                }


                String id = product_info.get("id"); // id of product
                String name = product_info.get("name");
                String amount = product_info.get("amount");
                String price = product_info.get("price");
                String image = product_info.get("image");
                String shopId = product_info.get("shopId");

                found = Integer.valueOf(id);

                productNameQR.setText(name);

                if (image != null) {
                    Resources resources = getResources();
                    final int resourceId = resources.getIdentifier(image, CONFIG_PRODUCT_IMAGES_PATH,
                            getPackageName());
                    Drawable d = ContextCompat.getDrawable(this, resourceId);

                    product_image_QR.setImageDrawable(d);
                }




                if(price != null) {
                    productPriceQR.setText(price);
                } else {
                    productPriceQR.setVisibility(View.GONE);
                }

                if(shopId == null) {
                    payButton.setVisibility(View.GONE);
                }

                productQuantityQR.setText(amount);

            } else {
                found = null;


                Toast.makeText( this, getString(R.string.qr_not_valid), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    public class BuyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // TODO SEND remove cartItem from basket

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
            int amount = (Integer.valueOf(productQuantityQR.getText().toString()));
            productQuantityQR.setText(String.valueOf(amount+1));
        }
    }

    public class DecrementOnClickedListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int amount = (Integer.valueOf(productQuantityQR.getText().toString()));
            if(amount > 0) {
                productQuantityQR.setText(String.valueOf(amount - 1));
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

