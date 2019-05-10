package com.ucm.informatica.spread.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ImageView;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Model.Alert;
import com.ucm.informatica.spread.Model.LocationMode;
import com.ucm.informatica.spread.Model.PinMode;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.Model.Region;
import com.ucm.informatica.spread.Presenter.MapFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.FlashBarBuilder;
import com.ucm.informatica.spread.View.MapFragmentView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.ucm.informatica.spread.Model.LocationMode.Auto;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LATITUDE_END;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LATITUDE_START;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LONGITUDE_END;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LONGITUDE_START;
import static com.ucm.informatica.spread.Utils.Constants.Map.MAP_STYLE;
import static com.ucm.informatica.spread.Utils.Constants.Map.MAP_TOKEN;
import static com.ucm.informatica.spread.Utils.Constants.Map.POLYGON_LAYER;
import static com.ucm.informatica.spread.Utils.Constants.Map.ZOOM_MARKER;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER;

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

    private View cameraStartDelimit, cameraEndDelimit;

    private Map regionMap = new HashMap<>();


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
        mapFragmentPresenter = new MapFragmentPresenter(this);

        regionMap = ((MainTabActivity) Objects.requireNonNull(getActivity())).getPolygonData();
        
        initView(savedInstanceState);
        initMapView();
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
        cameraStartDelimit = view.findViewById(R.id.startCamera);
        cameraEndDelimit = view.findViewById(R.id.endCamera);
        mapView.onCreate(savedInstanceState);

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
                String title;
                PinMode pinMode;
                if(menuItem.getTitle()==getString(R.string.menu_item_alert)){
                    title = getString(R.string.button_add_pin_alert);
                    pinMode = PinMode.Alert;
                } else {
                    title = getString(R.string.button_add_pin_poster);
                    pinMode = PinMode.Poster;
                }
                mapFragmentPresenter.popUpDialog(pinMode, title, null
                        , getLayoutInflater(),new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create());
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
            mapboxMap.getUiSettings().setRotateGesturesEnabled(false);
            mapboxMap.addOnCameraMoveListener(() -> {
                SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                LatLng startCameraBoundLocation = mapboxMap.getProjection().fromScreenLocation(new PointF
                        (cameraStartDelimit.getLeft(), cameraStartDelimit.getBottom()));
                LatLng endCameraBoundLocation = mapboxMap.getProjection().fromScreenLocation(new PointF
                        (cameraEndDelimit.getRight() , cameraEndDelimit.getTop()));

                editor.putString(CAMERA_BOUND_LATITUDE_START, Double.toString(startCameraBoundLocation.getLatitude()));
                editor.putString(CAMERA_BOUND_LATITUDE_END,  Double.toString(endCameraBoundLocation.getLatitude()));
                editor.putString(CAMERA_BOUND_LONGITUDE_START,  Double.toString(startCameraBoundLocation.getLongitude()));
                editor.putString(CAMERA_BOUND_LONGITUDE_END,  Double.toString(endCameraBoundLocation.getLongitude()));

                editor.apply();

            });
            initRegionMap();
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
    public void showNewMarkerIntoMap(double latitude, double longitude, String markerTitle, String markerDescription, boolean isAlert){
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .title(markerTitle)
                .icon(isAlert?IconFactory.getInstance(Objects.requireNonNull(getActivity())).fromResource(R.drawable.ic_pin_alert):
                        IconFactory.getInstance(Objects.requireNonNull(getActivity())).fromResource(R.drawable.ic_pin_poster))
                .snippet(markerDescription));
        if(isAlert) {
            regionMap = mapFragmentPresenter.getUpdatedContainedPointsInRegionMap(Point.fromLngLat(longitude, latitude), regionMap);
        }
    }


    @Override
    public void saveDataAlert(String title, String description, String latitude, String longitude) {
        ((MainTabActivity) Objects.requireNonNull(getActivity())).saveDataAlert(title,description,latitude,longitude);
    }

    @Override
    public void saveDataPoster(String title, String description, String latitude, String longitude, byte[] image) {
        ((MainTabActivity) Objects.requireNonNull(getActivity())).saveDataPoster(title,
                description,latitude,longitude,image);
    }

    @Override
    public void buildPicturePicker() {
        ((MainTabActivity) Objects.requireNonNull(getActivity())).createPictureIntentPicker(REQUEST_IMAGE_POSTER);
    }

    @Override
    public List<Alert> getAlerts() {
        return ((MainTabActivity) Objects.requireNonNull(getActivity())).getDataAlertSmartContract();
    }

    @Override
    public List<Poster> getPosters() {
        return ((MainTabActivity) Objects.requireNonNull(getActivity())).getDataPosterSmartContract();
    }

    @Override
    public Location getLatestLocation() {
        return ((MainTabActivity) Objects.requireNonNull(getActivity())).getCustomLocationListener().getLatestLocation();
    }

    @Override
    public void showSendConfirmation(){
        new FlashBarBuilder(getActivity(), getString(R.string.snackbar_information_transaction)).getConfirmationSnackBar().show();
    }

    @Override
    public void showErrorTransaction(int text) {
        new FlashBarBuilder(getActivity()).getErrorSnackBar(text).show();
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

    public void initRegionMap() {
        regionMap = ((MainTabActivity) Objects.requireNonNull(getActivity())).getPolygonData();
        for(Map.Entry<Point, Region> entry : ((Map<Point, Region>)regionMap).entrySet()){
            entry.getValue().initContainedPoints();
        }
    }

    public void renderContentWithPicture(Bitmap imageBitmap){
        mapFragmentPresenter.popUpDialog(PinMode.Poster, getString(R.string.button_add_pin_poster),
                imageBitmap, getLayoutInflater(), new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create());
    }

    public void showSelectedLocation(Double latitude, Double longitude) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude,longitude),ZOOM_MARKER);
        mapboxMap.animateCamera(cameraUpdate, 1000, null);
    }
}
