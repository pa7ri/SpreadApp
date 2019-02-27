package com.ucm.informatica.spread.Presenter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Contracts.AlertContract;
import com.ucm.informatica.spread.Contracts.PosterContract;
import com.ucm.informatica.spread.Fragments.MapFragment;
import com.ucm.informatica.spread.Model.Event;
import com.ucm.informatica.spread.Model.LocationMode;
import com.ucm.informatica.spread.Model.PinMode;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.Model.Region;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.MapFragmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.ucm.informatica.spread.Model.LocationMode.Auto;
import static com.ucm.informatica.spread.Model.LocationMode.Manual;
import static com.ucm.informatica.spread.Utils.Constants.Map.POLYGON_LAYER;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER;

public class MapFragmentPresenter {

    private AlertContract alertContract;
    private PosterContract posterContract;

    private LocationMode currentMode = Auto; //default := Auto

    private MapFragmentView mapFragmentView;
    private MapFragment mapFragment;
    private String titleText, descriptionText;

    private PinMode pinMode;
    private Bitmap posterImage;

    public MapFragmentPresenter(MapFragmentView mapFragmentView, MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.mapFragmentView = mapFragmentView;
    }

    public void start(){
        List<Event> historicalEventList = ((MainTabActivity)mapFragment.getActivity()).getDataEventSmartContract();
        List<Poster> historicalPosterList = ((MainTabActivity)mapFragment.getActivity()).getDataPosterSmartContract();
        for (Event event:historicalEventList) {
            mapFragmentView.showNewMarkerIntoMap(
                    event.getLatitude(),
                    event.getLongitude(),
                    event.getTitle(),
                    event.getDescription(), true);
        }
        for (Event event:historicalPosterList) {
            mapFragmentView.showNewMarkerIntoMap(
                    event.getLatitude(),
                    event.getLongitude(),
                    event.getTitle(),
                    event.getDescription(), false);
        }
    }

