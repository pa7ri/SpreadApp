package com.ucm.informatica.spread.View;

public interface HomeFragmentView {

    void showSuccessfulStoredTransition(String result);

    void showSuccessfulLoadedTransition(String title,String description,String latitude,String longitude);

    void showErrorTransition();
}
