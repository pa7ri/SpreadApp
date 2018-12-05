package com.ucm.informatica.spread.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private Button saveAutoLocationButton;
    private Button exitManualModeButton;
    private EditText infoTitleEditText;
    private EditText infoDescriptionEditText;

    enum LocationMode {Auto, Manual}
    private LocationMode currMode = Auto;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        initView(savedInstanceState);
        setUpListeners();
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
        saveAutoLocationButton.setEnabled(true);
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
        saveAutoLocationButton = findViewById(R.id.saveLocationButton);
        markerImage = findViewById(R.id.markerImage);
        infoTitleEditText = findViewById(R.id.infoTitleEditText);
        infoDescriptionEditText = findViewById(R.id.infoDescriptionEditText);

    }

    private void setUpListeners() {
        saveAutoLocationButton.setOnClickListener(v -> {
            String titleText = infoTitleEditText.getText().toString().equals("")?
                    getResources().getString(R.string.no_text):infoTitleEditText.getText().toString();
            String descriptionText = infoDescriptionEditText.getText().toString().equals("")?
                    getResources().getString(R.string.no_text):infoDescriptionEditText.getText().toString();

            if (currMode== Auto) {
                Location currLoc = getCurrentLocation();
                if (currLoc != null) {
                    addMarkerToMap(currLoc.getLatitude(),
                            currLoc.getLongitude(), titleText, descriptionText);
                    showFeedback(v, getResources().getString(R.string.location_saved));
                } else {
                    showFeedback(v, getResources().getString(R.string.location_no_available));
                    switchLocationMode();
                }
            }
            else { //Manual mode
                LatLng selectedLocation = mapboxMap.getProjection().fromScreenLocation(new PointF
                        (markerImage.getLeft() + (markerImage.getWidth()/2), markerImage.getBottom()));

                addMarkerToMap( selectedLocation.getLatitude(),selectedLocation.getLongitude(),
                         titleText, descriptionText);
                showFeedback(v, getResources().getString(R.string.location_saved));
                switchLocationMode();
            }
        });
        exitManualModeButton.setOnClickListener(v -> switchLocationMode());
    }

    private void switchLocationMode(){
        if(currMode==Auto) {
            currMode=Manual;
            exitManualModeButton.setVisibility(View.VISIBLE);
            markerImage.setVisibility(View.VISIBLE);
        }
        else {
            currMode=Auto;
            exitManualModeButton.setVisibility(View.GONE);
            markerImage.setVisibility(View.GONE);
        }
    }

    private Location getCurrentLocation() {
        Location bestLocation=null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            List<String> providers = locationManager.getProviders( true );
            for( String provider : providers){
                Location localLocation = locationManager.getLastKnownLocation( provider );
                if( localLocation != null && (bestLocation == null || localLocation.getAccuracy() < bestLocation.getAccuracy())){
                    bestLocation = localLocation;
                }
            }
        }
        return bestLocation;
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
}
