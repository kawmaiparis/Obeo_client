package com.example.obeo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ClientServer.ClientRequests.ObeoClient;
import user.ObeoUser;

public class SelectLocationScreen extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final String TAG = SelectLocationScreen.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_screen);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String caller = getIntent().getStringExtra("caller");
        String language = getIntent().getStringExtra("language");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Geocoder geocoder = new Geocoder(this,new Locale("English"));

        if (caller.equals("CreateHolidayScreen")) {
            new AlertDialog.Builder(this).setTitle("Address")
                    .setMessage("Add an address close to your holiday destination.  " +
                            "Don't worry, no-one else will be able to see it")
                    .setNegativeButton(android.R.string.ok, null)
                    .show();
        } else {
            new AlertDialog.Builder(this).setTitle("Address")
                    .setMessage("Add an address close to your home.  " +
                            "Don't worry, no-one else will be able to see it")
                    .setNegativeButton(android.R.string.ok, null)
                    .show();
        }

        final Address[] address = new Address[1];

        final EditText userAddress = findViewById(R.id.enterAddressEditText);
        Button confirmButton = findViewById(R.id.confirmButton);
//        Button enterButton = findViewById(R.id.enterButton);

        userAddress.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {

                String addressString = userAddress.getText().toString();
                try {
                    mMap.clear();
                    address[0] = geocoder.getFromLocationName(addressString, 1).get(0);
                    LatLng enteredAddress = new LatLng(address[0].getLatitude(), address[0].getLongitude());
                    mMap.addMarker(new MarkerOptions().position(enteredAddress));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(enteredAddress));
                    return true;
                } catch (Exception e) {
                    Toast.makeText(SelectLocationScreen.this,
                            "Invalid address", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return false;
        });

//        enterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String addressString = userAddress.getText().toString();
//                try {
//                    mMap.clear();
//                    address[0] = geocoder.getFromLocationName(addressString, 1).get(0);
//                    LatLng enteredAddress = new LatLng(address[0].getLatitude(), address[0].getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(enteredAddress));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(enteredAddress));
//                } catch (IOException e) {
//                    Toast.makeText(SelectLocationScreen.this,
//                            "Invalid address", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        if (caller.equals("CreateAccountScreen")) {
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locality = address[0].getLocality();
                    String subAdmin = address[0].getSubAdminArea();
                    String admin = address[0].getAdminArea();
                    String city;
                    if (locality != null) {
                        city = locality;
                    } else if (subAdmin != null) {
                        city = subAdmin;
                    } else if (admin != null) {
                        city = admin;
                    } else {
                        Toast.makeText(SelectLocationScreen.this,
                                "Address not supported", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (city.equals("東京都")) {
                        city = "Tokyo";
                    } else if (city.equals("กรุงเทพมหานคร")) {
                        city = "Bangkok";
                    } else if (city.equals("Москва")) {
                        city = "Moscow";
                    }

                    ObeoClient.getInstance().createUserWithLocation(city, address[0].getLongitude(),
                            address[0].getLatitude());
                    ObeoClient.getInstance().createLanguage(language, 5);

                    openHomeScreen();

                }
            });
        } else if (caller.equals("UpdateProfileScreen")){

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locality = address[0].getLocality();
                    String subAdmin = address[0].getSubAdminArea();
                    String admin = address[0].getAdminArea();
                    String city;
                    if (locality != null) {
                        city = locality;
                    } else if (subAdmin != null) {
                        city = subAdmin;
                    } else if (admin != null) {
                        city = admin;
                    } else {
                        Toast.makeText(SelectLocationScreen.this,
                                "Address not supported", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (city.equals("東京都")) {
                        city = "Tokyo";
                    } else if (city.equals("กรุงเทพมหานคร")) {
                        city = "Bangkok";
                    } else if (city.equals("Москва")) {
                        city = "Moscow";
                    }

                    ObeoClient.getInstance().updateUserWithLocation(city, address[0].getLongitude(),
                            address[0].getLatitude());
                    openHomeScreen();
                }
            });
        } else if (caller.equals("CreateHolidayScreen")) {
            confirmButton.setOnClickListener(v -> {
                CreateHolidayScreen.setAddress(address[0]);
                openCreateHolidayScreen(address[0]);
            });
        }

    }

    private void openHomeScreen() {
        Intent intent = new Intent(this, UpdateProfileScreen.class);
        startActivity(intent);
    }

    private void openCreateHolidayScreen(Address address) {
        Intent intent = new Intent(this, CreateHolidayScreen.class);
        intent.putExtra("caller", "SelectLocationScreen");
        startActivity(intent);
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
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
