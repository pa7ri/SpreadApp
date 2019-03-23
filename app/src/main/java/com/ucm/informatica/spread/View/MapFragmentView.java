package com.ucm.informatica.spread.View;

import com.ucm.informatica.spread.Model.LocationMode;

public interface MapFragmentView {

    void renderLocationView(LocationMode state);

    void showNewMarkerIntoMap(double latitude, double longitude, String markerTitle, String markerDescription, boolean isAlert);

    void showErrorTransaction(int text);

    void showSendConfirmation();
}
