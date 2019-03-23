package com.ucm.informatica.spread.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.CustomLocationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.ucm.informatica.spread.Utils.Constants.Map.MAP_STYLE;
import static com.ucm.informatica.spread.Utils.Constants.Map.MAP_TOKEN;
import static com.ucm.informatica.spread.Utils.Constants.Map.ZOOM_MARKER;
import static com.ucm.informatica.spread.Utils.Constants.Notifications.*;

public class AlertDetailsActivity extends AppCompatActivity {

    private Button cancelButton;

    private TextView nameText;
    private TextView ageText;
    private TextView tshirtText;
    private TextView pantsText;
    private TextView watchwordKeyText;
    private TextView watchwordResponseText;

    private CustomLocationManager locationManager;

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(Objects.requireNonNull(this), MAP_TOKEN);

        setContentView(R.layout.activity_alert_details);

        locationManager = new CustomLocationManager(this);

        initView();
        mapView.onCreate(savedInstanceState);
        initViewContent();
        setUpListeners();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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

    public void initView(){
        mapView =findViewById(R.id.mapView);

        nameText = findViewById(R.id.dataNameDescription);
        ageText = findViewById(R.id.dataAgeDescription);
        tshirtText = findViewById(R.id.tshirtDescription);
        pantsText = findViewById(R.id.pantsDescription);
        watchwordKeyText = findViewById(R.id.watchwordKeyDescription);
        watchwordResponseText = findViewById(R.id.watchwordResponseDescription);

        cancelButton = findViewById(R.id.cancelButton);
    }

    public void initMap(Double latitude, Double longitude){
        mapView.getMapAsync(mp -> {
            mp.setStyle(MAP_STYLE);
            mp.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude,longitude))
                    .title(getResources().getString(R.string.button_help))
                    .snippet(getResources().getString(R.string.button_help_description)));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitude,longitude),ZOOM_MARKER);
            mp.animateCamera(cameraUpdate, 1000, null);
        });
    }

    public void initViewContent(){
        Intent intent = getIntent();
        String notificationMessageJsonObject = intent.getStringExtra(NOTIFICATION_MESSAGE);
        try {
            JSONObject notificationJsonObject = new JSONObject(notificationMessageJsonObject);
            String latitude = notificationJsonObject.getString(NOTIFICATION_DATA_LATITUDE);
            String longitude = notificationJsonObject.getString(NOTIFICATION_DATA_LONGITUDE);
            initMap(Double.valueOf(latitude), Double.valueOf(longitude));
            nameText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_NAME));
            ageText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_AGE));
            tshirtText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_TSHIRT_COLOR));
            pantsText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_PANTS_COLOR));
            watchwordKeyText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_WATCHWORD_KEY));
            watchwordResponseText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_WATCHWORD_RESPONSE));

        } catch (JSONException e) {
            Log.e("DATA NOTIFICATION", e.getMessage());
        }
    }

    public void setUpListeners(){
        cancelButton.setOnClickListener(v->onBackPressed());
       }
}
