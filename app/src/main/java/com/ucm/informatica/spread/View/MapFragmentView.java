package com.ucm.informatica.spread.View;

import com.ucm.informatica.spread.Model.LocationMode;

public interface MapFragmentView {

    void showNewMarkerIntoMap(double latitude, double longitude, String markerTitle, String markerDescription);

    void renderLocationView(LocationMode state);

    void showFeedback();

    void showError(int text);

}
