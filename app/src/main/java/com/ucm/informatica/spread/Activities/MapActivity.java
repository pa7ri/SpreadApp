package com.ucm.informatica.spread.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.ucm.informatica.spread.R;

import java.util.List;


import static com.ucm.informatica.spread.Activities.MapActivity.LocationMode.Auto;
import static com.ucm.informatica.spread.Activities.MapActivity.LocationMode.Manual;
import static com.ucm.informatica.spread.Constants.Map.MAP_TOKEN;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //TODO : MVP design pattern
    //TODO : store data into BC
    private MapView mapView;
    private MapboxMap mapboxMap;


    private ImageView markerImage;
    private Button exitManualModeButton;
    private Button addLocationButton;
    private FloatingActionButton addPinButton;

    enum LocationMode {Auto, Manual}

    private LocationMode currMode = Auto; //default := Auto

    private Location latestLocation;

    private String titleText;
    private String descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocationManager();
        initView(savedInstanceState);
        setUpListeners();
    }

    private void initLocationManager() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new CustomLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(MapboxMap mMap) {
        mapboxMap = mMap;
        //TODO : add previous stored markers
    }

    private void instanceMap(Bundle savedInstanceState) {
        Mapbox.getInstance(this, MAP_TOKEN);
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void initView(Bundle savedInstanceState) {
        instanceMap(savedInstanceState);
        exitManualModeButton = findViewById(R.id.exitManualModeButton);
        addPinButton = findViewById(R.id.addLocationButton);
        addLocationButton = findViewById(R.id.saveLocationButton);
        markerImage = findViewById(R.id.markerImage);
    }

    private void setUpListeners() {

        addPinButton.setOnClickListener(this::popUpDialog);

        addLocationButton.setOnClickListener(v -> {
            LatLng selectedLocation = mapboxMap.getProjection().fromScreenLocation(new PointF
                    (markerImage.getLeft() + (markerImage.getWidth()/2), markerImage.getBottom()));

            addMarkerToMap( selectedLocation.getLatitude(),selectedLocation.getLongitude(),
                    titleText, descriptionText);
            showFeedback(v, getResources().getString(R.string.location_saved));
            switchLocationMode();
        });

        exitManualModeButton.setOnClickListener(v -> switchLocationMode());
    }

    private void switchLocationMode(){
        if(currMode==Auto) {
            currMode=Manual;
            exitManualModeButton.setVisibility(View.VISIBLE);
            markerImage.setVisibility(View.VISIBLE);
            addLocationButton.setVisibility(View.VISIBLE);
            addPinButton.setVisibility(View.GONE);
        }
        else {
            currMode=Auto;
            exitManualModeButton.setVisibility(View.GONE);
            markerImage.setVisibility(View.GONE);
            addLocationButton.setVisibility(View.GONE);
            addPinButton.setVisibility(View.VISIBLE);
        }
    }

    private void addMarkerToMap(double latitude, double longitude, String markerTitle, String markerDescription){
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .title(markerTitle)
                .snippet(markerDescription));
    }

    private void showFeedback(View v, String text){
        Snackbar.make(v, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void popUpDialog(View v){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_location, null);

        EditText infoTitleEditText = dialogView.findViewById(R.id.infoTitleEditText);
        EditText infoDescriptionEditText = dialogView.findViewById(R.id.infoDescriptionEditText);
        Button buttonCancel = dialogView.findViewById(R.id.cancelButton);
        Button buttonSubmit = dialogView.findViewById(R.id.storeButton);

        buttonSubmit.setOnClickListener(view -> {
            titleText = infoTitleEditText.getText().toString().equals("")?
                    getResources().getString(R.string.no_text):infoTitleEditText.getText().toString();
            descriptionText = infoDescriptionEditText.getText().toString().equals("")?
                    getResources().getString(R.string.no_text):infoDescriptionEditText.getText().toString();

            //try automatic geolocation
            if (currMode == Auto) {
                if (latestLocation != null) {
                    addMarkerToMap(latestLocation.getLatitude(),
                            latestLocation.getLongitude(), titleText, descriptionText);
                    showFeedback(v, getResources().getString(R.string.location_saved));
                } else {
                    showFeedback(v, getResources().getString(R.string.location_no_available));
                    switchLocationMode();
                }
            }
            dialogBuilder.dismiss();
        });
        buttonCancel.setOnClickListener(view -> dialogBuilder.dismiss());

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private class CustomLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            latestLocation = location;
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
    }
}
