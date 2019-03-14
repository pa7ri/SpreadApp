package com.ucm.informatica.spread.Presenter;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Fragments.HistoricalFragment;
import com.ucm.informatica.spread.Model.Alert;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.Utils.CustomRecyclerAdapter;
import com.ucm.informatica.spread.View.HistoricalFragmentView;

import java.util.List;

public class HistoricalFragmentPresenter {


    private HistoricalFragmentView historicalFragmentView;
    private HistoricalFragment historicalFragment;

    public HistoricalFragmentPresenter(HistoricalFragment historicalFragment, HistoricalFragmentView historicalFragmentView) {
        this.historicalFragment = historicalFragment;
        this.historicalFragmentView = historicalFragmentView;
    }

    public void start(){
        List<Alert> historicalAlertList = ((MainTabActivity)historicalFragment.getActivity()).getDataAlertSmartContract();
        List<Poster> historicalPosterList = ((MainTabActivity)historicalFragment.getActivity()).getDataPosterSmartContract();
        CustomRecyclerAdapter customRecyclerAdapter = new CustomRecyclerAdapter(
                historicalFragment.getContext(), historicalAlertList, historicalPosterList);
        historicalFragmentView.initView(customRecyclerAdapter);
    }
}
