package com.ucm.informatica.spread.Presenter;

import android.graphics.Color;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Contracts.CoordContract;
import com.ucm.informatica.spread.Fragments.MapFragment;
import com.ucm.informatica.spread.Model.Event;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.SmartContract;
import com.ucm.informatica.spread.View.MapFragmentView;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragmentPresenter {

    private CoordContract coordContract;

    private MapFragmentView mapFragmentView;
    private MapFragment mapFragment;

    public MapFragmentPresenter(MapFragmentView mapFragmentView, MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.mapFragmentView = mapFragmentView;
    }

    public void start(){
        List<Event> historicalList = ((MainTabActivity)mapFragment.getActivity()).getDataSmartContract();
        for (Event event:historicalList) {
            mapFragmentView.loadCoordinateFromContract(event.getTitle(),
                    event.getDescription(),
                    event.getLatitude(),
                    event.getLongitude());
        }
    }

    public void saveData(String title, String description, String longitude, String latitude) {
        if(coordContract == null) {
            coordContract = ((MainTabActivity) mapFragment.getActivity()).getNameContract();
        }

        coordContract.addEvent(title,description,latitude,longitude, String.valueOf(System.currentTimeMillis())).observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (result) -> mapFragmentView.showFeedback()
                        ,
                        (error) -> mapFragmentView.showError(R.string.snackbar_alert_transaction)
                );

    }

    public int getColorPolygon(){
        Random rand = new Random();
        switch (rand.nextInt(3)) {
            case 0 : return Color.GREEN;
            case 1 : return Color.YELLOW;
            default: return Color.RED;
        }
    }
}
