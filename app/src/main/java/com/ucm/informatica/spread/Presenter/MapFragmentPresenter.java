package com.ucm.informatica.spread.Presenter;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Contracts.CoordContract;
import com.ucm.informatica.spread.Fragments.MapFragment;
import com.ucm.informatica.spread.SmartContract;
import com.ucm.informatica.spread.View.MapFragmentView;

import java.math.BigInteger;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragmentPresenter {

    private CoordContract coordContract;
    private SmartContract smartContract;


    private MapFragmentView mapFragmentView;
    private MapFragment mapFragment;

    public MapFragmentPresenter(MapFragmentView mapFragmentView, MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.mapFragmentView = mapFragmentView;
    }

    public void loadData() {
        if(coordContract == null) { //|| !nameContract.isValid()) {
            smartContract = ((MainTabActivity) mapFragment.getActivity()).getSmartContract();
            coordContract = ((MainTabActivity) mapFragment.getActivity()).getNameContract();
        }
        coordContract.getEventsCount().observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (countCoords) -> {
                            for(int i =0; i<countCoords.intValue(); i++){
                                coordContract.getEventByIndex(BigInteger.valueOf(i)).observable()
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                (result) ->
                                                    mapFragmentView.loadCoordinateFromContract(
                                                            result.getValue1(),
                                                            result.getValue2(),
                                                            result.getValue3(),
                                                            result.getValue4())
                                                ,
                                                (error) -> mapFragmentView.showErrorTransition()
                                        );
                            }
                        },
                        (error) -> mapFragmentView.showErrorTransition()
                );
    }

    public void saveData(String title, String description, String longitude, String latitude) {
        if(coordContract == null) { //|| !nameContract.isValid()) {
            // TODO : isValid se ejecuta sincronamente tonses cuidado si lo lanzas dos veces sin haber acabado, va a saltar NetworkorMainException
            smartContract = ((MainTabActivity) mapFragment.getActivity()).getSmartContract();
            coordContract = ((MainTabActivity) mapFragment.getActivity()).getNameContract();
        }

        coordContract.addEvent(title,description,latitude,longitude).observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (result) -> {}
                        ,
                        (error) -> mapFragmentView.showErrorTransition()
                );

    }
}
