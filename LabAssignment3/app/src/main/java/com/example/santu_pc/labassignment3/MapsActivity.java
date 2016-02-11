package com.example.santu_pc.labassignment3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager PresentLocation;
    LocationListener PresentLocationListener;
    LatLng PresentLocationLatLng;
    Geocoder PresentLocationGeocoder;
    double PresentLocationLatitude = 0;
    double PresentLocationLongitude = 0;
    StringBuilder UserPresentAddress;
    //ImageView ProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        PresentLocationGeocoder = new Geocoder(this);
        PresentLocation = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        PresentLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        PresentLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, PresentLocationListener);
        //PresentLocationLatitude = PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        //PresentLocationLongitude = PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        PresentLocationLatLng = new LatLng(PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());

        //PresentLocationLatLng = new LatLng();

        try {

            List<Address> PresentLocationAddresses = PresentLocationGeocoder.getFromLocation(PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(),1);
            Address PresentLocationAddress = PresentLocationAddresses.get(0);
            UserPresentAddress =  new StringBuilder();

            for (int i = 0; i < PresentLocationAddress.getMaxAddressLineIndex(); i++) {

                UserPresentAddress.append(PresentLocationAddress.getAddressLine(i)).append("\t");

            }
            UserPresentAddress.append(PresentLocationAddress.getCountryName()).append("\t");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add a marker in Sydney and move the camera
        //LatLng currentLocation = new LatLng(39.027412, -94.575287);
        //mMap.setMyLocationEnabled(true);

        Intent intent = getIntent();
        Bitmap IBitmap = (Bitmap) intent.getParcelableExtra("PROFILEIMG");
        //ProfileImage.setImageBitmap(IBitmap);


        mMap.addMarker(new MarkerOptions().position(PresentLocationLatLng).title("you are here").snippet(UserPresentAddress.toString())).setIcon(BitmapDescriptorFactory.fromBitmap(IBitmap));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PresentLocationLatLng, 8));
    }
}
