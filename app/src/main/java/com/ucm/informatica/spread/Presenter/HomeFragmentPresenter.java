package com.ucm.informatica.spread.Presenter;

import android.widget.Toast;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Contracts.NameContract;
import com.ucm.informatica.spread.Fragments.HomeFragment;
import com.ucm.informatica.spread.SmartContract;
import com.ucm.informatica.spread.View.HomeFragmentView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragmentPresenter {

    private NameContract nameContract;
    private SmartContract smartContract;

    private HomeFragmentView homeFragmentView;
    private HomeFragment homeFragment;

    public HomeFragmentPresenter(HomeFragmentView homeFragmentView, HomeFragment homeFragment){
        this.homeFragmentView = homeFragmentView;
        this.homeFragment = homeFragment;
    }

    public void saveData(String data) {
        if(nameContract == null) { //|| !nameContract.isValid()) {
            // TODO : isValid se ejecuta sincronamente tonses cuidado si lo lanzas dos veces sin haber acabado, va a saltar NetworkorMainException
            smartContract = ((MainTabActivity) homeFragment.getActivity()).getSmartContract();
            nameContract = ((MainTabActivity) homeFragment.getActivity()).getNameContract();
        }

        nameContract.setName(data).observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (result) -> homeFragmentView.showSuccessfulTransition("Bloque : " + result.getBlockNumber().toString()
                                + " , Gas usado : " + result.getGasUsed().toString())
                        ,
                        (error) -> homeFragmentView.showErrorTransition()
                );

    }

    public void loadData() {
        if(nameContract == null) { //|| !nameContract.isValid()) {
            smartContract = ((MainTabActivity) homeFragment.getActivity()).getSmartContract();
            nameContract = ((MainTabActivity) homeFragment.getActivity()).getNameContract();
        }
        nameContract.getName().observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (result) -> homeFragmentView.showSuccessfulTransition(result)
                        ,
                        (error) -> homeFragmentView.showErrorTransition()
                );
    }
}
