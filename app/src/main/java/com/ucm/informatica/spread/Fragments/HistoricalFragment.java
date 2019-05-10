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

import com.ucm.informatica.spread.Presenter.HistoricalFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.FlashBarBuilder;
import com.ucm.informatica.spread.Utils.HistoricalRecyclerAdapter;
import com.ucm.informatica.spread.View.HistoricalFragmentView;

import java.util.Objects;

import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LATITUDE_END;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LATITUDE_START;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LONGITUDE_END;
import static com.ucm.informatica.spread.Utils.Constants.Map.CAMERA_BOUND_LONGITUDE_START;

public class HistoricalFragment extends Fragment implements HistoricalFragmentView{

    private HistoricalFragmentPresenter historicalFramentPresenter;
    private RecyclerView historicalListView;
    private SharedPreferences sharedPreferences;
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
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();

        historicalFramentPresenter = new HistoricalFragmentPresenter(this,this);
        historicalFramentPresenter.start(getCameraBounds());
    }

    @Override
    public void initView(HistoricalRecyclerAdapter adapter) {
        historicalListView = view.findViewById(R.id.historicalListView);

        historicalListView.setLayoutManager(new LinearLayoutManager(getContext()));
        historicalListView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL));

        historicalListView.setAdapter(adapter);
    }

    @Override
    public void showErrorTransaction() {
        new FlashBarBuilder(getActivity()).getErrorSnackBar(R.string.snackbar_alert_transaction).show();
    }

    private Pair<Pair<Double,Double>,Pair<Double,Double>> getCameraBounds(){
        Pair<Double,Double> latitudeBound = new Pair<>(Double.valueOf(sharedPreferences.getString(CAMERA_BOUND_LATITUDE_START, "-90")),
                Double.valueOf(sharedPreferences.getString(CAMERA_BOUND_LATITUDE_END, "90")));
        Pair<Double,Double> longitudeBound = new Pair<>(Double.valueOf(sharedPreferences.getString(CAMERA_BOUND_LONGITUDE_START, "-180")),
                Double.valueOf(sharedPreferences.getString(CAMERA_BOUND_LONGITUDE_END, "180")));
        return new Pair<>(latitudeBound,longitudeBound);
    }
}
