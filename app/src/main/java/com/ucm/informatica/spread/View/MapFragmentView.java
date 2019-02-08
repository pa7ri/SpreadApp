package com.ucm.informatica.spread.View;

public interface MapFragmentView {

    void loadCoordinateFromContract(String title,String description,Double latitude,Double longitude);

    void showFeedback();

    void showError(int text);

}
