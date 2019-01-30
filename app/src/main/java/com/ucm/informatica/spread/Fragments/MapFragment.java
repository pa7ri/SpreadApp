package com.ucm.informatica.spread.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.LOCATION_SERVICE;
import static com.ucm.informatica.spread.Constants.Map.MAP_TOKEN;
import static com.ucm.informatica.spread.Fragments.MapFragment.LocationMode.Auto;
import static com.ucm.informatica.spread.Fragments.MapFragment.LocationMode.Manual;

public class MapFragment extends Fragment implements OnMapReadyCallback {

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

    public MapFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Mapbox.getInstance(getContext(), MAP_TOKEN);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initLocationManager();
        initView(view, savedInstanceState);
        setUpListeners();

        return view;
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(MapboxMap mp) {
        mapboxMap = mp;
    }
    private void initLocationManager() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new CustomLocationListener();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    private void initView(View v ,Bundle savedInstanceState) {
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        exitManualModeButton = v.findViewById(R.id.exitManualModeButton);
        addPinButton = v.findViewById(R.id.addLocationButton);
        addLocationButton = v.findViewById(R.id.saveLocationButton);
        markerImage = v.findViewById(R.id.markerImage);
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
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
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

    private class CustomLocationListener implements LocationListener {

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