    public void saveData(String title, String description, String longitude, String latitude) {
        switch (pinMode) {
            case Alert: {
                if(alertContract == null) {
                    alertContract = ((MainTabActivity) mapFragment.getActivity()).getAlertContract();
                }

                alertContract.addAlert(title,description,latitude,longitude, String.valueOf(System.currentTimeMillis())).observable()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (result) -> mapFragmentView.showFeedback()
                                ,
                                (error) -> mapFragmentView.showError(R.string.snackbar_alert_transaction)
                        );
            }
            case Poster: {
                if(posterContract == null) {
                    posterContract = ((MainTabActivity) mapFragment.getActivity()).getPosterContract();
                }

                //TODO : store data into IPFS and has into ethereum
            }
        }
    }

    public Map<Point, Region> getUpdatedContainedPointsInRegionMap(Point marker, Map<Point, Region> regionMap){
        Point nearestPoint = getMinDistancePoint(marker, regionMap);
        regionMap.get(nearestPoint).increaseContainedPoints();
        return regionMap;
    }

    public void onAddLocationButtonPressed(LatLng selectedLocation){
        mapFragmentView.showNewMarkerIntoMap( selectedLocation.getLatitude(),selectedLocation.getLongitude(),
                titleText, descriptionText, pinMode.equals(PinMode.Alert));

        saveData(titleText,descriptionText,
                Double.toString(selectedLocation.getLongitude()),
                Double.toString(selectedLocation.getLatitude()));
        onSwitchLocationMode();
    }

    public void getPolygonLayer(@NonNull Style style, Map<Point,Region> regionMap){
        List<List<Point>> singlePolygon;
        int i=0;
        for (Map.Entry<Point, Region> entry : regionMap.entrySet()) {
            singlePolygon = new ArrayList<>();
            singlePolygon.add(entry.getValue().getPolygonCoordinatesList());
            GeoJsonSource source = new GeoJsonSource(POLYGON_LAYER+i, Polygon.fromLngLats(singlePolygon));
            style.addSource(source);
            FillLayer polygonMadridLayer = new FillLayer(POLYGON_LAYER+i, POLYGON_LAYER+i);
            polygonMadridLayer.setProperties(
                    PropertyFactory.fillColor(getColorPolygon(entry.getValue().getCountContainedPoints())),
                    PropertyFactory.fillOpacity(.3f),
                    PropertyFactory.circleStrokeColor(Color.RED),
                    visibility(NONE));
            style.addLayer(polygonMadridLayer);
            i++;
        }
    }

    public void onSwitchLocationMode() {
        if(currentMode==Auto) currentMode = Manual;
        else currentMode = Auto;
        mapFragmentView.renderLocationView(currentMode);
    }


    private int getColorPolygon(int numContainedPoints){
        int color;
        if(numContainedPoints==0 || numContainedPoints==1) {
            color = Color.GREEN;
        } else if (numContainedPoints>1 && numContainedPoints<6) {
            color = Color.YELLOW;
        } else {
            color = Color.RED;
        }
        return color;
    }

    private Point getMinDistancePoint(Point marker, Map<Point, Region> regionMap) {
        Point nearestPoint = marker; //just to have an initial value, it doesnt mean anything
        Double minDistance = Double.MAX_VALUE;
        Double currentDistance;
        for (Map.Entry<Point, Region> entry : regionMap.entrySet())
        {
            currentDistance = getDistance(marker, entry.getKey());
            if(currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestPoint = entry.getKey();
            }
        }
        return nearestPoint;
    }

    //not needed to compute square root because we dont want distance itself, just proportion
    private Double getDistance(Point p1, Point p2){
        return (((p2.latitude() - p1.latitude())*(p2.latitude() - p1.latitude())) +
                    ((p2.longitude() - p1.longitude())*(p2.longitude() - p1.longitude())));
    }

    public void popUpDialog(PinMode pMode, String title, Bitmap image) {
        pinMode = pMode;
        posterImage = image;

        //Todo: make a class as dialog builder and organise it
        final AlertDialog dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(mapFragment.getContext())).create();
        LayoutInflater inflater = mapFragment.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_location, null);


        ImageView posterImageView = dialogView.findViewById(R.id.posterImage);
        FloatingActionButton cameraFloatingButton = dialogView.findViewById(R.id.addPosterButton);
        TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
        EditText infoTitleEditText = dialogView.findViewById(R.id.infoTitleEditText);
        EditText infoDescriptionEditText = dialogView.findViewById(R.id.infoDescriptionEditText);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button submitButton = dialogView.findViewById(R.id.storeButton);
        ToggleButton toggleButton = dialogView.findViewById(R.id.toggleButton);

        titleTextView.setText(title);

        switch (pinMode) {
            case Alert:
                posterImageView.setVisibility(View.GONE);
                cameraFloatingButton.setVisibility(View.GONE);
                break;
            case Poster:
                if(posterImage!=null){
                    posterImageView.setImageBitmap(posterImage);
                }
                posterImageView.setVisibility(View.VISIBLE);
                cameraFloatingButton.setVisibility(View.VISIBLE);
                break;
        }

        toggleButton.setOnClickListener(view -> {
            if(((ToggleButton) view).isChecked()) {
                currentMode = Auto;
            } else{
                currentMode = Manual;
            }
        });

        submitButton.setOnClickListener(view -> {
            titleText = infoTitleEditText.getText().toString().equals("")?
                    mapFragment.getResources().getString(R.string.no_text):infoTitleEditText.getText().toString();
            descriptionText = infoDescriptionEditText.getText().toString().equals("")?
                    mapFragment.getResources().getString(R.string.no_text):infoDescriptionEditText.getText().toString();

            if (currentMode == Auto) {
                Location latestLocation = ((MainTabActivity)mapFragment.getActivity()).getLocation();
                if (latestLocation != null) {
                    mapFragmentView.showNewMarkerIntoMap(latestLocation.getLatitude(),
                            latestLocation.getLongitude(), titleText, descriptionText, pMode.equals(PinMode.Alert));

                    saveData(titleText,descriptionText,
                            Double.toString(latestLocation.getLongitude()),
                            Double.toString(latestLocation.getLatitude()));
                } else {
                    mapFragmentView.showError(R.string.location_no_available);
                    onSwitchLocationMode();
                }
            } else {
                currentMode = Auto;
                onSwitchLocationMode();
            }
            dialogBuilder.dismiss();
        });
        cameraFloatingButton.setOnClickListener(view -> {
            ((MainTabActivity) mapFragment.getActivity()).createPictureIntentPicker(REQUEST_IMAGE_POSTER);
            dialogBuilder.dismiss();
        });
        cancelButton.setOnClickListener(view -> dialogBuilder.dismiss());

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}
