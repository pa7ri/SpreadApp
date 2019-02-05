package com.ucm.informatica.spread.Fragments;

import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Presenter.MapFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.MapFragmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import static com.ucm.informatica.spread.Constants.Map.MAP_STYLE;
import static com.ucm.informatica.spread.Constants.Map.MAP_TOKEN;
import static com.ucm.informatica.spread.Constants.Map.POLYGON_LAYER;
import static com.ucm.informatica.spread.Fragments.MapFragment.LocationMode.Auto;
import static com.ucm.informatica.spread.Fragments.MapFragment.LocationMode.Manual;


import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class MapFragment extends Fragment implements MapFragmentView {

    private MapFragmentPresenter mapFragmentPresenter;

    private MapView mapView;
    private MapboxMap mapboxMap;

    private ImageView markerImage;
    private Button exitManualModeButton;
    private Button addLocationButton;
    private FloatingActionButton switchLayerButton;
    private FloatingActionButton addPinButton;

    @Override
    public void loadCoordinateFromContract(String title, String description, String latitude, String longitude) {
        addMarkerToMap(Double.valueOf(latitude),Double.valueOf(longitude),title,description);
    }

    @Override
    public void showErrorTransition() {
        Snackbar.make(Objects.requireNonNull(this.getView()), "Ha habido un error en la transicción", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    enum LocationMode {Auto, Manual}

    private LocationMode currMode = Auto; //default := Auto

    private List<List<Point>> polygonPointList = new ArrayList<>();

    private String titleText;
    private String descriptionText;

    public MapFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Mapbox.getInstance(Objects.requireNonNull(getContext()), MAP_TOKEN);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragmentPresenter = new MapFragmentPresenter(this,this);

        initView(view, savedInstanceState);
        setupListeners();
        mapFragmentPresenter.loadData();

        return view;
    }

    private void initMapView() {
        mapView.getMapAsync(mp -> {
            mapboxMap = mp;
            mapboxMap.setStyle(MAP_STYLE, this::addPolygonLayer);
        });
    }

    private void addPolygonLayer(@NonNull Style style){
        polygonPointList = ((MainTabActivity) Objects.requireNonNull(getActivity())).getPolygonPointList();
        List<List<Point>> sample;
        for (int i=0; i<polygonPointList.size(); i++) {
            sample = new ArrayList<>();
            sample.add(polygonPointList.get(i));
            GeoJsonSource source = new GeoJsonSource(POLYGON_LAYER+i, Polygon.fromLngLats(sample));
            style.addSource(source);
            FillLayer polygonMadridLayer = new FillLayer(POLYGON_LAYER+i, POLYGON_LAYER+i);
            polygonMadridLayer.setProperties(
                    PropertyFactory.fillColor(getColorPolygon()),
                    PropertyFactory.fillOpacity(.4f),
                    visibility(NONE));
            style.addLayer(polygonMadridLayer);
        }


    }

    private int getColorPolygon(){
        Random rand = new Random();
        switch (rand.nextInt(3)) {
            case 0 : return Color.GREEN;
            case 1 : return Color.YELLOW;
            default: return Color.RED;
        }
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void togglePolygonLayer() {
        Layer polygonLayer = Objects.requireNonNull(mapboxMap.getStyle()).getLayer(POLYGON_LAYER+0);
        if (polygonLayer != null) { //&& pointsLayer != null) {
            String visibility = VISIBLE.equals(polygonLayer.getVisibility().getValue()) ? NONE : VISIBLE;
            for (int i=0; i<polygonPointList.size(); i++) {
                polygonLayer = mapboxMap.getStyle().getLayer(POLYGON_LAYER+i);
                assert polygonLayer != null;
                polygonLayer.setProperties(visibility(visibility));
            }
        }
    }


    private void initView(View v ,Bundle savedInstanceState) {
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        initMapView();

        exitManualModeButton = v.findViewById(R.id.exitManualModeButton);
        addPinButton = v.findViewById(R.id.addLocationButton);
        addLocationButton = v.findViewById(R.id.saveLocationButton);
        markerImage = v.findViewById(R.id.markerImage);
        switchLayerButton = v.findViewById(R.id.switchLayerButton);
    }

    private void setupListeners() {
        addPinButton.setOnClickListener(this::popUpDialog);

        switchLayerButton.setOnClickListener(v -> togglePolygonLayer());

        addLocationButton.setOnClickListener(v -> {
            LatLng selectedLocation = mapboxMap.getProjection().fromScreenLocation(new PointF
                    (markerImage.getLeft() + (markerImage.getWidth()/2), markerImage.getBottom()));

            addMarkerToMap( selectedLocation.getLatitude(),selectedLocation.getLongitude(),
                    titleText, descriptionText);

            mapFragmentPresenter.saveData(titleText,descriptionText, Double.toString(selectedLocation.getLongitude()), Double.toString(selectedLocation.getLatitude()));

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
            switchLayerButton.setVisibility(View.GONE);
        }
        else {
            currMode=Auto;
            exitManualModeButton.setVisibility(View.GONE);
            markerImage.setVisibility(View.GONE);
            addLocationButton.setVisibility(View.GONE);
            addPinButton.setVisibility(View.VISIBLE);
            switchLayerButton.setVisibility(View.VISIBLE);
        }
    }

    private void addMarkerToMap(double latitude, double longitude, String markerTitle, String markerDescription){
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .title(markerTitle)
                .snippet(markerDescription));
        //TODO : Custom icon for marker
    }

    private void showFeedback(View v, String text){
        Snackbar.make(v, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void popUpDialog(View v){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext())).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_location, null);

        EditText infoTitleEditText = dialogView.findViewById(R.id.infoTitleEditText);
        EditText infoDescriptionEditText = dialogView.findViewById(R.id.infoDescriptionEditText);
        Button buttonCancel = dialogView.findViewById(R.id.cancelButton);
        Button buttonSubmit = dialogView.findViewById(R.id.storeButton);
        ToggleButton toggleButton = dialogView.findViewById(R.id.toggleButton);

        toggleButton.setOnClickListener(view -> {
            if(((ToggleButton) view).isChecked()) {
                currMode = Auto;
            } else{
                currMode = Manual;
            }
        });

        buttonSubmit.setOnClickListener(view -> {
            titleText = infoTitleEditText.getText().toString().equals("")?
                    getResources().getString(R.string.no_text):infoTitleEditText.getText().toString();
            descriptionText = infoDescriptionEditText.getText().toString().equals("")?
                    getResources().getString(R.string.no_text):infoDescriptionEditText.getText().toString();

            //try automatic geolocation
            if (currMode == Auto) {
                Location latestLocation = ((MainTabActivity) getActivity()).getLocation();
                if (latestLocation != null) {
                    addMarkerToMap(latestLocation.getLatitude(),
                            latestLocation.getLongitude(), titleText, descriptionText);

                    mapFragmentPresenter.saveData(titleText,descriptionText, Double.toString(latestLocation.getLongitude()), Double.toString(latestLocation.getLatitude()));

                    showFeedback(v, getResources().getString(R.string.location_saved));
                } else {
                    showFeedback(v, getResources().getString(R.string.location_no_available));
                    switchLocationMode();
                }
            } else {
                currMode = Auto;
                switchLocationMode();
            }
            dialogBuilder.dismiss();
        });
        buttonCancel.setOnClickListener(view -> dialogBuilder.dismiss());

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }



}
