package com.ucm.informatica.spread.Presenter;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Contracts.CoordContract;
import com.ucm.informatica.spread.Fragments.HomeFragment;
import com.ucm.informatica.spread.SmartContract;
import com.ucm.informatica.spread.View.HomeFragmentView;

import java.math.BigInteger;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragmentPresenter {

    private CoordContract coordContract;
    private SmartContract smartContract;

    private HomeFragmentView homeFragmentView;
    private HomeFragment homeFragment;

    public HomeFragmentPresenter(HomeFragmentView homeFragmentView, HomeFragment homeFragment){
        this.homeFragmentView = homeFragmentView;
        this.homeFragment = homeFragment;
    }

    public void saveData(String title, String description, String longitude, String latitude) {
        if(coordContract == null) { //|| !nameContract.isValid()) {
            // TODO : isValid se ejecuta sincronamente tonses cuidado si lo lanzas dos veces sin haber acabado, va a saltar NetworkorMainException
            smartContract = ((MainTabActivity) homeFragment.getActivity()).getSmartContract();
            coordContract = ((MainTabActivity) homeFragment.getActivity()).getNameContract();
        }

        coordContract.addEvent(title,description,latitude,longitude).observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (result) -> homeFragmentView.showSuccessfulStoredTransition(
                                "Bloque : " + result.getBlockNumber().toString()
                                + " , Gas usado : " + result.getGasUsed().toString())
                        ,
                        (error) -> homeFragmentView.showErrorTransition()
                );

    }
}
