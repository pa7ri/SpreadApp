package com.ucm.informatica.spread.View;

import android.location.Location;

import com.ucm.informatica.spread.Model.Alert;
import com.ucm.informatica.spread.Model.LocationMode;
import com.ucm.informatica.spread.Model.Poster;

import java.util.List;

public interface MapFragmentView {

    void renderLocationView(LocationMode state);

    void showNewMarkerIntoMap(double latitude, double longitude, String markerTitle, String markerDescription, boolean isAlert);

    void showErrorTransaction(int text);

    void showSendConfirmation();

    void saveDataAlert(String title, String description, String latitude, String longitude);

    void saveDataPoster(String title, String description, String latitude, String longitude, byte[] image);

    void buildPicturePicker(int requestImagePoster);

    List<Alert> getAlerts();

    List<Poster> getPosters();

    Location getLatestLocation();

}
