package com.ucm.informatica.spread.Presenter;

import android.content.res.Resources;
import android.location.Location;

import com.ucm.informatica.spread.Contracts.AlertContract;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.HomeFragmentView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragmentPresenter {

    private AlertContract alertContract;

    private HomeFragmentView homeFragmentView;

    public HomeFragmentPresenter(HomeFragmentView homeFragmentView, AlertContract alertContract){
        this.homeFragmentView = homeFragmentView;
        this.alertContract = alertContract;
    }

    public void onHelpButtonPressed(Location location, Resources resources) {
        if(location != null) {
            saveData(resources.getString(R.string.button_help),
                    resources.getString(R.string.button_help_description),
                    Double.toString(location.getLongitude()),
                    Double.toString(location.getLatitude()));
        } else {
            homeFragmentView.showErrorGPS();
        }
    }

    private void saveData(String title, String description, String longitude, String latitude) {
        if(alertContract != null) { //|| !nameContract.isValid()) {
            // TODO : isValid se ejecuta sincronamente tonses cuidado si lo lanzas dos veces sin haber acabado, va a saltar NetworkorMainException
            alertContract.addAlert(title, description, latitude, longitude, String.valueOf(System.currentTimeMillis())).observable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (result) ->
                                homeFragmentView.showSuccessfulStoredTransition(
                                        "Bloque : " + result.getBlockNumber().toString()
                                                + " , Gas usado : " + result.getGasUsed().toString())
                            ,
                            (error) -> homeFragmentView.showErrorTransition()
                    );
        } else {
            homeFragmentView.showErrorTransition();
        }

    }

    public void start() {
        homeFragmentView.initView();
        homeFragmentView.setupListeners();
    }

}
