package com.ucm.informatica.spread.Fragments;

import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.ucm.informatica.spread.Utils.Constants.Map.MAP_STYLE;
import static com.ucm.informatica.spread.Utils.Constants.Map.MAP_TOKEN;
import static com.ucm.informatica.spread.Utils.Constants.Map.POLYGON_LAYER;
import static com.ucm.informatica.spread.Fragments.MapFragment.LocationMode.Auto;
import static com.ucm.informatica.spread.Fragments.MapFragment.LocationMode.Manual;


import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class MapFragment extends Fragment implements MapFragmentView {

    private MapFragmentPresenter mapFragmentPresenter;

    private View view;
    private MapView mapView;
    private MapboxMap mapboxMap;

    private ImageView markerImage;
    private Button exitManualModeButton;
    private Button addLocationButton;
    private FloatingActionButton switchLayerButton;
    private FabSpeedDial addPinDial;

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
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragmentPresenter = new MapFragmentPresenter(this,this);

        initView(savedInstanceState);
        setupListeners();

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
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void togglePolygonLayer() {
        Layer polygonLayer = Objects.requireNonNull(mapboxMap.getStyle()).getLayer(POLYGON_LAYER+0);
        if (polygonLayer != null) {
            String visibility = VISIBLE.equals(polygonLayer.getVisibility().getValue()) ? NONE : VISIBLE;
            for (int i=0; i<polygonPointList.size(); i++) {
                polygonLayer = mapboxMap.getStyle().getLayer(POLYGON_LAYER+i);
                assert polygonLayer != null;
                polygonLayer.setProperties(visibility(visibility));
            }
        }
    }

    @Override
    public void loadCoordinateFromContract(String title, String description, Double latitude, Double longitude) {
        addMarkerToMap(latitude,longitude,title,description);
    }

    @Override
    public void showFeedback(){
        ((MainTabActivity) getActivity()).getConfirmationSnackBar().show();
    }

    @Override
    public void showError(int text) {
        ((MainTabActivity) getActivity()).getErrorSnackBar(text).show();
    }



    private void initView(Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        initMapView();

        exitManualModeButton = view.findViewById(R.id.exitManualModeButton);
        addPinDial = view.findViewById(R.id.floatingDial);
        addLocationButton = view.findViewById(R.id.saveLocationButton);
        markerImage = view.findViewById(R.id.markerImage);
        switchLayerButton = view.findViewById(R.id.switchLayerButton);
    }

    private void setupListeners() {
        addPinDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                popUpDialog();
                return false;
            }
        });

        switchLayerButton.setOnClickListener(v -> togglePolygonLayer());

        addLocationButton.setOnClickListener(v -> {
            LatLng selectedLocation = mapboxMap.getProjection().fromScreenLocation(new PointF
                    (markerImage.getLeft() + (markerImage.getWidth()/2), markerImage.getBottom()));

            addMarkerToMap( selectedLocation.getLatitude(),selectedLocation.getLongitude(),
                    titleText, descriptionText);

            mapFragmentPresenter.saveData(titleText,descriptionText, Double.toString(selectedLocation.getLongitude()), Double.toString(selectedLocation.getLatitude()));

            switchLocationMode();
        });

        exitManualModeButton.setOnClickListener(v -> switchLocationMode());
    }

    private void initMapView() {
        mapView.getMapAsync(mp -> {
            mapboxMap = mp;
            mapboxMap.setStyle(MAP_STYLE, this::addPolygonLayer);
            mapFragmentPresenter.start();
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
                    PropertyFactory.fillColor(mapFragmentPresenter.getColorPolygon()),
                    PropertyFactory.fillOpacity(.4f),
                    visibility(NONE));
            style.addLayer(polygonMadridLayer);
        }
    }

    private void switchLocationMode(){
        if(currMode==Auto) {
            currMode=Manual;
            exitManualModeButton.setVisibility(View.VISIBLE);
            markerImage.setVisibility(View.VISIBLE);
            addLocationButton.setVisibility(View.VISIBLE);
            addPinDial.setVisibility(View.GONE);
            switchLayerButton.setVisibility(View.GONE);
        }
        else {
            currMode=Auto;
            exitManualModeButton.setVisibility(View.GONE);
            markerImage.setVisibility(View.GONE);
            addLocationButton.setVisibility(View.GONE);
            addPinDial.setVisibility(View.VISIBLE);
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

    private void popUpDialog(){
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

            if (currMode == Auto) {
                Location latestLocation = ((MainTabActivity) getActivity()).getLocation();
                if (latestLocation != null) {
                    addMarkerToMap(latestLocation.getLatitude(),
                            latestLocation.getLongitude(), titleText, descriptionText);

                    mapFragmentPresenter.saveData(titleText,descriptionText, Double.toString(latestLocation.getLongitude()), Double.toString(latestLocation.getLatitude()));
                } else {
                    showError(R.string.location_no_available);
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
