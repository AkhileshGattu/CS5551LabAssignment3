package com.example.santu_pc.labassignment3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Login extends AppCompatActivity {

    ImageView img;
    int PHOTO_REQUEST_CODE = 1313;
    private GoogleMap mMap;
    LocationManager PresentLocation;
    LocationListener PresentLocationListener;
    LatLng PresentLocationLatLng;
    Geocoder PresentLocationGeocoder;
    double PresentLocationLatitude = 0;
    double PresentLocationLongitude = 0;
    StringBuilder UserPresentAddress;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void TakePhoto(View view) {

        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camIntent, PHOTO_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_REQUEST_CODE) {
            Bitmap Takenimg = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(Takenimg);
        }
    }

    public void ViewLocation(View view) {

        img.setDrawingCacheEnabled(true);
        Bitmap bm = img.getDrawingCache();
        Intent mapsIntent = new Intent(Login.this, MapsActivity.class);
        mapsIntent.putExtra("PROFILEIMG", bm);
        startActivity(mapsIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        img = (ImageView) findViewById(R.id.imageView);

        //mMap = googleMap;

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
        PresentLocationLatLng = new LatLng(PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());

        //PresentLocationLatLng = new LatLng();

        try {

            List<Address> PresentLocationAddresses = PresentLocationGeocoder.getFromLocation(PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), PresentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(), 1);
            Address PresentLocationAddress = PresentLocationAddresses.get(0);
            UserPresentAddress = new StringBuilder();

            for (int i = 0; i < PresentLocationAddress.getMaxAddressLineIndex(); i++) {

                UserPresentAddress.append(PresentLocationAddress.getAddressLine(i)).append("\t");

            }
            //UserPresentAddress.append(PresentLocationAddress.getCountryName()).append("\t");

            String addr = UserPresentAddress.toString();
            EditText addres = (EditText) findViewById(R.id.Address);
            addres.setText(addr);

            EditText address2 = (EditText) findViewById(R.id.Address2);
            address2.setText(PresentLocationAddress.getCountryName());
            //TextView address = (TextView) findViewById(R.id.Address);
            //address.setText(addr);

        } catch (IOException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.santu_pc.labassignment3/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.santu_pc.labassignment3/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
