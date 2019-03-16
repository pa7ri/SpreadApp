package com.ucm.informatica.spread.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ucm.informatica.spread.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.ucm.informatica.spread.Utils.Constants.Notifications.*;

public class AlertDetailsActivity extends AppCompatActivity {

    private Button cancelButton;
    private Button showLocationButton;


    private TextView placeText;
    private TextView nameText;
    private TextView ageText;
    private TextView tshirtText;
    private TextView pantsText;
    private TextView watchwordKeyText;
    private TextView watchwordResponseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_details);

        initView();
        initViewContent();
        setUpListeners();
    }

    public void initView(){
        placeText = findViewById(R.id.placeDescription);
        nameText = findViewById(R.id.dataNameDescription);
        ageText = findViewById(R.id.dataAgeDescription);
        tshirtText = findViewById(R.id.tshirtDescription);
        pantsText = findViewById(R.id.pantsDescription);
        watchwordKeyText = findViewById(R.id.watchwordKeyDescription);
        watchwordResponseText = findViewById(R.id.watchwordResponseDescription);

        cancelButton = findViewById(R.id.cancelButton);
        showLocationButton = findViewById(R.id.showLocationButton);
    }

    public void initViewContent(){
        Intent intent = getIntent();
        String notificationMessageJsonObject = intent.getStringExtra(NOTIFICATION_MESSAGE);
        try {
            JSONObject notificationJsonObject = new JSONObject(notificationMessageJsonObject);
            String latitude = notificationJsonObject.getString(NOTIFICATION_DATA_LATITUDE);
            String longitude = notificationJsonObject.getString(NOTIFICATION_DATA_LONGITUDE);
            placeText.setText(getAddress(Double.valueOf(latitude) , Double.valueOf(longitude)));
            nameText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_NAME));
            ageText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_AGE));
            tshirtText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_TSHIRT_COLOR));
            pantsText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_PANTS_COLOR));
            watchwordKeyText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_WATCHWORD_KEY));
            watchwordResponseText.setText(notificationJsonObject.getString(NOTIFICATION_DATA_WATCHWORD_RESPONSE));

        } catch (JSONException e) {
            Log.e("Data notification", e.getMessage());
        }
    }

    public void setUpListeners(){
        cancelButton.setOnClickListener(v->onBackPressed());
        showLocationButton.setOnClickListener(v->
                //TODO
                Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show());
    }

    private Address getLocation(Double latitude, Double longitude){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }
        return addresses.get(0);
    }

    private String getAddress(Double latitude, Double longitude) {
        Address address = getLocation(latitude, longitude);
        return address.getThoroughfare() + ", " + address.getSubThoroughfare() + "\n"
                + address.getLocality() + "\n"
                + address.getPostalCode() + " - " + address.getSubAdminArea() + "\n"
                + address.getCountryName();
    }
}
