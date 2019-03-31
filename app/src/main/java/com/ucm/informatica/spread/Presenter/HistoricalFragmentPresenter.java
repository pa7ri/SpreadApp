package com.ucm.informatica.spread.Presenter;

import android.support.v4.util.Pair;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Fragments.HistoricalFragment;
import com.ucm.informatica.spread.Model.Alert;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.Utils.HistoricalRecyclerAdapter;
import com.ucm.informatica.spread.View.HistoricalFragmentView;

import java.util.ArrayList;
import java.util.List;

public class HistoricalFragmentPresenter {

    private HistoricalFragmentView historicalFragmentView;
    private HistoricalFragment historicalFragment;

    private List<Alert> historicalAlertList;
    private List<Poster> historicalPosterList;


    public HistoricalFragmentPresenter(HistoricalFragment historicalFragment, HistoricalFragmentView historicalFragmentView) {
        this.historicalFragment = historicalFragment;
        this.historicalFragmentView = historicalFragmentView;
    }

    public void start(Pair<Pair<Double,Double>,Pair<Double,Double>> cameraBounds){
        historicalAlertList = ((MainTabActivity)historicalFragment.getActivity()).getDataAlertSmartContract();
        historicalPosterList = ((MainTabActivity)historicalFragment.getActivity()).getDataPosterSmartContract();

        filterItemsByCameraBounds(cameraBounds);

        HistoricalRecyclerAdapter historicalRecyclerAdapter = new HistoricalRecyclerAdapter(historicalFragment.getContext(),
                (MainTabActivity) historicalFragment.getActivity(), historicalAlertList, historicalPosterList);
        historicalFragmentView.initView(historicalRecyclerAdapter);
    }

    private void filterItemsByCameraBounds(Pair<Pair<Double,Double>,Pair<Double,Double>> cameraBounds){
        List<Alert> alertList = new ArrayList<>();
        List<Poster> posterList = new ArrayList<>();
        for (Alert alert : historicalAlertList) {
            if(alert.getLatitude()>cameraBounds.first.first && alert.getLatitude()<cameraBounds.first.second
                    && alert.getLongitude()>cameraBounds.second.first && alert.getLongitude()<cameraBounds.second.second) {
                alertList.add(alert);
            }
        }
        for (Poster poster : historicalPosterList) {
            if(poster.getLatitude()>cameraBounds.first.first && poster.getLatitude()<cameraBounds.first.second
                    && poster.getLongitude()>cameraBounds.second.first && poster.getLongitude()<cameraBounds.second.second) {
                posterList.add(poster);
            }
        }
        historicalAlertList=alertList;
        historicalPosterList=posterList;
    }

}
