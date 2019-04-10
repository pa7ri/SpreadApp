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
import com.ucm.informatica.spread.Model.Alert;
import com.ucm.informatica.spread.Model.LocationMode;
import com.ucm.informatica.spread.Model.PinMode;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.Model.Region;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.MapFragmentView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.ucm.informatica.spread.Model.LocationMode.Auto;
import static com.ucm.informatica.spread.Model.LocationMode.Manual;
import static com.ucm.informatica.spread.Utils.Constants.Map.POLYGON_LAYER;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER;

public class MapFragmentPresenter {

    private LocationMode currentMode = Auto;

    private MapFragmentView mapFragmentView;
    private String titleText, descriptionText;

    private PinMode pinMode;
    private Bitmap posterImage;

    public MapFragmentPresenter(MapFragmentView mapFragmentView) {
        this.mapFragmentView = mapFragmentView;
    }

    public void start(){
        List<Alert> historicalAlertList = mapFragmentView.getAlerts();
        List<Poster> historicalPosterList = mapFragmentView.getPosters();

        for (Alert alert:historicalAlertList) {
            if(alert.getLatitude()!=null && alert.getLongitude()!=null) {
                mapFragmentView.showNewMarkerIntoMap(
                        alert.getLatitude(),
                        alert.getLongitude(),
                        alert.getTitle(),
                        alert.getDescription(), true);
            }
        }
        for (Poster poster:historicalPosterList) {
            if(poster.getLatitude()!=null && poster.getLongitude()!=null) {
                mapFragmentView.showNewMarkerIntoMap(
                        poster.getLatitude(),
                        poster.getLongitude(),
                        poster.getTitle(),
                        poster.getDescription(), false);
            }
        }
    }

    public Map<Point, Region> getUpdatedContainedPointsInRegionMap(Point marker, Map<Point, Region> regionMap){
        Point nearestPoint = getMinDistancePoint(marker, regionMap);
        regionMap.get(nearestPoint).increaseContainedPoints();
        return regionMap;
    }

    public void onAddLocationButtonPressed(LatLng selectedLocation){
        mapFragmentView.showSendConfirmation();

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

    //not needed to compute square root because we dont want distance itself, just proportion
    private Double getDistance(Point p1, Point p2){
        return (((p2.latitude() - p1.latitude())*(p2.latitude() - p1.latitude())) +
                    ((p2.longitude() - p1.longitude())*(p2.longitude() - p1.longitude())));
    }

    public void popUpDialog(PinMode pMode, String title, Bitmap image, LayoutInflater inflater, AlertDialog dialogBuilder) {
        pinMode = pMode;
        posterImage = image;

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
            titleText = infoTitleEditText.getText().toString().isEmpty()? "None"
                    :infoTitleEditText.getText().toString();
            descriptionText = infoDescriptionEditText.getText().toString().isEmpty()? "None"
                    :infoDescriptionEditText.getText().toString();

            if (currentMode == Auto) {
                Location latestLocation = mapFragmentView.getLatestLocation();
                if (latestLocation != null) {
                    mapFragmentView.showSendConfirmation();

                    mapFragmentView.showNewMarkerIntoMap(latestLocation.getLatitude(),
                            latestLocation.getLongitude(), titleText, descriptionText, pMode.equals(PinMode.Alert));

                    saveData(titleText,descriptionText,
                            Double.toString(latestLocation.getLongitude()),
                            Double.toString(latestLocation.getLatitude()));

                } else {
                    mapFragmentView.showErrorTransaction(R.string.location_no_available);
                    onSwitchLocationMode();
                }
            } else {
                currentMode = Auto;
                onSwitchLocationMode();
            }
            dialogBuilder.dismiss();
        });
        cameraFloatingButton.setOnClickListener(view -> {
            mapFragmentView.buildPicturePicker(REQUEST_IMAGE_POSTER);
            dialogBuilder.dismiss();
        });
        cancelButton.setOnClickListener(view -> dialogBuilder.dismiss());

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void saveData(String title, String description, String longitude, String latitude) {
        switch (pinMode) {
            case Alert: {
                mapFragmentView.saveDataAlert(title,description,latitude,longitude);
                break;
            }
            case Poster: {
                mapFragmentView.saveDataPoster(title, description,  latitude,
                        longitude, bitmapToByteArray(posterImage));
                break;
            }
        }
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

    private byte[] bitmapToByteArray(Bitmap posterImage){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        posterImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
