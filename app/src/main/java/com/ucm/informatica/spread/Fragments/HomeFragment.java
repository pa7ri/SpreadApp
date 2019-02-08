package com.ucm.informatica.spread.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Presenter.HomeFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.HomeFragmentView;
public class HomeFragment extends Fragment implements HomeFragmentView{

    private HomeFragmentPresenter homeFragmentPresenter;

    private Flashbar flashbar;

    private TextView nameText;
    private Button helpButton;

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
        helpButton = v.findViewById(R.id.helpButton);
    }

    private void setupListeners(){
        helpButton.setOnClickListener(view -> {
                Location location = ((MainTabActivity) getActivity()).getLocation();
                if(location != null) {
                    homeFragmentPresenter.saveData(getResources().getString(R.string.button_help),
                            getResources().getString(R.string.button_help_description),
                            Double.toString(location.getLongitude()),
                            Double.toString(location.getLatitude()));
                } else {
                    ((MainTabActivity) getActivity()).getAlertSnackBarGPS().show();
                }
        });
    }

    @Override
    public void showSuccessfulStoredTransition(String result) {
        nameText.setText(result);
    }

    @Override
    public void showSuccessfulLoadedTransition(String title, String description, String latitude, String longitude) {
        nameText.setText(" Título : " +title + "\n" +
                " Descripción : " +description + "\n" +
                " Latitud : " + latitude + "\n" +
                " Longitud : " + longitude);
    }

    @Override
    public void showErrorTransition() {
        ((MainTabActivity) getActivity()).getErrorSnackBar(R.string.snackbar_alert_transaction).show();
    }
}
