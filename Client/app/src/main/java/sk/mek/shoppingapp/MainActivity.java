package sk.mek.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shoppingapp.BuildConfig;
import com.example.shoppingapp.R;

import java.text.DecimalFormat;

import sk.mek.shoppingapp.controller.AccountManager;
import sk.mek.shoppingapp.model.IAsyncResult;
import sk.mek.shoppingapp.model.entity.Shop;
import sk.mek.shoppingapp.model.service.HTTPGetRequestTask;
import sk.mek.shoppingapp.model.service.HTTPResult;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAsyncResult<Shop> {

    private DrawerLayout drawer;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_map);
        }


        TextView usernameTextView = navigationView.getHeaderView(0).findViewById(R.id.usernameHeaderTextView);
        TextView emailTextView = navigationView.getHeaderView(0).findViewById(R.id.emailHeaderTextView);

if(AccountManager.loggedUser != null) {
    usernameTextView.setText(AccountManager.loggedUser.getName());
    emailTextView.setText(AccountManager.loggedUser.getEmail());
}

        boolean find = getIntent().getBooleanExtra("best",false);

        if(find){
            String url = BuildConfig.CONFIG_SERVER_PATH + "/" + BuildConfig.CONFIG_BEST_SHOP_API_PATH.replace("{userId}", String.valueOf(AccountManager.loggedUser.getId()));
            System.out.println("Getting best case: " + url);
            HTTPGetRequestTask<Shop> findBestShop = new HTTPGetRequestTask(url,Shop.class,this);
            findBestShop.execute();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapFragment()).commit();
                break;
            case R.id.nav_products:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProductsFragment()).commit();
                break;
            case R.id.nav_shopping_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ShoppingCartFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();


                break;
            case R.id.nav_qr:


                Intent qrScannerIntent = new Intent(this, QRCameraActivity.class);
                startActivity(qrScannerIntent);


                break;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void update(HTTPResult<Shop> result) {

    }
}
