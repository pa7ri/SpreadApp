package com.ucm.informatica.spread.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Presenter.HistoricalFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.CustomRecyclerAdapter;
import com.ucm.informatica.spread.View.HistoricalFragmentView;

public class HistoricalFragment extends Fragment implements HistoricalFragmentView{

    private HistoricalFragmentPresenter historicalFramentPresenter;
    private RecyclerView historicalListView;

    private View view;


    public HistoricalFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_historical, container, false);
        historicalFramentPresenter = new HistoricalFragmentPresenter(this,this);
        historicalFramentPresenter.start();
        return view;
    }

    @Override
    public void initView(CustomRecyclerAdapter adapter) {
        historicalListView = view.findViewById(R.id.historicalListView);

        historicalListView.setLayoutManager(new LinearLayoutManager(getContext()));
        historicalListView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        historicalListView.setAdapter(adapter);
    }

    @Override
    public void showErrorTransaction() {
        ((MainTabActivity) getActivity()).getErrorSnackBar(R.string.snackbar_alert_transaction).show();
    }
}
