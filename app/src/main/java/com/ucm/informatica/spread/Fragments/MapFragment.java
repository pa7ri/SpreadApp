package com.ucm.informatica.spread.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Model.LocationMode;
import com.ucm.informatica.spread.Model.PinMode;
import com.ucm.informatica.spread.Model.Region;
import com.ucm.informatica.spread.Presenter.MapFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.MapFragmentView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.ucm.informatica.spread.Model.LocationMode.Auto;
import static com.ucm.informatica.spread.Utils.Constants.Map.IMAGE_POSTER;
import static com.ucm.informatica.spread.Utils.Constants.Map.MAP_STYLE;
import static com.ucm.informatica.spread.Utils.Constants.Map.MAP_TOKEN;
import static com.ucm.informatica.spread.Utils.Constants.Map.POLYGON_LAYER;


import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.ucm.informatica.spread.Utils.Constants.Map.UPDATE_MAP;

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

    private Map<Point, Region> regionMap = new HashMap<>();

    private Boolean isUpdated = false;
    private byte[] imageByteArray;
    private Bitmap nuevoBitmap;


    public MapFragment() { }

    public static MapFragment newInstance(Boolean param1, byte[] param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putBoolean(UPDATE_MAP, param1);
        args.putByteArray(IMAGE_POSTER, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            isUpdated = getArguments().getBoolean(UPDATE_MAP);
            imageByteArray = getArguments().getByteArray(IMAGE_POSTER);
            nuevoBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        }
        Mapbox.getInstance(Objects.requireNonNull(getContext()), MAP_TOKEN);
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragmentPresenter = new MapFragmentPresenter(this,this);

        regionMap = ((MainTabActivity) Objects.requireNonNull(getActivity())).getPolygonData();
        
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

    private void initView(Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        initMapView();

        exitManualModeButton = view.findViewById(R.id.exitManualModeButton);
        addPinDial = view.findViewById(R.id.floatingDial);
        addLocationButton = view.findViewById(R.id.saveLocationButton);
        markerImage = view.findViewById(R.id.markerImage);
        switchLayerButton = view.findViewById(R.id.switchLayerButton);
        if(isUpdated) {
            isUpdated = false;
            mapFragmentPresenter.popUpDialog(PinMode.Poster, getString(R.string.button_add_pin_poster), imageByteArray);
        }
    }

    private void setupListeners() {
        addPinDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                String title;
                PinMode pinMode;
                if(menuItem.getTitle()==getString(R.string.menu_item_alert)){
                    title = getString(R.string.button_add_pin_alert);
                    pinMode = PinMode.Alert;
                } else {
                    title = getString(R.string.button_add_pin_poster);
                    pinMode = PinMode.Poster;
                }
                mapFragmentPresenter.popUpDialog(pinMode, title, null);
                return false;
            }
        });

        switchLayerButton.setOnClickListener(v -> togglePolygonLayer());

        addLocationButton.setOnClickListener(v -> {
            LatLng selectedLocation = mapboxMap.getProjection().fromScreenLocation(new PointF
                    (markerImage.getLeft() + (markerImage.getWidth()/2), markerImage.getBottom()));

            mapFragmentPresenter.onAddLocationButtonPressed(selectedLocation);
        });

        exitManualModeButton.setOnClickListener(v -> mapFragmentPresenter.onSwitchLocationMode());
    }

    private void initMapView() {
        mapView.getMapAsync(mp -> {
            mapboxMap = mp;
            mapboxMap.setStyle(MAP_STYLE, style -> mapFragmentPresenter.getPolygonLayer(style, regionMap));
            mapFragmentPresenter.start();
        });
    }

    private void togglePolygonLayer() {
        Layer polygonLayer = Objects.requireNonNull(mapboxMap.getStyle()).getLayer(POLYGON_LAYER+0);
        if (polygonLayer != null) {
            String visibility = VISIBLE.equals(polygonLayer.getVisibility().getValue()) ? NONE : VISIBLE;
            for (int i=0; i<regionMap.size(); i++) {
                polygonLayer = mapboxMap.getStyle().getLayer(POLYGON_LAYER+i);
                assert polygonLayer != null;
                polygonLayer.setProperties(visibility(visibility));
            }
        }
    }

    @Override
    public void showNewMarkerIntoMap(double latitude, double longitude, String markerTitle, String markerDescription, boolean isEvent){
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .title(markerTitle)
                .snippet(markerDescription));
        regionMap = mapFragmentPresenter.getUpdatedContainedPointsInRegionMap(Point.fromLngLat(longitude,latitude),regionMap);
    }


    @Override
    public void showFeedback(){
        ((MainTabActivity) getActivity()).getConfirmationSnackBar().show();
    }

    @Override
    public void showError(int text) {
        ((MainTabActivity) getActivity()).getErrorSnackBar(text).show();
    }

    @Override
    public void renderLocationView(LocationMode currentMode){
        if(currentMode==Auto) {
            exitManualModeButton.setVisibility(View.GONE);
            markerImage.setVisibility(View.GONE);
            addLocationButton.setVisibility(View.GONE);
            addPinDial.setVisibility(View.VISIBLE);
            switchLayerButton.setVisibility(View.VISIBLE);
        }
        else {
            exitManualModeButton.setVisibility(View.VISIBLE);
            markerImage.setVisibility(View.VISIBLE);
            addLocationButton.setVisibility(View.VISIBLE);
            addPinDial.setVisibility(View.GONE);
            switchLayerButton.setVisibility(View.GONE);
        }
    }

}
