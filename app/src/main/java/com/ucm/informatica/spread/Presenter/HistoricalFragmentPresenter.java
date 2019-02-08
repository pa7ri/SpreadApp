package com.ucm.informatica.spread.Presenter;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Fragments.HistoricalFragment;
import com.ucm.informatica.spread.Model.Event;
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
        List<Event> historicalList = ((MainTabActivity)historicalFragment.getActivity()).getDataSmartContract();
        CustomRecyclerAdapter customRecyclerAdapter = new CustomRecyclerAdapter(historicalFragment.getContext(), historicalList);
        historicalFragmentView.initView(customRecyclerAdapter);
    }
}
