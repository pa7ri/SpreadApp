package com.ucm.informatica.spread.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
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

import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LATITUDE_END;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LATITUDE_START;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LONGITUDE_END;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LONGITUDE_START;

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
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
        historicalFramentPresenter = new HistoricalFragmentPresenter(this,this);
        historicalFramentPresenter.start(getCameraBounds());
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

    private Pair<Pair<Double,Double>,Pair<Double,Double>> getCameraBounds(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);
        Pair<Double,Double> latitudeBound = new Pair<>(Double.valueOf(sharedPreferences.getString(CAMERA_BOUND_LATITUDE_START, "40")),
                Double.valueOf(sharedPreferences.getString(CAMERA_BOUND_LATITUDE_END, "41")));
        Pair<Double,Double> longitudeBound = new Pair<>(Double.valueOf(sharedPreferences.getString(CAMERA_BOUND_LONGITUDE_START, "-3")),
                Double.valueOf(sharedPreferences.getString(CAMERA_BOUND_LONGITUDE_END, "-4")));
        return new Pair<>(latitudeBound,longitudeBound);
    }
}
