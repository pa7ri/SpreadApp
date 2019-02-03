package com.ucm.informatica.spread.Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Contracts.NContract;
import com.ucm.informatica.spread.Presenter.HomeFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.SmartContract;
import com.ucm.informatica.spread.View.HomeFragmentView;

import java.io.IOException;
import java.util.Objects;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class HomeFragment extends Fragment implements HomeFragmentView{

    private HomeFragmentPresenter homeFragmentPresenter;


    private TextView nameText;
    private EditText nameEditText;
    private Button saveButton;
    private Button loadButton;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        homeFragmentPresenter = new HomeFragmentPresenter(this, this);
        setupListeners();
        return view;
    }
    private void initView(View v) {
        nameText = v.findViewById(R.id.nameText);
        nameEditText = v.findViewById(R.id.nameEditText);
        saveButton = v.findViewById(R.id.saveNameButton);
        loadButton = v.findViewById(R.id.loadNameButton);
    }

    private void setupListeners(){
        saveButton.setOnClickListener(view -> homeFragmentPresenter.saveData(nameEditText.getText().toString()));
        loadButton.setOnClickListener(view -> homeFragmentPresenter.loadData());
    }

    @Override
    public void showSuccessfulTransition(String result) {
        nameText.setText(result);
    }

    @Override
    public void showErrorTransition() {
        Snackbar.make(Objects.requireNonNull(this.getView()), "Ha habido un error en la transicción", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
