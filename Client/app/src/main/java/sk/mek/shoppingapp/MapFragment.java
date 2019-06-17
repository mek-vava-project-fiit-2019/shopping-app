package sk.mek.shoppingapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppingapp.BuildConfig;
import com.example.shoppingapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import sk.mek.shoppingapp.controller.modelmanager.ShopManager;
import sk.mek.shoppingapp.model.IAsyncResult;
import sk.mek.shoppingapp.model.entity.Shop;
import sk.mek.shoppingapp.model.service.HTTPGetRequestTask;
import sk.mek.shoppingapp.model.service.HTTPResult;

import static java.net.HttpURLConnection.HTTP_OK;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, IAsyncResult<Shop[]> {

    private MapView mMapView;
    private GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LocationManager locationManager;

    private static final int MY_PERMISSION_REQUEST_READ_FINE_LOCATION = 111;
    private LatLng myPosition;


    private ArrayList<Marker> markerList;

    private boolean locationPermissionGranted;
    private ShopManager shopManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Bundle mapViewBundle = null;
        markerList = new ArrayList();

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = view.findViewById(R.id.shops_map);
        mMapView.onCreate(mapViewBundle);


        mMapView.getMapAsync(this);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        shopManager = new ShopManager();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();


        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(mMap != null){

            HTTPGetRequestTask<Shop[]> getShopTask = new HTTPGetRequestTask(BuildConfig.CONFIG_SERVER_PATH + "/" + BuildConfig.CONFIG_SHOPS_API_PATH, Shop[].class,null);

            try {
                HTTPResult<Shop[]> shopsResult = getShopTask.execute().get();
                if(shopsResult.getResultCode() == HTTP_OK){

                    if(shopsResult.getResultObject() != null ) {
                        for (int i = 0; i < shopsResult.getResultObject().length; i++) {
                            addMarker(shopsResult.getResultObject()[i].getLatitude(),shopsResult.getResultObject()[i].getLongitude(),shopsResult.getResultObject()[i].getName());
                        }


                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1000, this);

            getDeviceLocation();
        }
    }


    public void addMarker(BigDecimal latitude, BigDecimal longitude, String name) {
        if (mMap != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(longitude.doubleValue(),latitude.doubleValue())).title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .draggable(false).visible(true));
            markerList.add(marker);
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location location = task.getResult();
                            LatLng currentLatLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            myPosition = currentLatLng;

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(location.getLatitude(), location.getLongitude()), 1));
                        }
                    }
                });


            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void moveCamera(double longitude, double latitude){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 16));
    }

    @Override
    public void onLocationChanged(Location location) {

        //find nearest shop


        String url =  BuildConfig.CONFIG_SERVER_PATH + "/" + BuildConfig.CONFIG_SHOPS_NEAREST_API_PATH;
        url = url.replace("{longitude}",String.valueOf(location.getLongitude()));
        url = url.replace("{latitude}",String.valueOf(location.getLatitude()));

        shopManager.getByArgs(this, url);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapView.getMapAsync(this);
                    System.out.println("granted");

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void update(HTTPResult<Shop[]> result) {

        if(result != null && result.getResultCode() == HTTP_OK) {
            Shop shop = result.getResultObject()[0];
            moveCamera(shop.getLatitude().doubleValue(),shop.getLongitude().doubleValue());
        }

    }
}
